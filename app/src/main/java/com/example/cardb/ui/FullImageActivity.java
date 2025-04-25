package com.example.cardb.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;

import androidx.viewpager2.widget.ViewPager2;

import com.example.cardb.R;

import java.util.ArrayList;

import com.example.cardb.data.adapter.FullscreenImageAdapter;

public class FullImageActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FullscreenImageAdapter adapter;
    private ArrayList<Uri> imageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_full_image);

        viewPager = findViewById(R.id.viewPager);

        Intent intent = getIntent();
        imageUris = intent.getParcelableArrayListExtra("imageUris");
        int startIndex = intent.getIntExtra("startIndex", 0);

        adapter = new FullscreenImageAdapter(this, imageUris);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(startIndex, false);
    }
}