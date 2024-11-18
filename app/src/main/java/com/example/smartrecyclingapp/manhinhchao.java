package com.example.smartrecyclingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.MediaController; // Thêm media controller cho VideoView
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class manhinhchao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manhinhchao);

        // Thiết lập VideoView
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.manhinhchao;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Thêm MediaController để dễ dàng kiểm soát video (play, pause)


        // Bắt đầu phát video
        videoView.start();

        // Lắng nghe sự kiện khi video kết thúc
        videoView.setOnCompletionListener(mp -> {
            // Animation dịch chuyển trên trục Y khi video kết thúc
            videoView.animate().translationY(9000).setDuration(2000).withEndAction(() -> {
                // Chuyển sang MainActivity sau khi video kết thúc và animation hoàn tất
                Intent intent = new Intent(manhinhchao.this, manhinhchao2.class);
                startActivity(intent);
                finish(); // Đóng activity hiện tại để không quay lại khi nhấn nút Back
            }).start();
        });

        // Áp dụng WindowInsets cho các thành phần giao diện



    }
}


