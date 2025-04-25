package com.example.cardb.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cardb.R;
import com.example.cardb.data.entity.Car;
import com.example.cardb.ui.CarFull;
import com.example.cardb.ui.FullscreenImageActivity;
import com.example.cardb.ui.MainActivity;

import java.io.File;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<Car> carList;

    public void setCarList(List<Car> carList) {
        this.carList = carList;
        notifyDataSetChanged();  // 데이터 변경 후 어댑터 갱신
    }
    public void clearCars() {
        carList.clear();
        notifyDataSetChanged();
    }

        public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView editKind, Number, code, context, Day;

        View ListItem;
        public CarViewHolder(View itemView) {
            super(itemView);
            ListItem = itemView.findViewById(R.id.ListItem);
            imageView = itemView.findViewById(R.id.imageView);
            editKind = itemView.findViewById(R.id.editKind);
            Number = itemView.findViewById(R.id.Number);
            code = itemView.findViewById(R.id.code);
            context = itemView.findViewById(R.id.context);
            Day = itemView.findViewById(R.id.Day);
        }
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.editKind.setText(String.format("차종 : %s", car.getCarKind()));
        holder.Number.setText(String.format("차번 : %s", car.getCarNumber()));
        holder.code.setText(String.format("도장 코드 : %s", car.getCode()));
        holder.context.setText(String.format("특이사항 : %s", car.getContext()));
        holder.Day.setText(String.format("날짜 : %s", car.getDay()));

        // 이미지 경로가 있을 경우 Glide로 로드
        List<String> imagePath = car.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(new File(imagePath.get(0)))  // 경로를 File 객체로 감싸야 함
                    .placeholder(R.drawable.placeholder) // 로딩 중 기본 이미지 (선택)
                    .error(R.drawable.error_image)       // 에러 시 이미지 (선택)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder); // 기본 이미지
        }

        holder.ListItem.setOnClickListener(v->{
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, CarFull.class);
            intent.putExtra("car", car);
            v.getContext().startActivity(intent);
                }
        );

    }

    public CarAdapter(List<Car> carList) {
        this.carList = carList;
    }

    public void addCarList(List<Car> newCars) {
        carList.addAll(newCars);  // 새로운 데이터를 기존 리스트에 추가
    }

    public void addCars(List<Car> cars) {
        this.carList.addAll(cars);  // 새로운 데이터를 기존 리스트에 추가
    }


    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }
}
