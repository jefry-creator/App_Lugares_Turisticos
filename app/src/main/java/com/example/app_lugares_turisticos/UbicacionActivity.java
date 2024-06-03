package com.example.app_lugares_turisticos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import android.os.Bundle;
import android.widget.Toast;

public class UbicacionActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private double latitud, longitud;
    private LatLng DESTINATION = new LatLng(latitud, longitud); // Coordenadas de destino (por ejemplo, San Francisco)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Intent intent = getIntent();
        latitud = Double.parseDouble(intent.getStringExtra("latitud"));
        longitud = Double.parseDouble(intent.getStringExtra("longitud"));

        Toast.makeText(UbicacionActivity.this, "Latitud: "+ latitud+"\nLongitud" + longitud, Toast.LENGTH_SHORT).show();

        DESTINATION = new LatLng(latitud, longitud);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Establecer el tipo de mapa a normal
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Verificar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        // Habilitar la capa de ubicación en el mapa
        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Mi Ubicación"));

                            // Agregar el marcador en el punto de destino
                            Marker destinoMarker = mMap.addMarker(new MarkerOptions().position(DESTINATION).title("Destino"));

                            // Mostrar la información del marcador automáticamente
                            destinoMarker.showInfoWindow();

                            // Traza la línea entre la ubicación actual y el destino
                            mMap.addPolyline(new PolylineOptions().add(currentLocation, DESTINATION).width(5).color(Color.RED));

                            // Mover la cámara al destino con zoom
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DESTINATION, 15));
                        }
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);

                    fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Mi Ubicación"));
                                mMap.addMarker(new MarkerOptions().position(DESTINATION).title("Destino"));

                                // Traza la línea entre la ubicación actual y el destino
                                mMap.addPolyline(new PolylineOptions().add(currentLocation, DESTINATION).width(5).color(Color.RED));

                                // Mover la cámara al destino con zoom
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DESTINATION, 50));
                            }
                        }
                    });
                }
            }
        }
    }
}

