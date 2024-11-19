package com.example.smartrecyclingapp;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smartrecyclingapp.Database.Database;
import com.example.smartrecyclingapp.ml.ModelUnquant;
import com.google.android.material.navigation.NavigationView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import android.widget.MediaController;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.example.smartrecyclingapp.R;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;
    private static final int CAMERA_REQUEST_CODE = 103;

    private ImageView imgReturned;
    private TextView tvUploadImage, tvInfor, tvResult, Title, Message;
    private Uri imageUri;
    private Button btnsubmit;
    private Bitmap selectedBitmap;
    private ViewPager2 viewPager;
    private SliderAdapter adapter;
    private Handler sliderHandler = new Handler(Looper.getMainLooper()); // Thêm Handler để tự động chuyển ảnh
    int imageSize = 224; // Kích thước ảnh cần thiết cho mô hình
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        imgReturned = findViewById(R.id.img_returned);
        tvUploadImage = findViewById(R.id.tv_upload_image);
        tvInfor = findViewById(R.id.tv_info);
        btnsubmit = findViewById(R.id.btn_submit);
        tvResult = findViewById(R.id.tvResult);
        viewPager = findViewById(R.id.viewPager);

        // Nhận giá trị username từ Intent
        String username = getIntent().getStringExtra("userNameTextView");

        if (username != null) {
            // Lấy NavigationView và headerView
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);  // Lấy headerView từ NavigationView

            // Tìm TextView trong headerView
            TextView userNameTextView = headerView.findViewById(R.id.header_user_name);

            // Gán username vào TextView
            userNameTextView.setText(username);
        }




        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.itManHinhChinh) {
                    // Xử lý khi người dùng chọn "Màn hình chính"
                    Toast.makeText(MainActivity.this, "Chọn Màn hình chính", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                } else if (id == R.id.itThongtincanhan) {
                    // Xử lý khi người dùng chọn "Thông tin cá nhân"
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    Toast.makeText(MainActivity.this, "Chọn Thông tin cá nhân", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_profile2);
                    EditText edtFullName2 = findViewById(R.id.edtHovaTen2);   // EditText cho Họ và tên
                    EditText edtUserName2 = findViewById(R.id.edtUsername2);   // EditText cho Username
                    EditText edtPhoneNumber2 = findViewById(R.id.edtSoDienThoai2);  // EditText cho Số điện thoại
                    String fullName = getIntent().getStringExtra("fullName");

                    String phoneNumber = getIntent().getStringExtra("phoneNumber");
                    profileIntent.putExtra("fullName", fullName);

                    profileIntent.putExtra("phoneNumber", phoneNumber);
                    edtFullName2.setText(fullName);

                    edtPhoneNumber2.setText(phoneNumber);

                    startActivity(profileIntent);
                } else if (id == R.id.itDoiMatKhau) {
                    // Xử lý khi người dùng chọn "Đổi mật khẩu"
                    Intent changePasswordIntent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                    startActivity(changePasswordIntent);
                } else if (id == R.id.itlichsunhandien) {
                    // Xử lý khi người dùng chọn "Lịch sử nhận diện"
                    Toast.makeText(MainActivity.this, "Chọn Lịch sử nhận diện", Toast.LENGTH_SHORT).show();
                    Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(historyIntent);
                } else if (id == R.id.itDangXuat) {
                    // Xử lý khi người dùng chọn "Đăng Xuất"
                    Toast.makeText(MainActivity.this, "Bạn đã Đăng xuất", Toast.LENGTH_SHORT).show();
                    Intent logoutIntent = new Intent(MainActivity.this, manhinhlogin.class);
                    startActivity(logoutIntent);
                    finish();  // Đóng MainActivity sau khi đăng xuất
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        List<Integer> images = Arrays.asList(
                R.drawable.img_11,
                R.drawable.img_8,
                R.drawable.img_9,
                R.drawable.img_10
        );

        adapter = new SliderAdapter(this, images);
        viewPager.setAdapter(adapter);

        // Bắt đầu tự động chuyển ảnh
        startAutoSlider();

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        tvUploadImage.setOnClickListener(v -> openGallery());

        Button btnRefresh = findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(v -> resetImageView());

        Button btnCamera = findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(v -> checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE));

        btnsubmit.setOnClickListener(v -> {
            if (selectedBitmap != null) {
                classifyImage(selectedBitmap);
            } else {
                Toast.makeText(this, "Vui lòng chọn hoặc chụp ảnh trước", Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void startAutoSlider() {
        sliderHandler.postDelayed(sliderRunnable, 3000); // Chuyển ảnh sau mỗi 3 giây
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            int nextItem = (currentItem + 1) % adapter.getItemCount();
            viewPager.setCurrentItem(nextItem, true); // Di chuyển đến ảnh tiếp theo với hiệu ứng
            sliderHandler.postDelayed(this, 3000); // Thiết lập lại thời gian cho ảnh tiếp theo
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // Tạm dừng slider khi activity bị pause
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tiếp tục slider khi activity resume
        startAutoSlider();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    public void classifyImage(Bitmap image) {
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            Bitmap resizedImage = Bitmap.createScaledBitmap(image, imageSize, imageSize, true);
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            resizedImage.getPixels(intValues, 0, imageSize, 0, 0, imageSize, imageSize);
            int pixel = 0;
            for (int w = 0; w < imageSize; w++) {
                for (int h = 0; h < imageSize; h++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confident = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confident.length; i++) {
                if (confident[i] > maxConfidence) {
                    maxConfidence = confident[i];
                    maxPos = i;
                }
            }

            String[] classes = {"Nhựa", "Kim loại", "Bìa cứng", "Thủy tinh", "Giấy"};
            tvResult.setText(classes[maxPos]);
            showCustomDialog(classes[maxPos]);


            model.close();
        } catch (IOException | OutOfMemoryError e) {
            Toast.makeText(this, "Không thể xử lý ảnh. Vui lòng chọn ảnh nhỏ hơn.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCustomDialog(String detectedClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_recycling_guide, null);
        builder.setView(dialogView);
        TextView title = dialogView.findViewById(R.id.Title);
        TextView message = dialogView.findViewById(R.id.Message);
        VideoView videoView = dialogView.findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        dialogView.setBackground(ContextCompat.getDrawable(this, R.drawable.dialog_background));
        Button disposalLocationButton = dialogView.findViewById(R.id.disposalLocationButton);


        // Cài đặt tiêu đề và nội dung riêng cho mỗi loại rác
        switch (detectedClass) {
            case "Nhựa":
                builder.setTitle("Loại rác: Nhựa");
                builder.setMessage("Hãy xử lý rác nhựa cẩn thận để giảm thiểu ô nhiễm môi trường.");
                builder.setIcon(R.drawable.ic_plastic);  // Icon đại diện cho nhựa
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.plastic));
                break;
            case "Kim loại":
                builder.setTitle("Loại rác: Kim loại");
                builder.setMessage("Kim loại có thể tái chế, hãy đảm bảo chúng không lẫn tạp chất.");
                builder.setIcon(R.drawable.ic_metal);  // Icon đại diện cho kim loại
                break;
            case "Bìa cứng":
                builder.setTitle("Loại rác: Bìa cứng");
                builder.setMessage("Bìa cứng có thể tái chế, hãy gấp gọn và tránh để ướt.");
                builder.setIcon(R.drawable.ic_cardboard);// Icon đại diện cho bìa cứng
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.carton));
                break;
            case "Thủy tinh":
                builder.setTitle("Loại rác: Thủy tinh");
                builder.setMessage("Thủy tinh có thể tái chế, cẩn thận với các mảnh vỡ.");
                builder.setIcon(R.drawable.ic_glass);  // Icon đại diện cho thủy tinh
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.glass));
                break;
            case "Giấy":
                builder.setTitle("Loại rác: Giấy");
                builder.setMessage("Giấy có thể tái chế, đảm bảo không bị ẩm ướt.");
                builder.setIcon(R.drawable.ic_paper);  // Icon đại diện cho giấy
                videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.paper));
                break;
            default:
                builder.setTitle("Loại rác không xác định");
                builder.setMessage("Không xác định được loại rác.");
                builder.setIcon(R.drawable.ic_unknown);  // Icon mặc định
                break;
        }
        disposalLocationButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecycleLocationActivity.class);
            intent.putExtra("wasteType", detectedClass); // Truyền loại rác qua Intent
            startActivity(intent);
        });
        videoView.setOnPreparedListener(mp -> mp.setLooping(true));
        videoView.start();
        // Nút xác nhận cho dialog
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        // Hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE && data != null) {
                imageUri = data.getData();
                imgReturned.setImageURI(imageUri);
                imgReturned.setVisibility(View.VISIBLE);
                tvUploadImage.setVisibility(View.GONE);
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    selectedBitmap = Bitmap.createScaledBitmap(selectedBitmap, imageSize, imageSize, true);
                    classifyImage(selectedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(photo.getWidth(), photo.getHeight());
                photo = ThumbnailUtils.extractThumbnail(photo, dimension, dimension);
                photo = Bitmap.createScaledBitmap(photo, imageSize, imageSize, true);

                imgReturned.setImageBitmap(photo);
                imgReturned.setVisibility(View.VISIBLE);
                tvUploadImage.setVisibility(View.GONE);

                selectedBitmap = photo;
                classifyImage(selectedBitmap);
            }
        }
    }

    private void resetImageView() {
        imgReturned.setImageDrawable(null);
        imgReturned.setVisibility(View.GONE);
        tvUploadImage.setVisibility(View.VISIBLE);
        selectedBitmap = null;
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền truy cập bộ nhớ đã được cấp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền truy cập bộ nhớ bị từ chối", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Quyền truy cập camera bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

}



















