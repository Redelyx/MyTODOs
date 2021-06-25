package com.example.androidmobdev;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Room;


public class ToDoManager {
    private static final String TAG = "ToDoManager";
    private Context mContext = null;
    private AppDatabase db = null;
    private ToDoDao toDoDao = null;
    private String outputFileName = "todos.csv";

//singleton java espone i metodi per gestire una lista di oggetti ToDo, essendo un singleton, ho una sola istanza all'interno dell'app
//disaccoppio le responsabilità cosi da non dovermi preoccupare di modificare tutto se modifico qualcosa
    /*
     * The instance is static so it is shared among all instances of the class. It is also private
     * so it is accessible only within the class.
     */
    private static ToDoManager instance = null;
    /*
     * The constructor is private so it is accessible only within the class.
     */
    private ToDoManager(Context context){
        Log.d(MainActivity.TAG,"ToDo Manager Created !");
        this.mContext = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, "todo").allowMainThreadQueries().build();
        this.toDoDao = this.db.todoDao();
    }

    public static ToDoManager getInstance(Context context){
        /*
         * The constructor is called only if the static instance is null, so only the first time
         * that the getInstance() method is invoked.
         * All the other times the same instance object is returned.
         */
        if(instance == null)
            instance = new ToDoManager(context);
        return instance;
    }

    public void addToDo(ToDo todo){
        this.toDoDao.insertAll(todo);
    }

    public void removeToDo(ToDo todo){
        this.toDoDao.delete(todo);
    }

    public void editToDoName(ToDo todo, String name){
        removeToDo(todo);
        todo.setName(name);
        addToDo(todo);
    }

    public void editToDoNote(ToDo todo, String note){
        removeToDo(todo);
        todo.setNote(note);
        addToDo(todo);
    }

    public void editToDoDueDate(ToDo todo, Date dueDate){
        removeToDo(todo);
        todo.setDuedate(dueDate);
        addToDo(todo);
    }

    public void editToDoCategory(ToDo todo, String category){
        removeToDo(todo);
        todo.setCategory(category);
        addToDo(todo);
    }

    public void modifyToDoStatus(ToDo todo){
        if(todo.getStatus()==true){
            removeToDo(todo);
            todo.setStatus(false);
            addToDo(todo);
        }else{
            removeToDo(todo);
            todo.setStatus(true);
            addToDo(todo);
        }
    }

    public boolean exportOnSharedDocument(Uri uri, List<ToDo> todos) {

        if(uri == null){
            Log.e(TAG, "Error Exporting on Shared Storage Document ! Uri = Null !");
            return false;
        }

        try {

            OutputStream outputStream = mContext.getContentResolver().openOutputStream(uri);

            if(outputStream != null && todos.size() != 0){
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                bw.write("Name,Category,Date,DueDate,Status,Note\n");
                for (ToDo todo:todos) {bw.write(todo.tocsvString());}
                bw.flush();
                bw.close();

                return true;
            }
            else{
                Log.e(TAG, "Error Exporting on Shared Storage Document ! OutputStream or Json Content = Null !");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ToDo> searchToDo(String string) {return toDoDao.searchToDo(string);}

    public ToDo searchToDoById(int id) {return toDoDao.searchToDoById(id);}

    public List<ToDo> searchToDoByCategory(String string) {return toDoDao.searchToDoByCategory(string);}

    public List<ToDo> searchToDoByDate(Date from, Date to) {return toDoDao.searchToDoByDate(from, to);}

    public List<ToDo> searchToDoByDueDate(Date from, Date to) {return toDoDao.searchToDoByDueDate(from, to);}

    public List<ToDo> getToDoList(){ return this.toDoDao.getAll(); }

    public List<ToDo> getYetToDoList(){ return this.toDoDao.getYetToDo();}

    public List<ToDo> getDoneList(){ return this.toDoDao.getDone();}


    public LiveData<List<ToDo>> getQueryListLiveData(String string){ return this.toDoDao.getQueryListLiveData(string);}

    public LiveData<List<ToDo>> getToDoListLiveData(){ return this.toDoDao.getAllLiveData();}

    public LiveData<List<ToDo>> getYetToDoListLiveData(){ return this.toDoDao.getYetToDoLiveData();}

    public LiveData<List<ToDo>> getDoneListLiveData(){ return this.toDoDao.getDoneLiveData();}

}
