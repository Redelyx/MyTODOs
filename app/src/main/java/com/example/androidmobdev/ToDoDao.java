package com.example.androidmobdev;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;
@Dao
public interface ToDoDao {
    @Query("SELECT * FROM todos ORDER BY id DESC")
    List<ToDo> getAll();

    @Query("SELECT * FROM todos WHERE status = 0 ORDER BY id DESC")
    List<ToDo> getYetToDo();

    @Query("SELECT * FROM todos WHERE status = 1 ORDER BY id DESC")
    List<ToDo> getDone();

    @Query("SELECT * FROM todos ORDER BY id DESC")
    LiveData<List<ToDo>> getAllLiveData();

    @Query("SELECT * FROM todos WHERE status = 0 ORDER BY id DESC")
    LiveData<List<ToDo>> getYetToDoLiveData();

    @Query("SELECT * FROM todos WHERE status = 1 ORDER BY id DESC")
    LiveData<List<ToDo>> getDoneLiveData();

    @Query("SELECT * " +
            "FROM todos WHERE name LIKE '%'||:s||'%' OR " +
            "date LIKE '%'||:s||'%' OR " +
            "note LIKE '%'||:s||'%' OR " +
            "category LIKE '%'||:s||'%' OR " +
            "duedate LIKE '%'||:s||'%' OR " +
            "note LIKE '%'||:s||'%'")
    List<ToDo> searchToDo(String s);

    @Query("SELECT * FROM todos WHERE category LIKE :s")
    List<ToDo> searchToDoByCategory(String s);

    @Query("SELECT * FROM todos WHERE date BETWEEN :from AND :to")
    List<ToDo> searchToDoByDate(Date from, Date to);

    @Query("SELECT * FROM todos WHERE duedate BETWEEN :from AND :to")
    List<ToDo> searchToDoByDueDate(Date from, Date to);

    @Query("SELECT * FROM todos WHERE id LIKE :id")
    ToDo searchToDoById(int id);

    @Query("SELECT * " +
            "FROM todos WHERE name LIKE '%'||:s||'%' OR " +
            "date LIKE '%'||:s||'%' OR " +
            "note LIKE '%'||:s||'%' OR " +
            "category LIKE '%'||:s||'%' OR " +
            "duedate LIKE '%'||:s||'%' OR " +
            "note LIKE '%'||:s||'%'")
    LiveData<List<ToDo>> getQueryListLiveData(String s);

    @Query("SELECT * FROM todos WHERE id IN (:userIds)")
    List<ToDo> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(ToDo... todos);

    @Delete
    void delete(ToDo todo);
}
