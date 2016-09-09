package com.example.hwang40.todolist.utils;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * Created by hwang40 on 9/4/16.
 */
public class UIUtils {

    public static void setTextViewStrikeThrough(@NonNull TextView tv, boolean strikethrough){
        if(strikethrough){
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
