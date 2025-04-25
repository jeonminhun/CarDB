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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private CarAdapter adapter;
    private CarRepository repository;
    private EditText editText1;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int limit = 20;  // 한 번에 불러올 데이터의 개수
    private int offset = 0;  // 불러올 시작 위치

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        repository = new CarRepository(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        // 버튼 클릭 시 임의로 데이터 추가
//        Button buttonAddTestData = findViewById(R.id.buttonAddTestData);
//        buttonAddTestData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 임의의 데이터를 생성
//                Car car1 = new Car("123ABC", "Toyota", "Corolla", "testContext", "Black", new ArrayList<>());
//                Car car2 = new Car("456DEF", "Honda", "Civic", "testContext", "Blue", new ArrayList<>());
//                Car car3 = new Car("789GHI", "Ford", "Focus", "testContext", "Red", new ArrayList<>());
//
//                // 데이터를 데이터베이스에 삽입
//                repository.insert(car1);
//                repository.insert(car2);
//                repository.insert(car3);
//            }
//        });


        // 앱 시작 시 데이터 로딩
        loadData(offset, limit);

        // 검색 기능 구현
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
                // 검색어가 변경될 때마다 새로고침
                offset = 0;  // 검색 시, 처음부터 다시 불러오기
                new Thread(() -> {
                    List<Car> cars = repository.searchCarsPaginated(String.valueOf(editText1.getText()), limit, offset);
                    runOnUiThread(() -> {
                        adapter = new CarAdapter(cars);
                        recyclerView.setAdapter(adapter);
                    });
                }).start();
            }
        });

        // 데이터 추가 버튼 클릭
        Button buttonAdd = findViewById(R.id.dataAdd);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, addActivity.class);
            startActivity(intent);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 스크롤이 마지막 아이템 근처에 오면 다음 페이지 데이터를 로드
                if (!recyclerView.canScrollVertically(1)) {
                    // 더 이상 스크롤할 공간이 없다면 (마지막 아이템에 도달)
                    offset += limit; // 다음 페이지의 시작 위치
                    loadData(offset, limit); // 데이터를 불러오기
                }
            }
        });


    }

    private void loadData(int offset, int limit) {
        new Thread(() -> {
            // 데이터베이스에서 페이지네이션 방식으로 데이터 로드
            List<Car> cars = repository.getCarsPaginated(limit, offset);

            // UI 스레드에서 RecyclerView 갱신
            runOnUiThread(() -> {
                if (adapter == null) {
                    // 처음 데이터가 로드될 때 어댑터를 설정
                    adapter = new CarAdapter(cars);
                    recyclerView.setAdapter(adapter);
                } else {
                    // 이미 데이터가 있다면, 데이터를 추가로 갱신
                    adapter.addCars(cars);  // 새로운 데이터를 추가
                    adapter.notifyDataSetChanged();  // 어댑터 갱신
                }
            });
        }).start();
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        // 새로고침 시 offset을 0으로 초기화하고 데이터를 처음부터 로드
        offset = 0;

        // 기존 데이터를 비우고 새 데이터를 로드
        new Handler().postDelayed(() -> {
            // 기존 데이터를 초기화
            if (adapter != null) {
                adapter.clearCars();  // 이 메서드는 adapter에서 데이터를 삭제하는 메서드로 정의해야 합니다.
            }

            // 새로 데이터를 로드
            loadData(offset, limit);
            mSwipeRefreshLayout.setRefreshing(false);
        }, 1000);  // 1초 뒤에 새로고침 동작 실행
    }
}


