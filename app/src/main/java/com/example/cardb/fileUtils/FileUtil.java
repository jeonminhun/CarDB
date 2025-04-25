package com.example.cardb.fileUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    // 이미지 크기 리사이즈
    private static Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float ratio = (float) width / height;
        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    // JPEG로 저장 (압축 + 크기 줄이기)
    public static String saveImageToInternalStorage(Bitmap bitmap, Context context) {
        String fileName = "car_" + System.currentTimeMillis() + ".jpg"; // JPEG로 저장
        File directory = context.getDir("car_images", Context.MODE_PRIVATE);
        File imageFile = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            Bitmap resized = resizeBitmap(bitmap, 1024); // 1024px 이하로 리사이징
            resized.compress(Bitmap.CompressFormat.JPEG, 80, fos); // 80% 품질로 압축
            resized.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile.getAbsolutePath();
    }

    // URI에서 이미지 받아 저장
    public static String saveImageFromUri(Context context, Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            return saveImageToInternalStorage(bitmap, context);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

