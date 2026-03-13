package com.example.quanlycaphe.ban;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycaphe.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ban2 extends AppCompatActivity {
    ListView lv2, lv22;
    TextView txt_tongtien2;
    Button btn_tt2;
    ImageButton imgbtn_back2;
    ArrayList<String> menuDataList; // Danh sách chứa dữ liệu menu ban đầu
    ArrayList<String> selectedItemsList; // Danh sách chứa các món đã chọn
    ArrayAdapter<String> adapter1, adapter2;
    double totalPrice = 0.0; // Tổng giá tiền
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Thời gian tính cho bấm đúp (300ms)
    private long lastClickTime = 0; // Biến lưu thời gian của lần nhấn cuối

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ban6);

        txt_tongtien2 = findViewById(R.id.txt_tongtien6);
        lv2 = findViewById(R.id.lv6);
        lv22 = findViewById(R.id.lv66);
        btn_tt2 = findViewById(R.id.btn_tt6);
        imgbtn_back2 = findViewById(R.id.imgbtn_back6);

        imgbtn_back2.setOnClickListener(view -> finish());

        menuDataList = loadDataFromSharedPreferences();

        Log.d("ban2", "Menu Data List: " + menuDataList);  // Kiểm tra xem menu có được tải đúng không

        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuDataList);
        lv2.setAdapter(adapter1); // Gán adapter cho lv6 để hiển thị menu

        selectedItemsList = new ArrayList<>(); // Khởi tạo danh sách các món đã chọn
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedItemsList);
        lv22.setAdapter(adapter2); // Gán adapter cho lv66 để hiển thị các món đã chọn

        HashMap<String, Integer> itemCounts = new HashMap<>();

        // Xử lý sự kiện bấm đúp trên lv6 để thêm vào lv66 và cập nhật tổng giá
        lv2.setOnItemClickListener((parent, view, position, id) -> {
            long clickTime = System.currentTimeMillis(); // Lấy thời gian hiện tại

            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) { // Kiểm tra khoảng thời gian để nhận diện bấm đúp
                // Lấy món đã chọn
                String item = menuDataList.get(position);

                // Cập nhật số lượng món trong HashMap
                int count = itemCounts.getOrDefault(item, 0) + 1;
                itemCounts.put(item, count);

                // Cập nhật danh sách hiển thị
                boolean isItemInList = false;
                for (int i = 0; i < selectedItemsList.size(); i++) {
                    if (selectedItemsList.get(i).contains(item)) {
                        selectedItemsList.set(i, count + " x " + item);
                        isItemInList = true;
                        break;
                    }
                }
                if (!isItemInList) {
                    selectedItemsList.add(count + " x " + item);
                }

                adapter2.notifyDataSetChanged(); // Cập nhật giao diện lv66

                // Phân tích và tính giá từ chuỗi (giả sử định dạng là "Tên món - Giá")
                String[] parts = item.split(" - ");
                if (parts.length == 2) { // Đảm bảo chuỗi có đủ phần
                    try {
                        int price = Integer.parseInt(parts[1].replace(" VND", "").trim()); // Lấy giá
                        totalPrice += price; // Cộng giá vào tổng giá tiền
                        txt_tongtien2.setText(formatCurrencyVND(totalPrice)); // Hiển thị tổng giá tiền
                    } catch (NumberFormatException e) {
                        Toast.makeText(ban2.this, "Lỗi định dạng giá tiền", Toast.LENGTH_SHORT).show();
                    }
                }

                lastClickTime = 0; // Đặt lại lastClickTime sau khi xử lý bấm đúp
            } else {
                lastClickTime = clickTime; // Lưu thời gian của lần bấm đầu tiên
            }
        });

        lv22.setOnItemClickListener((adapterView, view, position, id) -> {
            long clickTime = System.currentTimeMillis(); // Lấy thời gian hiện tại

            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) { // Kiểm tra bấm đúp
                // Lấy món cần xóa
                String selectedItem = selectedItemsList.get(position);

                // Tách số lượng và tên món (giả sử định dạng là "Số lượng x Tên món")
                String[] parts = selectedItem.split(" x ");
                if (parts.length == 2) {
                    try {
                        int count = Integer.parseInt(parts[0]); // Lấy số lượng
                        String itemName = parts[1]; // Lấy tên món

                        // Giảm số lượng trong HashMap
                        int currentCount = itemCounts.getOrDefault(itemName, 0);
                        if (currentCount > 1) {
                            itemCounts.put(itemName, currentCount - 1);
                            selectedItemsList.set(position, (currentCount - 1) + " x " + itemName);
                        } else {
                            // Nếu chỉ còn 1, xóa món khỏi danh sách
                            itemCounts.remove(itemName);
                            selectedItemsList.remove(position);
                        }

                        // Tính lại giá tiền
                        String[] priceParts = itemName.split(" - ");
                        if (priceParts.length == 2) {
                            int price = Integer.parseInt(priceParts[1].replace(" VND", "").trim());
                            totalPrice -= price; // Giảm giá từ tổng tiền
                            txt_tongtien2.setText(formatCurrencyVND(totalPrice));
                        }

                        adapter2.notifyDataSetChanged(); // Cập nhật giao diện lv66
                    } catch (NumberFormatException e) {
                        Toast.makeText(ban2.this, "Lỗi định dạng số lượng hoặc giá tiền", Toast.LENGTH_SHORT).show();
                    }
                }

                lastClickTime = 0; // Đặt lại lastClickTime sau khi xử lý bấm đúp
            } else {
                lastClickTime = clickTime; // Lưu thời gian của lần bấm đầu tiên
            }
        });

        btn_tt2.setOnClickListener(view -> {
            if (!selectedItemsList.isEmpty()) {
                // Lấy ngày hiện tại dưới dạng chuỗi "yyyyMMdd"
                String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                Log.d("ban2", "Current Date: " + currentDate); // Kiểm tra ngày hiện tại

                // Tạo key cho doanh thu của ngày hiện tại
                String revenueKey = "doanhThu_" + currentDate;
                String invoiceKey = "soHoaDon_" + currentDate; // Key để lưu số lượng hóa đơn

                // Lưu các món đã thanh toán vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Cập nhật danh sách các món đã thanh toán trong ngày
                Set<String> paidItemsSet = sharedPreferences.getStringSet("paidItems_" + currentDate, new HashSet<>());
                paidItemsSet.addAll(selectedItemsList); // Thêm các món đã thanh toán vào danh sách
                editor.putStringSet("paidItems_" + currentDate, paidItemsSet);

                // Lưu số lượng hóa đơn
                int invoiceCount = sharedPreferences.getInt(invoiceKey, 0) + 1;
                editor.putInt(invoiceKey, invoiceCount);
                editor.putString(revenueKey, formatCurrencyVND(totalPrice)); // Lưu tổng doanh thu cho ngày hiện tại
                editor.apply();

                // Chuyển sang Activity hóa đơn và truyền dữ liệu
                // Hiển thị thông báo thành công với AlertDialog
                new AlertDialog.Builder(ban2.this)
                        .setTitle("Thành Công")
                        .setMessage("Bạn đã thanh toán thành công!")
                        .setPositiveButton("OK", (dialog, which) -> {
                            Log.d("ban5", "Tổng tiền trước khi truyền: " + totalPrice);

                            Intent intent = new Intent(ban2.this, hoadon.class);
                            intent.putExtra("table_number", "5");  // Truyền số bàn
                            intent.putExtra("items", new ArrayList<>(selectedItemsList));  // Truyền danh sách món ăn
                            intent.putExtra("totalPrice", totalPrice);  // Truyền tổng tiền

                            // Truyền thêm thông tin khác nếu cần
                            String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                            intent.putExtra("date_time", currentDateTime);

                            startActivity(intent);
                        })
                        .show();

            } else {
                Toast.makeText(ban2.this, "Chưa có món nào để thanh toán!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Định dạng tiền tệ Việt Nam
    private String formatCurrencyVND(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount);
    }

    // Hàm tải dữ liệu từ SharedPreferences
    private ArrayList<String> loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("menuData", Context.MODE_PRIVATE);
        Set<String> menuSet = sharedPreferences.getStringSet("menuList", new HashSet<>());
        return new ArrayList<>(menuSet);
    }
}
