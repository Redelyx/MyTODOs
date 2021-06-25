package com.example.androidmobdev;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderBroadcast extends BroadcastReceiver {
    public static String TAG = "Remainder";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"received");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        List<ToDo> mDataList = ToDoManager.getInstance(context).searchToDoByDueDate(today, tomorrow);

        int i = 0;

        for(ToDo todo : mDataList){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent repeating_intent = new Intent(context, MainActivity.class);
            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);


            String category = todo.getCategory();
            String name = todo.getName();
            String notes = todo.getNote();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyTODO")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(category + " reminder: " + name)
                    .setContentText("Remember to: " + notes + " before tomorrow!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(i, builder.build());
            i++;
        }
    }
}
