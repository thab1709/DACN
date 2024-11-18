package com.example.smartrecyclingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText edtHovaTen2, edtUsername2, edtSoDienThoai2;
    private Button btnXacNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        edtHovaTen2 = findViewById(R.id.edtHovaTen2);
        edtUsername2 = findViewById(R.id.edtUsername2);
        edtSoDienThoai2 = findViewById(R.id.edtSoDienThoai2);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        // Nhận dữ liệu từ Intent
        String fullName = getIntent().getStringExtra("fullName");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        // Điền sẵn thông tin vào các EditText
        edtHovaTen2.setText(fullName);
        edtSoDienThoai2.setText(phoneNumber);

        // Sự kiện click nút Xác nhận
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin từ các EditText
                String updatedFullName = edtHovaTen2.getText().toString();
                String updatedPhoneNumber = edtSoDienThoai2.getText().toString();

                if (updatedFullName.isEmpty() || updatedPhoneNumber.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Có thể lưu thông tin vào SharedPreferences hoặc database tùy nhu cầu

                    // Trở lại MainActivity
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.putExtra("fullName", updatedFullName);  // Truyền lại thông tin cập nhật nếu cần
                    intent.putExtra("phoneNumber", updatedPhoneNumber);  // Truyền lại thông tin cập nhật nếu cần
                    startActivity(intent);
                    finish();  // Đóng ProfileActivity
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Chuyển trở lại MainActivity khi nhấn nút quay lại
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();  // Đóng ProfileActivity
    }
}




