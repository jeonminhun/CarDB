package com.example.cardb.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Car implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "carKind")
    public String carKind;

    @ColumnInfo(name = "carNumber")
    public String carNumber;

    @ColumnInfo(name = "code")
    public String code;

    @ColumnInfo(name = "context")
    public String context;

    @ColumnInfo(name = "day")
    public String day;

    @ColumnInfo(name = "imagePath")
    public List<String> imagePath;

    public int getId() {
        return id;
    }
    public String getCode() {
        return code;
    }
    public String getCarKind() {
        return carKind;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getDay() {
        return day;
    }

    public List<String> getImagePath() {
        return imagePath;
    }

    public void setCarKind(String carKind) {
        this.carKind = carKind;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setImagePath(List<String> imagePath) {
        this.imagePath = imagePath;
    }

    public String getContext() {
        return context;
    }

    public Car(String carKind, String carNumber, String code, String context, String day, List<String> imagePath) {
        this.carKind = carKind;
        this.carNumber = carNumber;
        this.code = code;
        this.context = context;
        this.day = day;
        this.imagePath = imagePath;
    }
}
