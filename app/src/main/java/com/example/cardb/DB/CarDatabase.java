package com.example.cardb.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.cardb.DAO.CarDao;
import com.example.cardb.entity.Car;

@Database(entities = {Car.class}, version = 1)
public abstract class CarDatabase extends RoomDatabase {
    public abstract CarDao carDao();
}