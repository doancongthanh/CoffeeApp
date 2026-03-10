package com.example.quanlycaphe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText edit_user, edit_pass;
    Button btn_dn;TextView txt_dk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edit_user = findViewById(R.id.edit_user);
        edit_pass = findViewById(R.id.edit_pass);
        btn_dn = findViewById(R.id.btn_dk);

        btn_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });

        edit_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Gọi phương thức handleLogin khi nhấn Enter
                    handleLogin();
                    return true; // Tuyên bố rằng sự kiện đã được xử lý
                }
                return false; // Không xử lý sự kiện
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    // Phương thức để xử lý logic đăng nhập
    private void handleLogin() {
        // Lấy dữ liệu người dùng nhập
        String edit_user_ = edit_user.getText().toString();
        String edit_passs_ = edit_pass.getText().toString();

        // Kiểm tra nếu các trường nhập liệu để trống
        if (TextUtils.isEmpty(edit_user_) || TextUtils.isEmpty(edit_passs_)) {
            Toast.makeText(MainActivity.this, "Vui lòng không để trống dữ liệu", Toast.LENGTH_SHORT).show();
        } else if (edit_user_.equals("admin") && edit_passs_.equals("211219")) {
            // Nếu thông tin đăng nhập đúng, mở trang chính của admin
            Intent i = new Intent(MainActivity.this, homee.class);
            startActivity(i);
            Toast.makeText(MainActivity.this, "Đăng Nhập AdminThành Công", Toast.LENGTH_SHORT).show();
        } else {
            // Hiển thị lỗi nếu đăng nhập không thành côngan
            signinUser(edit_user_, edit_passs_);
        }
    }
    private void signinUser(String edit_userr, String edit_passs) {


        SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
        final Cursor c = db.rawQuery("select * from tbluser where username = ? and password = ?", new String[]{edit_userr, edit_passs});

        if (c.getCount() > 0) {
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            //Nếu c truy xuất ra được dữ liệu trùng khớp thì Đăng nhập vào HomeActivity
            Intent i = new Intent(getApplicationContext(), home_nv.class);
            i.putExtra("stringuserr", edit_userr);
            startActivity(i);
        } else {
            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        }

    }

}