package com.example.quanlycaphe.ban;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycaphe.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class hoadon extends AppCompatActivity {

    TextView txt_soban, txt_ngaygio, txt_tthd;
    Button btn_in, btn_thoathd;
    ListView lv_hdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hoadon);

        txt_soban = findViewById(R.id.txt_soban);
        txt_ngaygio = findViewById(R.id.txt_ngaygio);
        txt_tthd = findViewById(R.id.txt_tthd);
        btn_in = findViewById(R.id.btn_in);
        btn_thoathd = findViewById(R.id.btn_thoathd);
        lv_hdd = findViewById(R.id.lv_hdd);

        // Nhận dữ liệu từ Intent
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String tableNumber = intent.getStringExtra("table_number"); // Lấy số bàn
        String dateTime = intent.getStringExtra("date_time"); // Lấy ngày giờ đúng
        ArrayList<String> items = intent.getStringArrayListExtra("items"); // Lấy danh sách món
        double totalPrice = intent.getDoubleExtra("totalPrice", 0.0); // Lấy tổng tiền



// Hiển thị dữ liệu
        txt_soban.setText("Bàn số: " + tableNumber);
        txt_ngaygio.setText("Ngày giờ: " + dateTime);
        txt_tthd.setText("Tổng tiền: " + formatCurrencyVND(totalPrice));  // Hiển thị tổng tiền


        // Hiển thị danh sách món
        if (items != null && !items.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
            lv_hdd.setAdapter(adapter);
        }
        Log.d("hoadon", "Ngày giờ nhận được: " + dateTime);

        // In hóa đơn
        btn_in.setOnClickListener(v -> printInvoice(tableNumber, dateTime, items, totalPrice));

        // Đóng Activity
        btn_thoathd.setOnClickListener(v -> finish());
    }

    private String formatCurrencyVND(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount);
    }

    // Hàm in hóa đơn
    private void printInvoice(String storeName, String dateTime, ArrayList<String> items, double totalPrice) {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        if (printManager != null) {
            PrintAttributes printAttributes = new PrintAttributes.Builder()
                    .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .build();

            // Tạo đối tượng PrintDocumentAdapter để in dữ liệu
            PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
                @Override
                public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle metadata) {
                    callback.onLayoutFinished(new PrintDocumentInfo.Builder("Hóa Đơn")
                            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                            .setPageCount(1)
                            .build(), true);
                }

                @Override
                public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                    PrintedPdfDocument pdfDocument = new PrintedPdfDocument(hoadon.this, printAttributes);
                    try {
                        PdfDocument.Page page = pdfDocument.startPage(0);

                        // Vẽ nội dung vào trang PDF
                        Canvas canvas = page.getCanvas();
                        canvas.drawColor(Color.WHITE); // Nền trắng
                        drawInvoiceDetails(canvas, storeName, dateTime, items, totalPrice); // Vẽ chi tiết hóa đơn

                        pdfDocument.finishPage(page);
                        pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        pdfDocument.close();
                        callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                    }
                }
            };

            // Bắt đầu in
            printManager.print("Hóa Đơn", printAdapter, null);
            // Sau khi in xong, lưu thông tin hóa đơn vào SharedPreferences
            saveInvoiceData(storeName, dateTime, totalPrice);
        }
    }
    private void saveInvoiceData(String storeName, String dateTime, double totalPrice) {
        SharedPreferences sharedPreferences = getSharedPreferences("printedInvoices", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Tạo key duy nhất cho mỗi hóa đơn (dựa trên ngày giờ)
        String key = dateTime + "_" + storeName;

        // Lưu thông tin hóa đơn vào SharedPreferences
        editor.putString(key + "_storeName", storeName);
        editor.putString(key + "_dateTime", dateTime);
        editor.putFloat(key + "_totalPrice", (float) totalPrice);
        editor.apply();
    }

    // Hàm vẽ chi tiết hóa đơn lên canvas
    private void drawInvoiceDetails(Canvas canvas, String storeName, String dateTime, ArrayList<String> items, double totalPrice) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.LEFT);

        // Vẽ tên cửa hàng
        canvas.drawText("Cửa hàng: " + storeName, 50, 50, paint);

        // Vẽ thời gian
        canvas.drawText("Ngày giờ: " + dateTime, 50, 100, paint);

        // Vẽ danh sách món
        int yPosition = 150;
        for (String item : items) {
            canvas.drawText(item, 50, yPosition, paint);
            yPosition += 50;
        }

        // Vẽ tổng tiền
        canvas.drawText("Tổng tiền: " + formatCurrencyVND(totalPrice), 50, yPosition, paint);


        // Lời cảm ơn
        canvas.drawText("Cảm ơn quý khách!", 50, yPosition, paint);
    }

}
