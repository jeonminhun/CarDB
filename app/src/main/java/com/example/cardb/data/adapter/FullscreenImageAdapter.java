package com.example.cardb.data.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cardb.R;

import java.util.ArrayList;

public class FullscreenImageAdapter extends RecyclerView.Adapter<FullscreenImageAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Uri> imageUris;

    public FullscreenImageAdapter(Context context, ArrayList<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fullscreenImage);
        }
    }

    @NonNull
    @Override
    public FullscreenImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fullscreen_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FullscreenImageAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(imageUris.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }
}
