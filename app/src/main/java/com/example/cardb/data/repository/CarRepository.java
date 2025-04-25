package com.example.cardb.data.repository;
import android.content.Context;


import com.example.cardb.data.DAO.CarDao;
import com.example.cardb.data.DB.CarDatabase;
import com.example.cardb.data.entity.Car;

import java.util.List;

public class CarRepository {

    private final CarDao carDao;

    public CarRepository(Context context) {
        CarDatabase db = CarDatabase.getInstance(context);
        carDao = db.carDao();
    }

    public void insert(Car car) {
        new Thread(() -> carDao.insert(car)).start();
    }

    public List<Car> getCarsPaginated(int limit, int offset) {
        return carDao.getCarsPaginated(limit, offset);
    }

    public List<Car> searchCarsPaginated(String query, int limit, int offset) {
        return carDao.searchCarsPaginated(query, limit, offset);
    }
}