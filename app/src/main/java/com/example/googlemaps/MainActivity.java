package com.example.googlemaps;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mapa;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapquevedo);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(true);

//        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(-1.0222853382012584, -79.46115085244955), 13);mapa.moveCamera(camUpd1);
//
//        LatLng italia = new LatLng(43.72299051060299, 10.396241628173426);
//
//        CameraPosition camPos = new CameraPosition.Builder()
//                .target(italia)
//                .zoom(19)
//                .bearing(45)      //noreste arriba
//                .tilt(70)         //punto de vista de la cámara 70 grados
//                .build();
//
//        CameraUpdate camUpd3 =
//                CameraUpdateFactory.newCameraPosition(camPos);
//
//        mapa.animateCamera(camUpd3);

        PolylineOptions lineas = new PolylineOptions()
        .add(new LatLng(-1.0122664824723606, -79.46714054058333))
        .add(new LatLng(-1.0119605017244087, -79.47144824065798))
        .add(new LatLng(-1.012885446829087, -79.4715387278733))
        .add(new LatLng(-1.0131668262183162, -79.46730228046019))
        .add(new LatLng(-1.0122664824723606, -79.46714054058333));
        lineas.width(8);
        lineas.color(Color.RED);
        mapa.addPolyline(lineas);

        LatLng punto = new LatLng(-1.0128713900973114, -79.4693271559106);mapa.addMarker(new MarkerOptions().position(punto).title("Universidad Tecnica Estatal de Quevedo"));


    }
}