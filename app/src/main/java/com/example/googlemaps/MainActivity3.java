package com.example.googlemaps;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.googlemaps.WebServices.Asynchtask;
import com.example.googlemaps.WebServices.WebService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity implements Asynchtask, OnMapReadyCallback {

    private GridView lstBanderasView;
    private LinearLayout detailViewContainer;
    private GoogleMap mMap;
    private TextView txtName, txtCapital, txtCodeIso2, txtCodeIsoNum, txtCodeIso3, txtCodeFips, txtTelPrefix, txtCenter, txtRectangle;
    private ImageView imgDetailFlag;
    private ArrayList<Banderas> lstBanderas;
    private Banderas selectedBandera;

    private enum ApiCallType {FLAGS, COUNTRY_DETAILS}
    private ApiCallType currentApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        lstBanderasView = findViewById(R.id.lstBanderas);
        detailViewContainer = findViewById(R.id.detailViewContainer);
        imgDetailFlag = findViewById(R.id.imgDetailFlag);
        txtName = findViewById(R.id.txtName);
        txtCapital = findViewById(R.id.txtCapital);
        txtCodeIso2 = findViewById(R.id.txtCodeIso2);
        txtCodeIsoNum = findViewById(R.id.txtCodeIsoNum);
        txtCodeIso3 = findViewById(R.id.txtCodeIso3);
        txtCodeFips = findViewById(R.id.txtCodeFips);
        txtTelPrefix = findViewById(R.id.txtTelPrefix);
        txtCenter = findViewById(R.id.txtCenter);
        txtRectangle = findViewById(R.id.txtRectangle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadFlags();

        lstBanderasView.setOnItemClickListener((parent, view, position, id) -> {
            selectedBandera = lstBanderas.get(position);
            loadCountryDetails(selectedBandera.getAlpha2Code());
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (detailViewContainer.getVisibility() == View.VISIBLE) {
                    showGridView();
                } else {
                    finish();
                }
            }
        });
    }

    private void loadFlags() {
        currentApiCall = ApiCallType.FLAGS;
        Map<String, String> datos = new HashMap<>();
        WebService ws = new WebService("http://api.countrylayer.com/v2/all", datos, this, this);
        ws.execute("GET", "access_key", "ea43116e313d3797e81bd670b115a898");
    }

    private void loadCountryDetails(String alpha2Code) {
        currentApiCall = ApiCallType.COUNTRY_DETAILS;
        Map<String, String> datos = new HashMap<>();
        WebService ws = new WebService("http://www.geognos.com/api/en/countries/info/" + alpha2Code + ".json", datos, this, this);
        ws.execute("GET");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void processFinish(String result) {
        try {
            if (currentApiCall == ApiCallType.FLAGS) {
                handleFlagsResponse(result);
            } else {
                handleCountryDetailsResponse(result);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
            Log.e("WebServiceError", "Error parsing JSON: " + result, e);
        }
    }

    private void handleFlagsResponse(String result) throws JSONException {
        if (result.startsWith("[") && result.endsWith("]")) {
            JSONArray JSONlista = new JSONArray(result);
            lstBanderas = Banderas.JsonObjectsBuild(JSONlista);
            BanderasAdaptador adaptadorBandera = new BanderasAdaptador(this, lstBanderas);
            lstBanderasView.setAdapter(adaptadorBandera);
        } else {
            Toast.makeText(this, "Respuesta no válida del servicio de banderas", Toast.LENGTH_LONG).show();
        }
    }

    private void handleCountryDetailsResponse(String result) throws JSONException {
        JSONObject jsonResponse = new JSONObject(result);
        if (jsonResponse.optString("StatusMsg", "").equals("OK") && jsonResponse.has("Results")) {
            JSONObject results = jsonResponse.getJSONObject("Results");

            if (selectedBandera != null) {
                Glide.with(this).load(selectedBandera.getImagenbandera()).into(imgDetailFlag);
            }

            txtName.setText(results.optString("Name", "N/A"));
            txtCapital.setText("Capital: " + (results.has("Capital") ? results.getJSONObject("Capital").optString("Name", "N/A") : "N/A"));

            JSONObject countryCodes = results.getJSONObject("CountryCodes");
            txtCodeIso2.setText("Code ISO 2: " + countryCodes.optString("iso2", "N/A"));
            txtCodeIsoNum.setText("Code ISO Num: " + countryCodes.optString("isoN", "N/A"));
            txtCodeIso3.setText("Code ISO 3: " + countryCodes.optString("iso3", "N/A"));
            txtCodeFips.setText("Code FIPS: " + countryCodes.optString("fips", "N/A"));
            txtTelPrefix.setText("Tel Prefix: " + results.optString("TelPref", "N/A"));

            if (mMap != null && results.has("GeoRectangle") && results.has("GeoPt")) {
                mMap.clear();

                JSONArray geoPt = results.getJSONArray("GeoPt");
                double lat = geoPt.getDouble(0);
                double lng = geoPt.getDouble(1);
                txtCenter.setText(String.format(Locale.US, "Center: %.2f, %.2f", lat, lng));

                JSONObject geoRectangle = results.getJSONObject("GeoRectangle");
                double north = geoRectangle.getDouble("North");
                double south = geoRectangle.getDouble("South");
                double east = geoRectangle.getDouble("East");
                double west = geoRectangle.getDouble("West");
                txtRectangle.setText(String.format(Locale.US, "Rectangle: N:%.2f, S:%.2f, E:%.2f, O:%.2f", north, south, east, west));

                mMap.addPolygon(new PolygonOptions()
                        .add(new LatLng(north, west)).add(new LatLng(north, east))
                        .add(new LatLng(south, east)).add(new LatLng(south, west))
                        .add(new LatLng(north, west)));

                LatLng countryLocation = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(countryLocation, 4));
            }
            showDetailView();
        } else {
            Toast.makeText(this, "No se encontraron resultados para el país.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGridView() {
        lstBanderasView.setVisibility(View.VISIBLE);
        detailViewContainer.setVisibility(View.GONE);
    }

    private void showDetailView() {
        lstBanderasView.setVisibility(View.GONE);
        detailViewContainer.setVisibility(View.VISIBLE);
    }
}