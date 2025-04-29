package com.example.cardb.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cardb.R;
import com.example.cardb.ui.FullscreenImageActivity;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ViewHolder> {

    private final List<Uri> imageUris;
    private final Context context;

    public ImagePagerAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = imageUris.get(position);
        Glide.with(context).load(uri).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullscreenImageActivity.class);
            intent.putParcelableArrayListExtra("imageUris", new ArrayList<>(imageUris));
            intent.putExtra("position", position);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            imageUris.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, imageUris.size());
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            btnDelete = itemView.findViewById(R.id.btnDeleteImage);
        }
    }
}
