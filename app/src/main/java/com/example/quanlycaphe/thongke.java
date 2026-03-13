package com.example.quanlycaphe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class thongke extends AppCompatActivity {
    ListView lv_doanhthu, lv_hoadon;
    TextView txt_dt, txt_hd;
    ImageButton imgbtn_backtk;;
    ArrayAdapter<String> adapter;
    ArrayList<String> statList = new ArrayList<>();
    ArrayList<String> invoiceList = new ArrayList<>(); // Danh sách hiển thị số lượng hóa đơn
    HashMap<String, Float> revenueMap = new HashMap<>();
    HashMap<String, Integer> invoiceCountMap = new HashMap<>(); // Map lưu số lượng hóa đơn theo ngày


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thongke);

        lv_hoadon = findViewById(R.id.lv_hd);
        lv_doanhthu = findViewById(R.id.lv_doanhthu);
        txt_dt = findViewById(R.id.txt_dt);
        txt_hd = findViewById(R.id.txt_hd);
        imgbtn_backtk = findViewById(R.id.imgbtn_backtk);

        imgbtn_backtk.setOnClickListener(view -> finish());
        loadPrintedInvoicesStatistics();
    }
    private void loadPrintedInvoicesStatistics() {
        SharedPreferences sharedPreferences = getSharedPreferences("printedInvoices", MODE_PRIVATE);

        // Lấy tất cả các key hóa đơn đã in từ SharedPreferences
        Map<String, ?> allInvoices = sharedPreferences.getAll();
        ArrayList<String> invoiceList = new ArrayList<>();
        float totalRevenue = 0f;
        int invoiceCount = 0;

        for (Map.Entry<String, ?> entry : allInvoices.entrySet()) {
            String key = entry.getKey();
            if (key.contains("_totalPrice")) {
                String dateTime = sharedPreferences.getString(key.replace("_totalPrice", "_dateTime"), "N/A");
                float totalPrice = sharedPreferences.getFloat(key, 0f);

                // Tính tổng doanh thu
                totalRevenue += totalPrice;
                invoiceCount++;

                // Lưu thông tin hóa đơn vào danh sách để hiển thị
                String invoiceDetail = dateTime + " | Tổng tiền: " + formatCurrencyVND(totalPrice);
                invoiceList.add(invoiceDetail);
            }
        }

        // Hiển thị tổng doanh thu và số lượng hóa đơn
        TextView txtTotalRevenue = findViewById(R.id.txt_dt);
        txtTotalRevenue.setText("Tổng doanh thu: " + formatCurrencyVND(totalRevenue));

        // Hiển thị danh sách hóa đơn đã in
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, invoiceList);
        ListView lvInvoices = findViewById(R.id.lv_doanhthu);
        lvInvoices.setAdapter(adapter);
    }

    private String formatCurrencyVND(double value) {
        return String.format("%,.0f VND", value); // Định dạng tiền tệ bằng VND
    }

}