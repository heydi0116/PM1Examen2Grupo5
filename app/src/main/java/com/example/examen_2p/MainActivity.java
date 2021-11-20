package com.example.examen_2p;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static final int RESULT_GALLERY = 101;

    private Bitmap photo;
    private TextView txbName, txbTel, txbLat, txbLong;
    private ImageView imvPhoto;
    private Button btnSave, btnContactos;
    private LocationManager ubicacion;
    TextView mensaje1;
    TextView mensaje2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imvPhoto = findViewById(R.id.imvPhoto);
        btnContactos = findViewById(R.id.btnContactos);
        btnSave = findViewById(R.id.btnSave);
        txbName = findViewById(R.id.txbName);
        txbTel = findViewById(R.id.txbTel);
        txbLat = findViewById(R.id.txbLatitud);
        txbLong = findViewById(R.id.txbLongitud);

        mensaje1 = (TextView) findViewById(R.id.txbLatitud);
        mensaje2 = (TextView) findViewById(R.id.txbLongitud);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }


        if(Accion.bandera){
            String NombreUpdate = getIntent().getStringExtra("NombreUpdate");
            String NumeroTelUpdate = getIntent().getStringExtra("NumeroTelUpdate");
            /*String LatitudUpdate = getIntent().getStringExtra("LatitudUpdate");
            String LongitudUpdate = getIntent().getStringExtra("LongitudUpdate");*/

            txbName.setText(NombreUpdate);
            txbTel.setText(NumeroTelUpdate);
            /*txbLat.setText(LatitudUpdate);
            txbLong.setText(LongitudUpdate);*/

            btnSave.setText("Actualizar");
            btnContactos.setText("Cancelar");
        }

        imvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaleriaImagenes();
            }
        });

        imvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaleriaImagenes();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Accion.bandera){
                    if(txbName.length() != 0 || txbTel.length() != 0 || txbLat.length() != 0 || txbLong.length() != 0){
                        //datos de aws
                        Toast.makeText(getApplicationContext(), "Dato actualizado", Toast.LENGTH_LONG).show();
                        btnSave.setText("Agregar Contactos");
                        btnContactos.setText("Contactos");
                        Accion.bandera = false;
                        ClearScreen();
                    }else{
                        Toast.makeText(getApplicationContext(), "Complete Todos los Campos", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    if(txbName.length() != 0 || txbTel.length() != 0 || txbLat.length() != 0 || txbLong.length() != 0){
                        AgregarContactos();
                    }else{
                        Toast.makeText(getApplicationContext(), "Complete Todos los Campos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Accion.bandera){
                    btnSave.setText("Agregar Contactos");
                    btnContactos.setText("Contactos");
                    Accion.bandera = false;
                    ClearScreen();
                }else{
                    Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void AgregarContactos() {
        //datos de aws
        Toast.makeText(getApplicationContext(), "Contacto Guardado", Toast.LENGTH_SHORT).show();
        ClearScreen();
    }

    private void ClearScreen() {
        txbName.setText("");
        txbTel.setText("");
        txbLat.setText("");
        txbLong.setText("");
    }

    private void GaleriaImagenes() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        imageUri = data.getData();
        imvPhoto.setImageURI(imageUri);

        try {
            photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

        }catch (Exception ex){
        }
    }

    private void sendImage() {
        String url = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                }catch (Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Respuesta", error.toString());
            }
        })
        {
            protected Map<String, String> getParams(){
                String image = getStringImage(photo);
                Map<String, String> parametros = new HashMap<>();
                parametros.put("IMAGEN", image);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String getStringImage(Bitmap photo) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, ba);
        byte[] imgByte = ba.toByteArray();
        String encode = Base64.encodeToString(imgByte, Base64.DEFAULT);
        return encode;
    }


    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        mensaje1.setText("Localizacion agregada");
        mensaje2.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    //  mensaje2.setText("Mi direccion es: \n"
                    //          + DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();


            mensaje1.setText(String.valueOf(loc.getLatitude()));
            mensaje2.setText(String.valueOf(loc.getLongitude()));
            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            mensaje1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            mensaje1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}