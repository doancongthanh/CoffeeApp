package com.example.quanlycaphe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class order extends AppCompatActivity {
    ImageButton imgbtn_back, imgbtn_1, imgbtn_2, imgbtn_3,  imgbtn_4, imgbtn_5, imgbtn_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);


        imgbtn_1 = findViewById(R.id.imgbtn_1);
        imgbtn_2 = findViewById(R.id.imgbtn_2);
        imgbtn_3 = findViewById(R.id.imgbtn_3);
        imgbtn_4 = findViewById(R.id.imgbtn_4);
        imgbtn_5 = findViewById(R.id.imgbtn_5);
        imgbtn_6 = findViewById(R.id.imgbtn_6);
        imgbtn_back = findViewById(R.id.imgbtn_dxnv);

        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgbtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ban1 = new Intent(order.this, com.example.quanlycaphe.ban.ban1.class);
                startActivities(new Intent[]{ban1});
            }
        });

        imgbtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ban2 = new Intent(order.this, com.example.quanlycaphe.ban.ban2.class);
                startActivities(new Intent[]{ban2});
            }
        });

        imgbtn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ban6 = new Intent(order.this, com.example.quanlycaphe.ban.ban6.class);
                startActivities(new Intent[]{ban6});
            }
        });

        imgbtn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ban3 = new Intent(order.this, com.example.quanlycaphe.ban.ban3.class);
                startActivities(new Intent[]{ban3});
            }
        });

        imgbtn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ban4 = new Intent(order.this, com.example.quanlycaphe.ban.ban4.class);
                startActivities(new Intent[]{ban4});
            }
        });

        imgbtn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ban5 = new Intent(order.this, com.example.quanlycaphe.ban.ban5.class);
                startActivities(new Intent[]{ban5});
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}