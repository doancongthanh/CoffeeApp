package com.example.quanlycaphe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class dk extends AppCompatActivity {
    EditText edit_userr, edit_passs, edit_passw;
    Button btn_dk;
    ImageButton imgbtn_thoatdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dk);

        edit_userr = findViewById(R.id.edit_user);
        edit_passs = findViewById(R.id.edit_pass);
        edit_passw = findViewById(R.id.edit_passw);
        btn_dk = findViewById(R.id.btn_dk);
        imgbtn_thoatdk = findViewById(R.id.imgbtn_thoatdk);

        imgbtn_thoatdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_passw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    handleSignup();
                    return true; // Đã xử lý sự kiện
                }
                return false;
            }
        });

        btn_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignup();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    private void handleSignup() {
        String edit_userr_ = edit_userr.getText().toString();
        String edit_passs_ = edit_passs.getText().toString();
        String edit_passw_ = edit_passw.getText().toString();

        if (TextUtils.isEmpty(edit_userr_) || TextUtils.isEmpty(edit_passs_) || TextUtils.isEmpty(edit_passw_)) {
            Toast.makeText(dk.this, "Vui lòng không để trống dữ liệu!!!", Toast.LENGTH_SHORT).show();
        } else if (!edit_passs_.equals(edit_passw_)) {
            Toast.makeText(dk.this, "Vui lòng nhập khớp mật khẩu và nhập lại mật khẩu!!!", Toast.LENGTH_SHORT).show();
        } else {
            signupUser(edit_userr_, edit_passs_);
        }
    }
    private void signupUser(String edit_userr, String edit_passs){
        try {
            String edit_userr_ = edit_userr;
            String edit_passs_ = edit_passs;

            SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tbluser (id INTEGER PRIMARY KEY, username VARCHAR, password VARCHAR, score VARCHAR)");

            String sql = "insert into tbluser(username,password,score)values(?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, edit_userr_);
            statement.bindString(2, edit_passs_);
            statement.bindString(3, "0");
            statement.execute();
            Intent intent = new Intent(dk.this, MainActivity.class);
            AlertDialog.Builder Dialog = new AlertDialog.Builder(dk.this);
            Dialog.setTitle("ĐANG KÝ THÀNH CÔNG");
            Dialog.setNegativeButton("YES", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // Bắt đầu MainActivity
                startActivity(intent);
            }
        });
            Dialog.create();
            Dialog.show();
        } catch (Exception e){
            Toast.makeText(dk.this, "Đăng ký tài khoản không thành công", Toast.LENGTH_SHORT).show();
        }
    }
}