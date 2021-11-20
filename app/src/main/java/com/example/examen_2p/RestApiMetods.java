package com.example.examen_2p;

public class RestApiMetods {
    private static final String ipAddress = "192.168.0.2";
    public static final String GETendPoint = "listaContactos.php";
    public static final String POSTendPoint = "crear.php";
    public static final String POSphoto= "UploadFile.php";

    public static final String GetApiContacts = "http://" + ipAddress + "/contactos_crud/" + GETendPoint;
    public static final String PostApiContacts = "http://" + ipAddress + "/contactos_crud/" + POSTendPoint;
    public  static final String UploadFileApiContacts = "http://" + ipAddress + "/contactos_crud/" + POSphoto;

    public static final String fieldNombre = "nombre";
    public static final String fieldTel = "tel";
    public static final String fieldLatitud = "latitud";
    public static final String fieldLongitus = "longitud";
    //public static final String fieldEdad = "edad";
}

