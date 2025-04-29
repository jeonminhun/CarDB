package com.example.cardb.ui;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cardb.R;
import com.example.cardb.data.adapter.ImagePagerAdapter;
import com.example.cardb.data.entity.Car;
import com.example.cardb.data.repository.CarRepository;
import com.example.cardb.fileUtils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditCarActivity extends AppCompatActivity {

    private EditText editKindInput, NumberInput, codeInput, contextInput, DayInput;
    private Button UpdateCar;
    private CarRepository repository;
    private Car car; // 수정할 대상

    private List<Uri> imageUris = new ArrayList<>();
    private List<String> Images = new ArrayList<>();
    private ImagePagerAdapter imagePagerAdapter;
    private ViewPager2 imagePager;
    private Button chooseImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        imagePager = findViewById(R.id.imagePager);
        chooseImageButton = findViewById(R.id.selectImageButton);

        repository = new CarRepository(getApplicationContext());

        editKindInput = findViewById(R.id.editKind);
        NumberInput = findViewById(R.id.editNumber);
        codeInput = findViewById(R.id.editCode);
        contextInput = findViewById(R.id.editContext);
        DayInput = findViewById(R.id.editDay);
        UpdateCar = findViewById(R.id.btnUpdateCar);



        // 전달받은 Car 객체
        car = (Car) getIntent().getSerializableExtra("car");
        Log.d("EditCarActivity", "받은 Car ID: " + car.getId());

        // 기존 데이터 세팅
        editKindInput.setText(car.getCarKind());
        NumberInput.setText(car.getCarNumber());
        codeInput.setText(car.getCode());
        contextInput.setText(car.getContext());
        DayInput.setText(car.getDay());



        for (String path : car.getImagePath()) {
            Uri uri = Uri.fromFile(new File(path));
            imageUris.add(uri);
            Images.add(path); // 기존 경로 유지
        }

        imagePagerAdapter = new ImagePagerAdapter(this, imageUris);
        imagePager.setAdapter(imagePagerAdapter);


        ActivityResultLauncher<PickVisualMediaRequest> pickImages = registerForActivityResult(
                new ActivityResultContracts.PickMultipleVisualMedia(),
                uris -> {
                    int remain = 10 - imageUris.size();
                    if (uris.size() > remain) uris = uris.subList(0, remain);
                    imageUris.addAll(uris);
                    imagePagerAdapter.notifyDataSetChanged();
                }
        );

        chooseImageButton.setOnClickListener(v -> {
            pickImages.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

        UpdateCar.setOnClickListener(v -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler mainHandler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                try {
                    List<String> updatedImagePaths = new ArrayList<>();
                    for (Uri uri : imageUris) {
                        if ("file".equals(uri.getScheme())) {
                            updatedImagePaths.add(uri.getPath());
                        } else {
                            Bitmap bitmap = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
                            } else {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            }
                            String newPath = FileUtil.saveImageToInternalStorage(bitmap, this);
                            updatedImagePaths.add(newPath);
                        }
                    }

                    car.setCarKind(editKindInput.getText().toString());
                    car.setCarNumber(NumberInput.getText().toString());
                    car.setCode(codeInput.getText().toString());
                    car.setContext(contextInput.getText().toString());
                    car.setDay(DayInput.getText().toString());
                    car.setImagePath(updatedImagePaths);

                    repository.updateCar(car);

                    mainHandler.post(() -> {
                        Toast.makeText(this, "수정 완료!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, CarFull.class).putExtra("car", car));
                        finish();
                    });
                } catch (IOException e) {
                    mainHandler.post(() -> Toast.makeText(this, "이미지 저장 실패", Toast.LENGTH_SHORT).show());
                }
            });
        });
    }
}
