package com.example.quanlycaphe;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.text.NumberFormat; // Thêm dòng này để sử dụng NumberFormat
import java.util.Locale; // Thêm dòng này để sử dụng Locale


public class menuu extends AppCompatActivity {
    EditText edit_tennuoc, edit_gia;
    Button btn_them;
    ImageButton imgbtn_backmenu;
    ListView lv_menu;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menuu);

        edit_tennuoc = findViewById(R.id.edit_tennuoc);
        edit_gia = findViewById(R.id.edit_gia);
        btn_them = findViewById(R.id.btn_them);
        imgbtn_backmenu = findViewById(R.id.imgbtn_backmenu);
        lv_menu = findViewById(R.id.lv_menu);

        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv_menu.setAdapter(myadapter);

        mydatabase = openOrCreateDatabase("menu.db",MODE_PRIVATE,null);
        try {
            String sql = "CREATE TABLE tblmenu(tennuoc TEXT primary key, gia TEXT)";
            mydatabase.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.e("Error","Table đã tồn tại");
        }
        loadData();

        imgbtn_backmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tennuoc = edit_tennuoc.getText().toString();
                String giaString = edit_gia.getText().toString();
                int gia;
                try {
                    gia = Integer.parseInt(giaString); // Chuyển đổi giá thành số nguyên
                } catch (NumberFormatException e) {
                    Toast.makeText(menuu.this, "Giá không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String giaFormatted = gia * 1000 + " VND"; // Nhân với 1000 và thêm " VND"
                ContentValues myvalue = new ContentValues();
                myvalue.put("tennuoc", tennuoc);
                myvalue.put("gia", giaFormatted); // Lưu giá đã định dạng vào cơ sở dữ liệu

                String msg;
                if (mydatabase.insert("tblmenu", null, myvalue) == -1) {
                    msg = "Fail to Insert Record!";
                } else {
                    msg = "Insert record Successfully";
                    mylist.add(tennuoc + " - " + giaFormatted); // Thêm dữ liệu đã định dạng vào ArrayList
                    myadapter.notifyDataSetChanged(); // Cập nhật ListView
                    edit_tennuoc.setText("");
                    edit_gia.setText("");
                    saveDataToSharedPreferences(mylist);
                }
                Toast.makeText(menuu.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private long lastClickTime = 0;
            private int lastClickedPosition = -1;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long clickTime = System.currentTimeMillis();

                // Kiểm tra nếu nhấp đúp vào cùng một vị trí trong vòng 200ms
                if (lastClickedPosition == position && (clickTime - lastClickTime < 1000)) {
                    // Lấy tên nước từ danh sách
                    String tennuoc = mylist.get(position).split(" - ")[0];

                    // Xóa khỏi cơ sở dữ liệu
                    int deletedRows = mydatabase.delete("tblmenu", "tennuoc = ?", new String[]{tennuoc});
                    if (deletedRows > 0) {
                        // Xóa khỏi ArrayList và cập nhật ListView
                        mylist.remove(position);
                        myadapter.notifyDataSetChanged();
                        Toast.makeText(menuu.this, "Đã xóa " + tennuoc, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(menuu.this, "Không thể xóa " + tennuoc, Toast.LENGTH_SHORT).show();
                    }

                    // Reset lại bộ đếm
                    lastClickTime = 0;
                    lastClickedPosition = -1;
                } else {
                    // Lưu lại thời gian và vị trí của lần nhấp đầu tiên
                    lastClickTime = clickTime;
                    lastClickedPosition = position;
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void loadData() {
        Cursor cursor = mydatabase.rawQuery("SELECT * FROM tblmenu", null);
        if (cursor.moveToFirst()) {
            do {
                String tennuoc = cursor.getString(0);
                String gia = cursor.getString(1);
                mylist.add(tennuoc + " - " + gia);
            } while (cursor.moveToNext());
        }
        cursor.close();
        myadapter.notifyDataSetChanged(); // Cập nhật ListView sau khi tải dữ liệu
    }
    private void saveDataToSharedPreferences(ArrayList<String> list) {
        SharedPreferences sharedPreferences = getSharedPreferences("menuData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>(list);  // Chuyển đổi ArrayList thành Set để lưu trữ
        editor.putStringSet("menuList", set);
        editor.apply();
    }
    private String formatCurrencyVND(double value) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(value);
    }

}
