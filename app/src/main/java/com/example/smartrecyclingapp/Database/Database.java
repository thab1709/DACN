package com.example.smartrecyclingapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    static final String DBName = "Clean-up";
    static final int DBVersion = 2;

    public Database(@Nullable Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng 'users' với các cột username, password, hoten và sodienthoai
        String dbusers = "CREATE TABLE users(" +
                "username TEXT PRIMARY KEY," +
                "password TEXT NOT NULL," +
                "hoten TEXT NOT NULL," +
                "sodienthoai TEXT NOT NULL)";

        db.execSQL(dbusers);

        // Thêm dữ liệu vào bảng mới với cấu trúc các cột mới
        db.execSQL("INSERT INTO users (username, password, hoten, sodienthoai) VALUES" +
                "('admin', 'admin', 'Mã Đào Thắng', '0879508568')," +
                "('Nhom', '123', 'Nhóm', '0123456789')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo lại bảng mới
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}


