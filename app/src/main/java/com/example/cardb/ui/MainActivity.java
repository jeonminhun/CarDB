package com.example.cardb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.cardb.R;
import com.example.cardb.data.adapter.CarAdapter;
import com.example.cardb.data.entity.Car;
import com.example.cardb.data.repository.CarRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private CarAdapter adapter;
    private CarRepository repository;
    private EditText editText1;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);//새로고침
        mSwipeRefreshLayout.setOnRefreshListener(this);

        repository = new CarRepository(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            List<Car> cars = repository.getAllCars();

            adapter = new CarAdapter(cars);
            Log.d("CarListSize", "Size: " + cars.size());
            recyclerView.setAdapter(adapter);
        }).start();

        editText1 = findViewById(R.id.SearchText);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // before Text Changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // on Text Changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                new Thread(() -> {
                    List<Car> cars = repository.SearchCars(String.valueOf(editText1.getText()));
                    runOnUiThread(() -> {
                        adapter = new CarAdapter(cars);
                        recyclerView.setAdapter(adapter);
                    });
                }).start();
            }
        });

        Button buttonAdd = findViewById(R.id.dataAdd);

        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, addActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        // 데이터를 갱신하고 UI 스레드에서 반영
        new Handler().postDelayed(() -> {
            new Thread(() -> {
                // 데이터 가져오기
                List<Car> cars = repository.getAllCars();

                // UI 스레드에서 RecyclerView 갱신
                runOnUiThread(() -> {
                    // 데이터만 갱신하기 (새로 어댑터를 설정하는 대신 notifyDataSetChanged로 갱신)
                    if (adapter == null) {
                        adapter = new CarAdapter(cars);  // 처음 어댑터가 설정될 때
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setCarList(cars);  // 데이터 갱신
                        adapter.notifyDataSetChanged();
                    }

                    // 새로고침 완료
                    mSwipeRefreshLayout.setRefreshing(false);
                });
            }).start();
        }, 1000);  // 1초 뒤에 새로고침 동작 실행
    }


}
