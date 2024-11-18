package com.example.smartrecyclingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartrecyclingapp.Database.UserDAO;

public class manhinhlogin extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnDangNhap;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhlogin);

        // Thiết lập Edge-to-Edge cho giao diện
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.manhinhlogin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo UserDAO để thao tác với cơ sở dữ liệu
        userDAO = new UserDAO(this);

        // Liên kết các thành phần UI
        edtUsername = findViewById(R.id.edtTenDangNhap);
        edtPassword = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        // Sự kiện cho nút Đăng nhập
        btnDangNhap.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Kiểm tra các trường nhập
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(manhinhlogin.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Kiểm tra thông tin đăng nhập với cơ sở dữ liệu
                boolean isAuthenticated = userDAO.authenticateUser(username, password);
                if (isAuthenticated) {
                    Toast.makeText(manhinhlogin.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Lấy thông tin fullname và phoneNumber từ database
                    String fullName = userDAO.getFullName(username);  // Lấy fullName từ database
                    String phoneNumber = userDAO.getPhoneNumber(username);  // Lấy phoneNumber từ database

                    // Chuyển sang màn hình MainActivity
                    Intent intent = new Intent(manhinhlogin.this, MainActivity.class);

                    // Gửi dữ liệu vào MainActivity
                    intent.putExtra("userNameTextView", username);  // Truyền username
                    intent.putExtra("edtHovaTen2", fullName);
                    intent.putExtra("edtSoDienThoai2", phoneNumber);  // Đảm bảo không có dấu cách thừa
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);  // Lưu tên đăng nhập
                    editor.apply();
                    // Khởi chạy MainActivity
                    startActivity(intent);
                    finish();  // Đóng màn hình đăng nhập
                } else {
                    Toast.makeText(manhinhlogin.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
            });
    }
}










