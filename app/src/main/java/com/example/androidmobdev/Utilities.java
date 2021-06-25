package com.example.androidmobdev;

import android.app.Activity;
import android.view.View;

import androidx.room.TypeConverter;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static Date fromString(String string)  {
        if(string == null){
            return null;
        }else{
            try {
                return (Date) new SimpleDateFormat("yyyy-MM-dd").parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String dateToString(Date date) {
        return date == null ? null : date.toString();
    }

    public static void showSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

}
