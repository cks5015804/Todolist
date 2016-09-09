package com.example.hwang40.todolist.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.hwang40.todolist.AlarmReceiver;
import com.example.hwang40.todolist.TodoEditActivity;
import com.example.hwang40.todolist.models.Todo;

import java.util.Calendar;

/**
 * Created by hwang40 on 9/5/16.
 */
public class AlarmUtils {
    public static void setAlarm(@NonNull Context context, @NonNull Todo todo){
        Calendar c = Calendar.getInstance();
        if(todo.remindDate.compareTo(c.getTime()) < 0){
            return;
        }

        AlarmManager alarmMgr;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(TodoEditActivity.KEY_TODO,todo);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP,todo.remindDate.getTime(),alarmIntent);
    }
}
