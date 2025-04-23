package com.example.cardb.data.DB;

import android.content.Context;

import androidx.room.TypeConverters;
import com.example.cardb.data.converters.Converter;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cardb.data.DAO.CarDao;
import com.example.cardb.data.entity.Car;

@Database(entities = {Car.class}, version = 4)
@TypeConverters(Converter.class)
public abstract class CarDatabase extends RoomDatabase {
    private static CarDatabase INSTANCE;
    public abstract CarDao carDao();
    // CarDatabase.java
    public static synchronized CarDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CarDatabase.class, "car-database").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

}