package com.example.quanlycaphe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class homee extends AppCompatActivity {
    ImageView imgbtn_order, imgbtn_qly, imgbtn_dx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homee);

        imgbtn_order = findViewById(R.id.imgbtn_ordernv);
        imgbtn_qly = findViewById(R.id.imgbtn_qly);
        imgbtn_dx = findViewById(R.id.imgbtn_dxnv);

        imgbtn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(homee.this, order.class);
                startActivities(new Intent[]{myIntent});
            }
        });

        imgbtn_qly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(homee.this, quanly.class);
                startActivities(new Intent[]{b});
            }
        });

        imgbtn_dx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homee.this, MainActivity.class);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(homee.this);
                Dialog.setTitle("Thông Báo");
                Dialog.setMessage("Bạn có muốn đăng xuất không?");
                Dialog.setNegativeButton("CÓ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        // Bắt đầu MainActivity
                        startActivity(intent);

                    }
                });
                Dialog.setPositiveButton("KHÔNG", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(homee.this, "Bạn đã chọn KHÔNG", Toast.LENGTH_SHORT).show();
                    }
                });
                Dialog.setNeutralButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(homee.this, "Bạn đã chọn Thoát", Toast.LENGTH_SHORT).show();
                    }
                });
                Dialog.create();
                Dialog.show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}