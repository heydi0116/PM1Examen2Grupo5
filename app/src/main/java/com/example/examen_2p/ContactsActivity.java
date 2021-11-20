package com.example.examen_2p;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    final int REQUEST_GPS= 10;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter contactsAdapter;
    private ArrayList<Contactos> arrayContacts = new ArrayList<>();

    private Button btnBack;
    private EditText txbBuscar;
    private Integer ContactoPos;
    private Boolean keyDown = false;
    private String nombreC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        txbBuscar = findViewById(R.id.txbBuscar);
        btnBack = findViewById(R.id.btnBack);

        mRecyclerView = findViewById(R.id.rvContacs);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        obtenerListaContactos();

        //sendRequest();
        /*arrayContacts.add(new Contactos("Fernando Martines", "99874356"));
        arrayContacts.add(new Contactos("Maria Isabel Ochoa", "97561023"));
        arrayContacts.add(new Contactos("Katherine Izaguirre", "87502331"));
        arrayContacts.add(new Contactos("Osman Aldahir Altamirano", "96345012"));*/

        contactsAdapter = new MyAdapter(ContactsActivity.this, arrayContacts);
        mRecyclerView.setAdapter(contactsAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        createSingleListDialog(position);
                        ContactoPos = position;
                    }
                })
        );

        txbBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                keyDown = true;
                obtenerListaContactos();
                ActualizarLv();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdate = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentUpdate);
            }
        });

    }

    private void ActualizarLv() {
        contactsAdapter = new MyAdapter(ContactsActivity.this, arrayContacts);
        mRecyclerView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();
        arrayContacts.clear();
    }

    private void obtenerListaContactos() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = RestApiMetods.GetApiContacts;

        //Realizamos la peticion a la URL del endPoint
        StringRequest strinRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray ContactsArray = obj.getJSONArray("contacto");

                    for(int i=0; i < ContactsArray.length(); i++){
                        //aqui se puede guardar uno por uno
                        JSONObject contactObject = ContactsArray.getJSONObject(i);
                        Contactos cont = new Contactos(contactObject.getString("name"),
                                contactObject.getString("tel"),
                                contactObject.getString("latitud"),
                                contactObject.getString("longitud"));

                        //arrayContacts.add(cont);
                        arrayContacts.add(new Contactos(cont.getName()));
                        //arrayContacts.add(new Contactos(cont.getImg()));
                    }

                    contactsAdapter = new MyAdapter(ContactsActivity.this, arrayContacts);
                    mRecyclerView.setAdapter(contactsAdapter);

                }catch (JSONException ex){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.toString());

            }
        });
        queue.add(strinRequest);
    }

    public AlertDialog createSingleListDialog(int position) {
        nombreC = arrayContacts.get(position).getName();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final CharSequence[] items = new CharSequence[3];

        items[0] = "Ubicación";
        items[1] = "Actualizar";
        items[2] = "Eliminar";

        builder.setTitle("Opciones")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(items[which] == "Ubicación") {
                            if(ActivityCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                                //Intent intentGPS = new Intent(Intent.ACTION_PICK, Uri.parse(""));
                                //startActivity(intentGPS);
                            }else{
                                ActivityCompat.requestPermissions(ContactsActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS);
                            }
                        }else if(items[which] == "Actualizar") {
                            Intent intentUpdate = new Intent(getApplicationContext(), MainActivity.class);
                            Accion.Contacto_ID  = arrayContacts.get(position).getId();
                            String Nombre = arrayContacts.get(position).getName();
                            String NumeroTel = arrayContacts.get(position).getTel();
                            /*String Latitud = arrayContacts.get(position).getLat();
                            String Longitud = arrayContacts.get(position).getLon();*/

                            intentUpdate.putExtra("NombreUpdate", Nombre);
                            intentUpdate.putExtra("NumeroTelUpdate", NumeroTel);
                            /*intentUpdate.putExtra("LatitudUpdate", Latitud);
                            intentUpdate.putExtra("LongitudUpdate", Longitud);*/

                            Accion.bandera = true;
                            startActivity(intentUpdate);
                        }else if(items[which] == "Eliminar"){
                            keyDown = false;
                           // SQLiteDatabase db = conexion.getWritableDatabase();
                            String idElemento = arrayContacts.get(position).getId();
                            //db.execSQL("DELETE FROM "+ Transacciones.tblContactos +" WHERE id = "+ idElemento);
                            arrayContacts.remove(position);
                            txbBuscar.setText("");
                            //obtenerListaContactos();
                            //ActualizarLv();
                            Toast.makeText(getApplicationContext(), "Contacto Eliminado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.show();
    }

}