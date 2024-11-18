package com.example.smartrecyclingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartrecyclingapp.Database.UserDAO;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtPassCu, edtPassMoi, edtPassMoi2;
    private Button btnChangePassword;
    private UserDAO userDAO; // Đối tượng UserDAO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);

        // Ánh xạ các EditText và Button từ layout
        edtPassCu = findViewById(R.id.edtPassCu);
        edtPassMoi = findViewById(R.id.edtPassMoi);
        edtPassMoi2 = findViewById(R.id.edtPassMoi2);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        // Khởi tạo UserDAO
        userDAO = new UserDAO(this);

        // Sự kiện khi nhấn nút "Đổi mật khẩu"
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị từ các EditText
                String oldPassword = edtPassCu.getText().toString().trim();
                String newPassword = edtPassMoi.getText().toString().trim();
                String confirmPassword = edtPassMoi2.getText().toString().trim();

                // Kiểm tra các trường nhập liệu
                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    // Kiểm tra mật khẩu cũ
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    String username = sharedPreferences.getString("username", null);  // Lấy tên đăng nhập

                    boolean isOldPasswordCorrect = userDAO.checkOldPassword(username, oldPassword);

                    if (isOldPasswordCorrect) {
                        // Cập nhật mật khẩu mới
                        boolean isUpdated = userDAO.updatePassword(username, newPassword);
                        if (isUpdated) {
                            Toast.makeText(ChangePasswordActivity.this, "Mật khẩu đã được thay đổi thành công", Toast.LENGTH_SHORT).show();
                            finish(); // Quay lại màn hình trước
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Có lỗi khi cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

