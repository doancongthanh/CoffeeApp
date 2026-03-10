package com.example.quanlycaphe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class home_nv extends AppCompatActivity {
    ImageView imgbtn_ordernv, imgbtn_dxnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_nv);

        imgbtn_ordernv = findViewById(R.id.imgbtn_ordernv);
        imgbtn_dxnv = findViewById(R.id.imgbtn_dxnv);

        imgbtn_dxnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_nv = new Intent(home_nv.this, MainActivity.class);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(home_nv.this);
                Dialog.setTitle("Thông Báo");
                Dialog.setMessage("Bạn có muốn đăng xuất không?");
                Dialog.setNegativeButton("CÓ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent_nv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        // Bắt đầu MainActivity
                        startActivity(intent_nv);

                    }
                });
                Dialog.setPositiveButton("KHÔNG", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(home_nv.this, "Bạn đã chọn KHÔNG", Toast.LENGTH_SHORT).show();
                    }
                });
                Dialog.setNeutralButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(home_nv.this, "Bạn đã chọn Thoát", Toast.LENGTH_SHORT).show();
                    }
                });
                Dialog.create();
                Dialog.show();
            }
        });

        imgbtn_ordernv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent od = new Intent(home_nv.this, order.class);
                startActivities(new Intent[]{od});
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}