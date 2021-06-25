package com.example.androidmobdev;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Created by Marco Picone (picone.m@gmail.com) 20/03/2020
 * Room DAO for Log Data Structure
 */
@Database(entities = {ToDo.class}, version = 1, exportSchema = false)
@TypeConverters({Utilities.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ToDoDao todoDao();
}
