package com.example.cardb.ui;

import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cardb.R;
import com.example.cardb.data.entity.Car;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CarFull extends AppCompatActivity {

    private ImageView mainImageView;
    private LinearLayout thumbnailContainer;
    private List<String> imageUris;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_full);



        mainImageView = findViewById(R.id.detailMainImage);
        thumbnailContainer = findViewById(R.id.imageThumbnailContainer);

        TextView carKind = findViewById(R.id.detailCarKind);
        TextView carNumber = findViewById(R.id.detailCarNumber);
        TextView carDay = findViewById(R.id.detailDay);
        TextView carCode = findViewById(R.id.detailCode);
        TextView carContext = findViewById(R.id.detailContext);

        // 데이터 받기

        Car car = (Car) getIntent().getSerializableExtra("car");

        carKind.setText("차종: " + car.getCarKind());
        carNumber.setText("차번: " + car.getCarNumber());
        carDay.setText("날짜: " + car.getDay());
        carCode.setText("도장 코드: " + car.getCode());
        carContext.setText("특이사항: " + car.getContext());

        imageUris = car.getImagePath();

        if (imageUris != null && !imageUris.isEmpty()) {
            // 대표 이미지 로딩
            Glide.with(this).load(imageUris.get(0)).into(mainImageView);

            Log.d("CarFull", "이미지 개수: " + imageUris.size());

            // 썸네일 생성
            for (int i = 0; i < imageUris.size(); i++) {

                Uri uri = Uri.fromFile(new File(imageUris.get(i)));

                ImageView thumb = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(240, 240);
                params.setMargins(12, 12, 12, 12);
                thumb.setLayoutParams(params);
                thumb.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(this).load(uri).into(thumb);

                int finalI = i;
                thumb.setOnClickListener(v -> {
                    // 썸네일 클릭 시 대표 이미지 변경
                    Glide.with(this).load(imageUris.get(finalI)).into(mainImageView);
                });

                thumbnailContainer.addView(thumb);
            }

            // 대표 이미지 클릭 시 풀스크린 보기로
            mainImageView.setOnClickListener(v -> {
                Intent fullscreenIntent = new Intent(this, FullImageActivity.class);

                // String → Uri 변환
                ArrayList<Uri> uriList = new ArrayList<>();
                for (String path : imageUris) {
                    uriList.add(Uri.fromFile(new File(path)));
                }
                fullscreenIntent.putParcelableArrayListExtra("imageUris", uriList);
                fullscreenIntent.putExtra("startIndex", 0);
                startActivity(fullscreenIntent);
            });
        }


    }
}