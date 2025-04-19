package com.example.cardb.data.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.cardb.data.DAO.CarDao;
import com.example.cardb.data.entity.Car;

@Database(entities = {Car.class}, version = 1)
public abstract class CarDatabase extends RoomDatabase {
    public abstract CarDao carDao();
}