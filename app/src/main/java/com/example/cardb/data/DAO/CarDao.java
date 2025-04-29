package com.example.cardb.data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cardb.data.entity.Car;

import java.util.List;

@Dao
public interface CarDao {
    @Insert
    void insert(Car car);

    @Query("SELECT * FROM Car LIMIT :limit OFFSET :offset")
    List<Car> getCarsPaginated(int limit, int offset);
    @Query("SELECT * FROM Car WHERE carNumber LIKE '%' || :query || '%' LIMIT :limit OFFSET :offset")
    List<Car> searchCarsPaginated(String query, int limit, int offset);
    @Update
    int updateCar(Car car);
    @Query("DELETE FROM Car WHERE id = :carId")
    void deleteCar(int carId);
}