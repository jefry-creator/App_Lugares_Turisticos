package com.example.app_lugares_turisticos;

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

public class DiscoverFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;
    private ImageView imgSitio;

    String url;
    private TextView startTime;
    private TextView endTime;
    EditText nombreSitio, descripcionSitio, etTarifa, etActividades, direccionSitio;
    Button btnGuardar;

    private DatabaseReference mPostRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        setUpStartTime(view);
        setUpEndTime(view);

        nombreSitio = view.findViewById(R.id.Name_site);
        descripcionSitio = view.findViewById(R.id.Description_site);
        etTarifa = view.findViewById(R.id.tarifa);
        etActividades = view.findViewById(R.id.Activities);
        direccionSitio = view.findViewById(R.id.Address_site);
        btnGuardar = view.findViewById(R.id.SaveBtn);
        imgSitio = view.findViewById(R.id.image);

        view.findViewById(R.id.from_time).setOnClickListener(v -> showStartTimePicker());
        view.findViewById(R.id.to_time).setOnClickListener(v -> showEndTimePicker());



        imgSitio.setOnClickListener(v -> seleccionarImagenDeGaleria());

        btnGuardar.setOnClickListener(v -> GuardarSitio());
        return view;
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
            public void onSuccess() { }

            @Override
            public void onError(Exception e) { }
        });

        if (imageUri != null) {

            // Crear una referencia en Firebase Storage para la imagen
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());

            // Subir la imagen a Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Si la imagen se sube correctamente, obtener su URL
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Sitios sitios = new Sitios();
                            sitios.setNombreSitio(Nombre_Lugar);
                            sitios.setDescripcionSitio(Desc_Lugar);
                            sitios.setTarifaSitio("$"+tarifa);
                            sitios.setActividadesSitio(Actividades);
                            sitios.setHoraApertura(HoraA);
                            sitios.setHoraCierre(HoraC);
                            sitios.setURLimagen(uri.toString());

                            getCoordinatesFromAddress(Direccion, (latitude, longitude) -> {
                                sitios.setLatitud(latitude);
                                sitios.setLongitud(longitude);
                                GuardadarEnBasedeDatos(sitios);
                            });
                        });
                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(getContext(), "Seleccione una imagen", Toast.LENGTH_LONG).show();
                    });
        } else {


        }
    }

    private void seleccionarImagenDeGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgSitio.setImageURI(imageUri);
        }
    }

    public interface OnCoordinatesObtainedListener {
        void onCoordinatesObtained(double latitude, double longitude);
    }
    private void getCoordinatesFromAddress(String address, OnCoordinatesObtainedListener listener) {
        Geocoder geocoder = new Geocoder(getContext(), new Locale("es", "SV")); // Configuración para El Salvador
        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    double latitude = addressResult.getLatitude();
                    double longitude = addressResult.getLongitude();
                    listener.onCoordinatesObtained(latitude, longitude);
                } else {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Dirección no encontrada", Toast.LENGTH_LONG).show();
                        listener.onCoordinatesObtained(0.0, 0.0);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error al obtener las coordenadas", Toast.LENGTH_LONG).show();
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
            sitios.setKey(key); // Asigna la clave al objeto Sitios
            myRef.child(key).setValue(sitios).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Sitio guardado exitosamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Error al guardar el sitio", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Error al generar la clave para el sitio", Toast.LENGTH_LONG).show();
        }
    }
    private void setUpStartTime(View view) {
        startTime = view.findViewById(R.id.from_time);
        startTime.setText(TimeUtils.toTimeText(LocalTime.now()));
        startTime.setOnClickListener(v -> showStartTimePicker());
    }

    private void showStartTimePicker() {
        showDialog((view, hourOfDay, minute) -> {
            LocalTime currentTime = LocalTime.of(hourOfDay, minute);
            if (isValidStartTime(currentTime)) {
                TimeUtils.setTime(startTime, currentTime);
            } else {
                Toast.makeText(getActivity(), "Tiempo inicial invalido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpEndTime(View view) {
        endTime = view.findViewById(R.id.to_time);
        endTime.setText(TimeUtils.toTimeText(LocalTime.now().plusHours(2)));
        endTime.setOnClickListener(v -> showEndTimePicker());
    }

    private void showEndTimePicker() {
        showDialog((view, hourOfDay, minute) -> {
            LocalTime currentTime = LocalTime.of(hourOfDay, minute);
            if (isValidEndTime(currentTime)) {
                TimeUtils.setTime(endTime, currentTime);
            } else {
                Toast.makeText(getActivity(), "Tiempo final invalido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(TimePickerDialog.OnTimeSetListener observer) {
        FragmentManager fragmentManager = getChildFragmentManager();
        TimePicker timePicker = TimePicker.newInstance(10, 30, observer);
        timePicker.show(fragmentManager, "time-picker");
    }

    private boolean isValidStartTime(LocalTime time) {
        return time.isBefore(TimeUtils.getTime(endTime));
    }

    private boolean isValidEndTime(LocalTime time) {
        return time.isAfter(TimeUtils.getTime(startTime));
    }

}

