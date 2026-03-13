package com.example.quanlycaphe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class quanly extends AppCompatActivity {
    ImageButton imgbtn_backk, imgbtn_menu, imgbtn_tk, imgbtn_taikhoan;
    ArrayList<String> doanhThuList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanly);

        imgbtn_menu = findViewById(R.id.imgbtn_menu);
        imgbtn_tk = findViewById(R.id.imgbtn_tk);
        imgbtn_backk = findViewById(R.id.imgbtn_backk);
        imgbtn_taikhoan = findViewById(R.id.imgbtn_taikhoan);

        imgbtn_backk.setOnClickListener(view -> finish());

        imgbtn_menu.setOnClickListener(view -> {
            Intent menu = new Intent(quanly.this, menuu.class);
            startActivity(menu);
        });

        imgbtn_tk.setOnClickListener(view -> {
            Intent tk = new Intent(quanly.this, thongke.class);
            startActivity(tk);
        });

        imgbtn_taikhoan.setOnClickListener(view -> {
            Intent tkk = new Intent(quanly.this, taikhoan.class);
            startActivity(tkk);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}