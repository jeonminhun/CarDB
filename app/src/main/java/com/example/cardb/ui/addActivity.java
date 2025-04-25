package com.example.cardb.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cardb.R;
import com.example.cardb.data.adapter.ImagePagerAdapter;
import com.example.cardb.data.entity.Car;
import com.example.cardb.data.repository.CarRepository;
import com.example.cardb.fileUtils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class addActivity extends AppCompatActivity {
    private EditText editKindInput, NumberInput, codeInput, contextInput, DayInput;
    private Button chooseImageButton, saveButton;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private List<String> Images;
    private CarRepository repository;
    private List<Uri> imageUris = new ArrayList<>();
    private ImagePagerAdapter imagePagerAdapter;
    private ViewPager2 imagePager;
    private Uri cameraImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        repository = new CarRepository(getApplicationContext());

        Images = new ArrayList<>();

        ViewBinding();

        // onCreate 내부에서 초기화
        imagePagerAdapter = new ImagePagerAdapter(this, imageUris);
        imagePager.setAdapter(imagePagerAdapter);

        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = getPickVisualMediaRequestActivityResultLauncher();

        // 이미지 선택 버튼 클릭 리스너
        chooseImageButton.setOnClickListener(v -> {

            if (imageUris.size() >= 10) {
                Toast.makeText(this, "이미 최대 10장을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            new android.app.AlertDialog.Builder(this)
                    .setTitle("이미지 추가 방법")
                    .setItems(new CharSequence[]{"사진 촬영", "갤러리에서 선택"}, (dialog, which) -> {
                        if (which == 0) {
                            takePhoto(); // 카메라 촬영
                        } else {
                            pickMultipleMedia.launch(
                                    new PickVisualMediaRequest.Builder()
                                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                            .build()
                            );
                        }
                    })
                    .show();
        });

        // 날짜 입력 클릭 리스너
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

        // 차량 추가 버튼 클릭 리스너
        saveButton.setOnClickListener(v -> {
            String kind = editKindInput.getText().toString();
            String day = DayInput.getText().toString();
            String code = codeInput.getText().toString();
            String context = contextInput.getText().toString();
            String number = NumberInput.getText().toString();

            if (isAnyFieldEmpty(kind, day, context, number)) {
                Toast.makeText(addActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 비동기 작업 시작
            new SaveCarTask().execute(kind, day, code, context, number);
        });
    }

    @NonNull
    private ActivityResultLauncher<PickVisualMediaRequest> getPickVisualMediaRequestActivityResultLauncher() {
        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = registerForActivityResult(
                new ActivityResultContracts.PickMultipleVisualMedia(),
                uris -> {
                    int remaining = 10 - imageUris.size();
                    if (!uris.isEmpty()) {
                        if (uris.size() > remaining) {
                            List<Uri> limitedUris = uris.subList(0, remaining);
                            imageUris.addAll(limitedUris);
                            Toast.makeText(this, "최대 10장까지만 선택할 수 있어요.", Toast.LENGTH_SHORT).show();
                        } else {
                            imageUris.addAll(uris);
                        }
                        imagePagerAdapter.notifyDataSetChanged();
                    }
                }
        );
        return pickMultipleMedia;
    }

    private void ViewBinding() {
        editKindInput = findViewById(R.id.editKind);
        NumberInput = findViewById(R.id.Number);
        contextInput = findViewById(R.id.context);
        codeInput = findViewById(R.id.code);
        DayInput = findViewById(R.id.Day);
        chooseImageButton = findViewById(R.id.selectImageButton);
        saveButton = findViewById(R.id.addCarButton);
        imagePager = findViewById(R.id.imagePager);
    }

    // 유효성 검사 메소드
    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // 비동기 작업 처리 (AsyncTask)
    private class SaveCarTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String kind = params[0];
            String day = params[1];
            String code = params[2];
            String context = params[3];
            String number = params[4];

            // 이미지 저장 (백그라운드에서 처리)
            saveImages(imageUris);
            // 자동차 객체 생성 및 DB 저장 (백그라운드에서 처리)
            Car car = new Car(kind, number, code, context, day, Images);
            repository.insert(car);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // UI 스레드에서 실행
            Toast.makeText(addActivity.this, "자동차 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(addActivity.this, MainActivity.class));
            finish(); // 리스트 화면으로 돌아가기
        }
    }


    // 이미지 저장 메소드
    private void saveImages(List<Uri> imageUris) {
        ExecutorService executor = Executors.newFixedThreadPool(4); // 병렬 처리 스레드 풀
        for (Uri uri : imageUris) {
            executor.submit(() -> {
                try {
                    String imgpath = FileUtil.saveImageFromUri(this, uri); // 이미지 저장
                    Log.d("이거 뭐냐 : ", imgpath);

                    // UI 스레드에서 Images 리스트에 추가
                    new Handler(Looper.getMainLooper()).post(() -> Images.add(imgpath));

                } catch (Exception e) {
                    Log.e("이미지 저장 실패", "URI: " + uri.toString(), e); // 예외 처리
                }
            });
        }
        executor.shutdown();
        try {
            // 모든 작업이 완료될 때까지 대기
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // 60초 내에 종료되지 않으면 강제 종료
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
                if (result && cameraImageUri != null) {
                    imageUris.add(cameraImageUri);
                    imagePagerAdapter.notifyDataSetChanged();

                    // 내부 저장소에 저장
                    try {
                        Bitmap bitmap;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), cameraImageUri);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cameraImageUri);
                        }

                        String imagePath = FileUtil.saveImageToInternalStorage(bitmap, this);
                        Images.add(imagePath);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "촬영 이미지 저장 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            launchCamera();
        }
    }

    private void launchCamera() {
        File imageFile = new File(getExternalCacheDir(), "photo_" + System.currentTimeMillis() + ".jpg");
        cameraImageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);
        takePictureLauncher.launch(cameraImageUri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허용 후 카메라 실행
                launchCamera();
            } else {
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
