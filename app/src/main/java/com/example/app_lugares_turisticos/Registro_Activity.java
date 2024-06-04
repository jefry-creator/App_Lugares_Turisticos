package com.example.app_lugares_turisticos;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Registro_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;
    private ImageView imgSitio;

    String url;
    private TextView startTime;
    private TextView endTime;
    EditText nombreSitio, descripcionSitio, etTarifa, etActividades, direccionSitio;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        setUpStartTime();
        setUpEndTime();

        nombreSitio = findViewById(R.id.Name_site);
        descripcionSitio = findViewById(R.id.Description_site);
        etTarifa = findViewById(R.id.tarifa);
        etActividades = findViewById(R.id.Activities);
        direccionSitio = findViewById(R.id.Address_site);
        btnGuardar = findViewById(R.id.SaveBtn);
        imgSitio = findViewById(R.id.image);

        findViewById(R.id.from_time).setOnClickListener(v -> showStartTimePicker());
        findViewById(R.id.to_time).setOnClickListener(v -> showEndTimePicker());

        imgSitio.setOnClickListener(v -> seleccionarImagenDeGaleria());

        btnGuardar.setOnClickListener(v -> GuardarSitio());

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("editar", false)) {
            cargarDatosParaEdicion(intent);
        }
    }

    private void GuardarSitio() {
        String Nombre_Lugar = nombreSitio.getText().toString();
        String Desc_Lugar = descripcionSitio.getText().toString();
        String tarifa = etTarifa.getText().toString();
        String Actividades = etActividades.getText().toString();
        String Direccion = direccionSitio.getText().toString();
        String HoraA = startTime.getText().toString();
        String HoraC = endTime.getText().toString();

        Picasso.get().load(url).into(imgSitio, new Callback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(Exception e) {}
        });

        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Sitios sitios = new Sitios();
                            sitios.setNombreSitio(Nombre_Lugar);
                            sitios.setDescripcionSitio(Desc_Lugar);
                            sitios.setTarifaSitio(tarifa);
                            sitios.setActividadesSitio(Actividades);
                            sitios.setHoraApertura(HoraA);
                            sitios.setHoraCierre(HoraC);
                            sitios.setDireccionSitio(Direccion);
                            sitios.setURLimagen(uri.toString());

                            getCoordinatesFromAddress(Direccion, (latitude, longitude) -> {
                                sitios.setLatitud(latitude);
                                sitios.setLongitud(longitude);
                                GuardadarEnBasedeDatos(sitios);
                            });
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_LONG).show();
                    });
        }
    }

    private void seleccionarImagenDeGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgSitio.setImageURI(imageUri);
        }
    }

    public interface OnCoordinatesObtainedListener {
        void onCoordinatesObtained(double latitude, double longitude);
    }

    private void getCoordinatesFromAddress(String address, OnCoordinatesObtainedListener listener) {
        Geocoder geocoder = new Geocoder(this, new Locale("es", "SV"));
        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    double latitude = addressResult.getLatitude();
                    double longitude = addressResult.getLongitude();
                    runOnUiThread(() -> listener.onCoordinatesObtained(latitude, longitude));
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_LONG).show();
                        listener.onCoordinatesObtained(0.0, 0.0);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error al obtener las coordenadas", Toast.LENGTH_LONG).show();
                    listener.onCoordinatesObtained(0.0, 0.0);
                });
            }
        }).start();
    }

    void GuardadarEnBasedeDatos(Sitios sitios) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sitios");

        String key = myRef.push().getKey();
        if (key != null) {
            sitios.setKey(key);
            myRef.child(key).setValue(sitios).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Sitio guardado exitosamente", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al guardar el sitio", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Error al generar la clave para el sitio", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpStartTime() {
        startTime = findViewById(R.id.from_time);
        startTime.setText(TimeUtils.toTimeText(LocalTime.now()));
        startTime.setOnClickListener(v -> showStartTimePicker());
    }

    private void showStartTimePicker() {
        showDialog((view, hourOfDay, minute) -> {
            LocalTime currentTime = LocalTime.of(hourOfDay, minute);
            TimeUtils.setTime(startTime, currentTime);
        });
    }

    private void setUpEndTime() {
        endTime = findViewById(R.id.to_time);
        endTime.setText(TimeUtils.toTimeText(LocalTime.now().plusHours(2)));
        endTime.setOnClickListener(v -> showEndTimePicker());
    }

    private void showEndTimePicker() {
        showDialog((view, hourOfDay, minute) -> {
            LocalTime currentTime = LocalTime.of(hourOfDay, minute);
            TimeUtils.setTime(endTime, currentTime);
        });
    }

    private void showDialog(TimePickerDialog.OnTimeSetListener observer) {
        TimePicker timePicker = TimePicker.newInstance(10, 30, observer);
        timePicker.show(getSupportFragmentManager(), "time-picker");
    }

    private void cargarDatosParaEdicion(Intent intent) {
        String key = intent.getStringExtra("id");
        String nombre = intent.getStringExtra("nombre");
        String descripcion = intent.getStringExtra("descripcion");
        String latitud = intent.getStringExtra("latitud");
        String longitud = intent.getStringExtra("longitud");
        url = intent.getStringExtra("url");
        String tarifa = intent.getStringExtra("tarifa");
        String actividades = intent.getStringExtra("actividades");
        String direccion = intent.getStringExtra("direccion");
        String horaApertura = intent.getStringExtra("horaApertura");
        String horaCierre = intent.getStringExtra("horaCierre");

        nombreSitio.setText(nombre);
        descripcionSitio.setText(descripcion);
        etTarifa.setText(tarifa);
        etActividades.setText(actividades);
        direccionSitio.setText(direccion);
        startTime.setText(horaApertura);
        endTime.setText(horaCierre);

        Picasso.get().load(url).into(imgSitio);

        btnGuardar.setOnClickListener(v -> actualizarSitio(key));
    }

    private void actualizarSitio(String key) {
        String Nombre_Lugar = nombreSitio.getText().toString();
        String Desc_Lugar = descripcionSitio.getText().toString();
        String tarifa = etTarifa.getText().toString();
        String Actividades = etActividades.getText().toString();
        String Direccion = direccionSitio.getText().toString();
        String HoraA = startTime.getText().toString();
        String HoraC = endTime.getText().toString();

        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            actualizarDatosEnBaseDeDatos(key, Nombre_Lugar, Desc_Lugar, tarifa, Actividades, Direccion, HoraA, HoraC, uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_LONG).show();
                    });
        } else {
            actualizarDatosEnBaseDeDatos(key, Nombre_Lugar, Desc_Lugar, tarifa, Actividades, Direccion, HoraA, HoraC, url);
        }
    }

    private void actualizarDatosEnBaseDeDatos(String key, String nombre, String descripcion, String tarifa, String actividades, String direccion, String horaA, String horaC, String imageUrl) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("sitios").child(key);

        getCoordinatesFromAddress(direccion, (latitude, longitude) -> {
            Sitios sitio = new Sitios();
            sitio.setKey(key);
            sitio.setNombreSitio(nombre);
            sitio.setDescripcionSitio(descripcion);
            sitio.setTarifaSitio(tarifa);
            sitio.setActividadesSitio(actividades);
            sitio.setHoraApertura(horaA);
            sitio.setHoraCierre(horaC);
            sitio.setDireccionSitio(direccion);
            sitio.setURLimagen(imageUrl);
            sitio.setLatitud(latitude);
            sitio.setLongitud(longitude);

            myRef.setValue(sitio).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Sitio actualizado exitosamente", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al actualizar el sitio", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}