package com.example.cardb.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.cardb.R;
import com.example.cardb.data.DB.CarDatabase;
import com.example.cardb.data.entity.Car;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        CarDatabase db = Room.databaseBuilder(getApplicationContext(),
                CarDatabase.class, "car-database").build();
        new Thread(() -> {
            Car car = new Car();
            car.brand = "Toyota";
            car.model = "Camry";
            car.year = 2020;

            db.carDao().insert(car);
        }).start();

        new Thread(() -> {
            List<Car> cars = db.carDao().getAllCars();
            for (Car c : cars) {
                Log.d("Car", c.brand + " " + c.model + " (" + c.year + ")");
            }
        }).start();

        TextView carText = findViewById(R.id.carText);

        new Thread(() -> {
            List<Car> cars = db.carDao().getAllCars();
            StringBuilder builder = new StringBuilder();
            for (Car c : cars) {
                builder.append(c.brand)
                        .append(" ")
                        .append(c.model)
                        .append(" (")
                        .append(c.year)
                        .append(")\n");
            }
            // 깃 허브 확인용

            runOnUiThread(() -> carText.setText(builder.toString()));
        }).start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}