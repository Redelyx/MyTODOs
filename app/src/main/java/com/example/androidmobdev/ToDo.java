package com.example.androidmobdev;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.Date;
@Entity(tableName= "todos")
public class ToDo {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    @ColumnInfo(name = "name")
    private String name = null;
    @ColumnInfo(name="date")
    private Date date = null;
    @ColumnInfo(name="duedate")
    private Date duedate = null;
    @ColumnInfo(name="note")
    private String note = null;
    @ColumnInfo(name="category")
    private String category = null;
    @ColumnInfo(name="status")
    private boolean status = false;

    @Ignore
    public ToDo(String name, String category, Date date, String note){
        this.name = name;
        this.date = date;
        this.duedate = null;
        this.note = note;
        this.category = category;
        this.status = false;
    }
    @Ignore
    public ToDo(String name, String category, Date date, Date duedate, String note){
        this.name = name;
        this.date = date;
        this.duedate = duedate;
        this.note = note;
        this.category = category;
        this.status = false;
    }
    public ToDo(){}

    public String getName(){
        return this.name;
    }

    public void todoCompleted(){ this.status = true;}
    public void todoNotCompleted(){ this.status = false;}

    public void setName(String name) {this.name = name;}

    public Date getDate() {return date;}
    public String getDateString() {return date.toString();}

    public void setDate(Date date) {this.date = date;}

    public Date getDuedate() {return duedate;}
    public String getDuedateString() {
        if(duedate==null){
            return "-";
        }else{
            return duedate.toString();
        }
    }

    public void setDuedate(Date duedate) {
        if(duedate==null){
            this.duedate = null;
        }else{
            this.duedate = duedate;
        }
    }

    public String getNote() {return note;}

    public void setNote(String note) {this.note = note;}

    public String getCategory() {return category;}

    public void setCategory(String category) {this.category = category;}

    public boolean getStatus() {return status;}

    public String getStatusString(){
        if(status){
            return "Completed";
        }
        else{
            return "In progress";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(boolean status) {this.status = status;}

    public String tocsvString(){
        return name+ "," +category+ "," +getDateString()+ "," +getDuedateString()+ "," +getStatusString()+ "," +note+"\n";
    }

    public String toString(){
        return "Name: "+name+ "\n\nCategory: "+category+"\n\nDate: "+date+"\n\nDue date: "+getDuedateString()+"\n\nStatus: "+getStatusString()+"\n\nNote: "+note;
    }

}
