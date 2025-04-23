package com.example.cardb.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.cardb.R;
import com.example.cardb.data.adapter.ImagePagerAdapter;
import com.example.cardb.data.entity.Car;
import com.example.cardb.data.repository.CarRepository;
import com.example.cardb.fileUtils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class addActivity extends AppCompatActivity {
    private EditText editKindInput, NumberInput, contextInput, DayInput;
    private Button chooseImageButton, saveButton;


    private List<String> Images;
    private CarRepository repository;

    private List<Uri> imageUris = new ArrayList<>();
    private ImagePagerAdapter imagePagerAdapter;
    private ViewPager2 imagePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        repository = new CarRepository(getApplicationContext());

        editKindInput = findViewById(R.id.editKind);
        NumberInput = findViewById(R.id.Number);
        contextInput = findViewById(R.id.context);
        DayInput = findViewById(R.id.Day);
        chooseImageButton = findViewById(R.id.selectImageButton);
        saveButton = findViewById(R.id.addCarButton);

        // onCreate 내부에서 초기화
        imagePager = findViewById(R.id.imagePager);
        imagePagerAdapter = new ImagePagerAdapter(this, imageUris);
        imagePager.setAdapter(imagePagerAdapter);

        Images = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(10), uris -> {
            if (!uris.isEmpty()) {

                imageUris.clear();
                imageUris.addAll(uris); // 슬라이더 이미지 데이터 갱신
                imagePagerAdapter.notifyDataSetChanged();

                for (int i = 0; i < uris.size(); i++) {
                    Uri uri = uris.get(i);

                    // 👉 백그라운드에서 이미지 저장
                    int finalI = i; // 람다에서 사용할 변수는 final 또는 effectively final이어야 함
                    executor.execute(() -> {
                        try {
                            Bitmap bitmap;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                                bitmap = ImageDecoder.decodeBitmap(source);
                            } else {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            }

                            String imagePath = FileUtil.saveImageToInternalStorage(bitmap, this);
                            synchronized (Images) {
                                Images.add(imagePath);
                            }

                            // UI 스레드에서 토스트 표시
                            mainHandler.post(() -> {
                                if (finalI == uris.size() - 1) {
                                    Toast.makeText(this, uris.size() + "개의 이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                            mainHandler.post(() -> Toast.makeText(this, "이미지 저장 실패: " + uri, Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        DayInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                DayInput.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        saveButton.setOnClickListener(v -> {
            String kind = editKindInput.getText().toString();
            String Day = String.valueOf(DayInput.getText());
            String context = contextInput.getText().toString();
            String number = NumberInput.getText().toString();

            if (kind.isEmpty() || Day.isEmpty() || context.isEmpty() || number.isEmpty() || Images.isEmpty()) {
                Toast.makeText(addActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Car car = new Car(kind, number, context, Day, Images);
            repository.insert(car);

            Toast.makeText(addActivity.this, "자동차 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(addActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 리스트 화면으로 돌아가기
        });

        chooseImageButton.setOnClickListener(v -> {
            Toast.makeText(addActivity.this, "이미지 선택!", Toast.LENGTH_SHORT).show();

            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE).build());
        });
    }
}
