package com.example.quanlycaphe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class taikhoan extends AppCompatActivity {
    ImageButton imgbtn_nv, imgbtn_thoattk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_taikhoan);
        imgbtn_nv = findViewById(R.id.imgbtn_nv);
        imgbtn_thoattk = findViewById(R.id.imgbtn_thoattk);

        imgbtn_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dk = new Intent(taikhoan  .this, dk.class);
                startActivities(new Intent[]{dk});
            }
        });
        imgbtn_thoattk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}