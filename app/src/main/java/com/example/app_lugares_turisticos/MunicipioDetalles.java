package com.example.app_lugares_turisticos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app_lugares_turisticos.R;

import com.google.android.material.imageview.ShapeableImageView;

public class MunicipioDetalles extends AppCompatActivity {

    ShapeableImageView mainimage, attr1img, attr2img, food1img, food2img;
    TextView city, description, duration, timetovisit, attr1name, attr1des, attr2name, attr2des, food1name, food1des, food2name, food2des;
    Button locationbtn;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipio_detalles);

        mainimage = findViewById(R.id.citydetails_image);
        attr1img = findViewById(R.id.citydetails_place1image);
        attr2img = findViewById(R.id.citydetails_place2image);
        food1img = findViewById(R.id.citydetails_food1image);
        food2img = findViewById(R.id.citydetails_food2image);
        city = findViewById(R.id.citydetails_name);
        description = findViewById(R.id.citydetails_description);
        duration = findViewById(R.id.citydetails_tripduration);
        timetovisit = findViewById(R.id.citydetails_besttime);
        attr1name = findViewById(R.id.citydetails_place1name);
        attr2name = findViewById(R.id.citydetails_place2name);
        food1name = findViewById(R.id.citydetails_food1name);
        food2name = findViewById(R.id.citydetails_food2name);
        attr1des = findViewById(R.id.citydetails_place1description);
        attr2des = findViewById(R.id.citydetails_place2description);
        food1des = findViewById(R.id.citydetails_food1description);
        food2des = findViewById(R.id.citydetails_food2description);
        locationbtn = findViewById(R.id.citydetails_getlocationbtn);

        String city_str = getIntent().getStringExtra("putextra_city");

        if (city_str.equals("Agra")) {
            city.setText("Agra, Uttar Pradesh");
            attr1name.setText("Taj Mahal");
            attr2name.setText("Agra Fort");
            attr1des.setText("Taj Mahal is a historical building");
            attr2des.setText("Agra Fort is a historical building");
            attr1img.setImageResource(R.drawable.agra);
            attr2img.setImageResource(R.drawable.agra_agrafort);
            food1name.setText("Agra Petha");
            food2name.setText("Bhalla");
            food1des.setText("A very delicious sweet");
            food2des.setText("A popular street food");
            food1img.setImageResource(R.drawable.agra_food_petha);
            food2img.setImageResource(R.drawable.agra_food_bhalla);
            description.setText("A very popular city among tourists. Famous attractions of agra are Taj Mahal, Agra Fort etc.");
            location = "https://www.google.co.in/maps/place/Agra,+Uttar+Pradesh/@27.176369,77.9386518,11z/data=!4m13!1m7!3m6!1s0x39740d857c2f41d9:0x784aef38a9523b42!2sAgra,+Uttar+Pradesh!3b1!8m2!3d27.1766701!4d78.0080745!3m4!1s0x39740d857c2f41d9:0x784aef38a9523b42!8m2!3d27.1766701!4d78.0080745";
            timetovisit.setText("October to March");
            duration.setText("2 to 3 Days");
            mainimage.setImageResource(R.drawable.agra);
        }
        if (city_str.equals("Amritsar")) {
            city.setText("Amritsar, Punjab");
            attr1name.setText("Golden Temple");
            attr2name.setText("Jallianwala Bagh");
            attr1des.setText("Shri Harmandir Sahib, a very popular temple.");
            attr2des.setText("Jallianwala Bagh is a historic garden.");
            attr1img.setImageResource(R.drawable.amritsar_golden_temple);
            attr2img.setImageResource(R.drawable.amritsar_jallianwala_bagh);
            food1name.setText("Aloo Paratha");
            food2name.setText("Sarson ka Saag");
            food1des.setText("Tasty dish with lot of butter.");
            food2des.setText("A very popular dish mainly eaten with makke ki roti");
            food1img.setImageResource(R.drawable.amritsar_food_paratha);
            food2img.setImageResource(R.drawable.amritsar_food_sarsonkasag);
            description.setText("A very popular city among tourists. Famous attractions of amritsar are Golden Temple, Wagah Border etc.");
            location = "https://www.google.co.in/maps/place/Amritsar,+Punjab/@31.633525,74.8000794,12z/data=!3m1!4b1!4m5!3m4!1s0x391964aa569e7355:0xeea2605bee84ef7d!8m2!3d31.6339793!4d74.8722642";
            timetovisit.setText("November to March");
            duration.setText("2 Days");
            mainimage.setImageResource(R.drawable.amritsar);
        }

        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(location)));
            }
        });



    }
}