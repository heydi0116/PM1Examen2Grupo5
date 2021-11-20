package com.example.examen_2p;

import android.graphics.Bitmap;

public class Contactos {
    private String id;
    private String img;
    private String name;
    private String tel;
    private String lat;
    private String lon;


    public Contactos(String name) {
        this.name = name;
    }

    public Contactos(String id, String img, String name, String tel, String lat, String lon) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.tel = tel;
        this.lat = lat;
        this.lon = lon;
    }

    public Contactos(String name, String tel, String lat, String lon) {
        this.name = name;
        this.tel = tel;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
