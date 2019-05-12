package com.example.evoy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**Fragment que se encarga de mostrar la hubicación en un mapa de unas coordenadas dadas**/
public class ShowLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude;
    double longitude;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        Intent intencion = getIntent();

        if (intencion.hasExtra("latitude") && intencion.hasExtra("longitude") && intencion.hasExtra("location")) {

            latitude = Double.parseDouble(intencion.getStringExtra("latitude"));
            longitude = Double.parseDouble(intencion.getStringExtra("longitude"));
            location = intencion.getStringExtra("location");

        } else {
            Log.i("Maps", "Need you to pass all 3 extras there brother.");
            //finish();
            latitude = 0.0;
            longitude = 0.0;
            location = "Ha habido un error";
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in given location and move the camera to that area
        LatLng myLoc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLoc).title(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
    }
}
