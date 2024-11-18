package com.example.smartrecyclingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartrecyclingapp.Database.UserDAO;

public class manhinhdangky extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextFullName, editTextPhoneNumber;
    private Button buttonSignUp;
    private ProgressBar progressBar;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhdangky);

        // Khởi tạo các thành phần giao diện
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        progressBar = findViewById(R.id.progressBar);

        // Khởi tạo DAO để thao tác với cơ sở dữ liệu
        userDAO = new UserDAO(this);

        // Xử lý sự kiện khi nhấn nút Đăng Ký
        buttonSignUp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String fullName = editTextFullName.getText().toString().trim();
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();

            // Kiểm tra các điều kiện nhập liệu
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(manhinhdangky.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Kiểm tra độ dài mật khẩu
            if (password.length() < 7) {
                Toast.makeText(manhinhdangky.this, "Mật khẩu phải có ít nhất 7 ký tự", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Kiểm tra độ dài của số điện thoại phải lớn hơn hoặc bằng 5 ký tự và là các chữ số
            if (phoneNumber.length() < 5 || !phoneNumber.matches("\\d+")) {
                Toast.makeText(manhinhdangky.this, "Số điện thoại phải có ít nhất 5 chữ số", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Thực hiện thêm người dùng vào cơ sở dữ liệu
            boolean isInserted = userDAO.addUser(username, password, fullName, phoneNumber);

            progressBar.setVisibility(View.GONE);
            if (isInserted) {
                Toast.makeText(manhinhdangky.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(manhinhdangky.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        });

    }
}



