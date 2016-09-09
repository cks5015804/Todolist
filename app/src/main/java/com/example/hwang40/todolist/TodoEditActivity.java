package com.example.hwang40.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.hwang40.todolist.models.Todo;
import com.example.hwang40.todolist.utils.AlarmUtils;
import com.example.hwang40.todolist.utils.DateUtils;
import com.example.hwang40.todolist.utils.UIUtils;

import java.util.Calendar;
import java.util.Date;


public class TodoEditActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    public static final String KEY_TODO = "todo";
    public static final String KEY_TODO_ID = "todo_id";
    public static final String KEY_NOTIFICATION_ID = "notification_id";


    private Todo todo;
    private Date remindDate;

    private EditText todotitle;
    private TextView date;
    private TextView time;
    private CheckBox completed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        todo = getIntent().getParcelableExtra(KEY_TODO);
        remindDate = todo != null ? todo.remindDate : null;

        setupUI();
        cancelNotificationIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelNotificationIfNeeded() {
        int notificationId = getIntent().getIntExtra(KEY_NOTIFICATION_ID, -1);
        if (notificationId != -1) {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationId);
        }
    }

    private void setupUI(){
        setupActionBar();

        todotitle = (EditText) findViewById(R.id.todo_edit_title);
        date = (TextView) findViewById(R.id.todo_edit_alarm_date);
        time = (TextView) findViewById(R.id.todo_edit_alarm_time);
        completed = (CheckBox) findViewById(R.id.todo_edit_completed_checkbox);

        if(todo == null) {
            findViewById(R.id.todo_edit_delete).setVisibility(View.GONE);
        }else{
            todotitle.setText(todo.text);
            UIUtils.setTextViewStrikeThrough(todotitle,todo.done);
            completed.setChecked(todo.done);

            findViewById(R.id.todo_edit_delete).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    delete();
                }
            });
        }

        if(remindDate != null){
            date.setText(DateUtils.dateToStringDate(remindDate));
            time.setText(DateUtils.dateToStringTime(remindDate));
        }else{
            date.setText("Set date");
            time.setText("Set time");
        }

        setupcheckbox();
        setupDate();
        setupSaveButton();
    }

    private void setupActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        setTitle(null);
    }

    private void setupcheckbox(){
        completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                UIUtils.setTextViewStrikeThrough(todotitle,isChecked);
                todotitle.setTextColor(isChecked ? Color.GRAY : Color.WHITE);
            }
        });

        View completeWrapper = findViewById(R.id.todo_edit_complete_wrapper);
        completeWrapper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                completed.setChecked(!completed.isChecked());
            }
        });
    }

    private void setupDate(){
        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar c = getCalendarFromRemindDate();
                Dialog dialog = new DatePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar c = getCalendarFromRemindDate();
                Dialog dialog = new TimePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
                dialog.show();
            }
        });
    }

    private void setupSaveButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.todo_edit_fab);
        fab.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });
    }

    private void saveAndExit(){
        if(todo == null){
            todo = new Todo(todotitle.getText().toString(),remindDate);
        }else{
            todo.text = todotitle.getText().toString();
            todo.remindDate = remindDate;
        }

        todo.done = completed.isChecked();

        if(todo.remindDate != null){
            AlarmUtils.setAlarm(this,todo);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_TODO,todo);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

    private void delete(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_TODO_ID,todo.id);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

    private Calendar getCalendarFromRemindDate(){
        Calendar c = Calendar.getInstance();
        if(remindDate != null){
            c.setTime(remindDate);
        }
        return c;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar c = getCalendarFromRemindDate();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);

        remindDate = c.getTime();
        time.setText(DateUtils.dateToStringTime(remindDate));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
        Calendar c = getCalendarFromRemindDate();
        c.set(year,month,dayofmonth);

        remindDate = c.getTime();
        date.setText(DateUtils.dateToStringDate(remindDate));
    }
}
