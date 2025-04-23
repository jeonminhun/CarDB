package com.example.cardb.fileUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static String saveImageToInternalStorage(Bitmap bitmap, Context context) {
        String fileName = "car_" + System.currentTimeMillis() + ".png";
        File directory = context.getDir("car_images", Context.MODE_PRIVATE);
        File imageFile = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile.getAbsolutePath();
    }

    public static String saveImageFromUri(Context context, Uri imageUri) {
        try {
            // 1. URI에서 Bitmap 가져오기
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            // 2. 내부 저장소에 저장
            return saveImageToInternalStorage(bitmap, context);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
