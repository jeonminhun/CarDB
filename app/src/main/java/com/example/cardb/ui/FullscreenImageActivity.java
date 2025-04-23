package com.example.cardb.ui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cardb.R;

import java.util.ArrayList;
import java.util.List;

public class FullscreenImageActivity extends AppCompatActivity {

    private List<ImageView> previewImageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        // 10개 이미지뷰 ID 등록
        previewImageViews = new ArrayList<>();
        previewImageViews.add(findViewById(R.id.previewImage1));
        previewImageViews.add(findViewById(R.id.previewImage2));
        previewImageViews.add(findViewById(R.id.previewImage3));
        previewImageViews.add(findViewById(R.id.previewImage4));
        previewImageViews.add(findViewById(R.id.previewImage5));
        previewImageViews.add(findViewById(R.id.previewImage6));
        previewImageViews.add(findViewById(R.id.previewImage7));
        previewImageViews.add(findViewById(R.id.previewImage8));
        previewImageViews.add(findViewById(R.id.previewImage9));
        previewImageViews.add(findViewById(R.id.previewImage10));

        // 이미지 리스트 받기
        ArrayList<Uri> imageUris = getIntent().getParcelableArrayListExtra("imageUris");

        // Glide로 이미지뷰에 이미지 세팅
        for (int i = 0; i < previewImageViews.size(); i++) {
            if (imageUris != null && i < imageUris.size()) {
                Glide.with(this).load(imageUris.get(i)).into(previewImageViews.get(i));
            }
        }
    }
}
