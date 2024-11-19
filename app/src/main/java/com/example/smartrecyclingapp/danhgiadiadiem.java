package com.example.smartrecyclingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class danhgiadiadiem extends AppCompatActivity {

    private Button btnGuiDanhGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhgiadiadiem);

        // Lấy tham chiếu đến nút "Gửi"
        btnGuiDanhGia = findViewById(R.id.btnGuiDanhGia);

        // Xử lý sự kiện khi nhấn nút "Gửi"
        btnGuiDanhGia.setOnClickListener(v -> {
            // Thực hiện hành động gửi đánh giá (có thể là lưu vào cơ sở dữ liệu hoặc gửi server)

            // Thông báo cho người dùng
            Toast.makeText(danhgiadiadiem.this, "Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();

            // Chuyển trở lại MainActivity
            Intent intent = new Intent(danhgiadiadiem.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear stack activity cũ
            startActivity(intent);
            finish(); // Đóng activity này
        });
    }
}
