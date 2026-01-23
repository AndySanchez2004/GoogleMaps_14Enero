package com.example.googlemaps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Banderas {

    private String imagenbandera;
    private String nombrebandera;
    private String alpha2Code;

    public Banderas(JSONObject a) throws JSONException {
        alpha2Code = a.getString("alpha2Code");
        imagenbandera = "http://www.geognos.com/api/en/countries/flag/" + alpha2Code + ".png";
        nombrebandera = a.getString("name");
    }

    public static ArrayList<Banderas> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<Banderas> banderas = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            banderas.add(new Banderas(datos.getJSONObject(i)));
        }
        return banderas;
    }

    public String getNombrebandera() {
        return nombrebandera;
    }

    public void setNombrebandera(String nombrebandera) {
        this.nombrebandera = nombrebandera;
    }

    public String getImagenbandera() {
        return imagenbandera;
    }

    public void setImagenbandera(String imagenbandera) {
        this.imagenbandera = imagenbandera;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }
}
