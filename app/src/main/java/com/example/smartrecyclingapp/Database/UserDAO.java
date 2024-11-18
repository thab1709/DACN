package com.example.smartrecyclingapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDAO {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    // Constructor để khởi tạo UserDAO
    public UserDAO(Context context) {
        openHelper = new Database(context);  // Database là lớp SQLiteOpenHelper của bạn
    }

    // Hàm mở cơ sở dữ liệu để thao tác
    public void open() {
        database = openHelper.getWritableDatabase();
    }

    // Hàm đóng cơ sở dữ liệu
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    // Hàm kiểm tra thông tin đăng nhập
    public boolean authenticateUser(String username, String password) {
        open(); // Mở cơ sở dữ liệu
        Cursor cursor = database.rawQuery(
                "SELECT * FROM nguoidung WHERE username = ? AND password =?",
                new String[]{username, password}
        );
        boolean isAuthenticated = cursor.getCount() > 0;
        cursor.close();
        close(); // Đóng cơ sở dữ liệu
        return isAuthenticated;
    }

    // Hàm thêm người dùng mới
    public boolean addUser(String username, String password, String fullName, String phoneNumber) {
        open(); // Mở kết nối cơ sở dữ liệu
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("hoten", fullName);
        values.put("sodienthoai", phoneNumber);

        long result = database.insert("nguoidung", null, values);
        close(); // Đóng kết nối cơ sở dữ liệu
        return result != -1; // Trả về true nếu thêm thành công
    }

    // Kiểm tra số điện thoại đã tồn tại trong cơ sở dữ liệu
    public int checkSoDienThoai(String soDienThoai) {
        Cursor cursor = database.rawQuery("SELECT * FROM nguoidung WHERE sodienthoai = ?", new String[]{String.valueOf(soDienThoai)});
        if (cursor.getCount() != 0) {
            cursor.close();  // Đảm bảo đóng cursor
            return -1;
        }
        cursor.close();  // Đảm bảo đóng cursor
        return 1;
    }

    // Kiểm tra tên đăng nhập đã tồn tại trong cơ sở dữ liệu
    public int checkUserName(String userName) {
        Cursor cursor = database.rawQuery("SELECT * FROM nguoidung WHERE username = ?", new String[]{String.valueOf(userName)});
        if (cursor.getCount() != 0) {
            cursor.close();  // Đảm bảo đóng cursor
            return -1;
        }
        cursor.close();  // Đảm bảo đóng cursor
        return 1;
    }

    // Phương thức lấy fullName dựa trên username
    public String getFullName(String username) {
        open();
        Cursor cursor = database.rawQuery("SELECT hoten FROM nguoidung WHERE username = ?", new String[]{username});
        String fullName = "";
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("hoten");
            if (columnIndex >= 0) {  // Kiểm tra columnIndex hợp lệ
                fullName = cursor.getString(columnIndex);
            }
        }
        cursor.close();  // Đảm bảo đóng cursor
        close();
        return fullName;
    }

    public String getPhoneNumber(String username) {
        open();
        Cursor cursor = database.rawQuery("SELECT sodienthoai FROM nguoidung WHERE username = ?", new String[]{username});
        String phoneNumber = "";
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("sodienthoai");
            if (columnIndex >= 0) {  // Kiểm tra columnIndex hợp lệ
                phoneNumber = cursor.getString(columnIndex);
            }
        }
        cursor.close();  // Đảm bảo đóng cursor
        close();
        return phoneNumber;
    }
    // Kiểm tra mật khẩu cũ
    public boolean checkOldPassword(String username, String oldPassword) {
        open(); // Mở kết nối cơ sở dữ liệu
        Cursor cursor = database.rawQuery("SELECT * FROM nguoidung WHERE username = ? AND password = ?",
                new String[]{username, oldPassword});
        boolean isPasswordCorrect = cursor.getCount() > 0;  // Nếu có kết quả thì mật khẩu cũ đúng
        cursor.close();
        close();
        return isPasswordCorrect;
    }
    // Cập nhật mật khẩu mới
    public boolean updatePassword(String username, String newPassword) {
        open(); // Mở kết nối cơ sở dữ liệu
        ContentValues values = new ContentValues();
        values.put("password", newPassword); // Cập nhật mật khẩu mới
        int rowsUpdated = database.update("nguoidung", values, "username = ?", new String[]{username});
        close();
        return rowsUpdated > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
    }


}



