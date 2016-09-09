package com.example.hwang40.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.hwang40.todolist.models.Todo;
import com.example.hwang40.todolist.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_CODE_TODO_EDIT = 100;

    private static final String TODOS = "todos";

    private TodoListAdapter adapter;
    private List<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = ((FloatingActionButton)findViewById(R.id.fab));

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TodoEditActivity.class);
                startActivityForResult(intent,REQ_CODE_TODO_EDIT);
            }
        });

        loadData();

        adapter = new TodoListAdapter(this,todos);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_TODO_EDIT && resultCode == Activity.RESULT_OK){
            String todoId = data.getStringExtra(TodoEditActivity.KEY_TODO_ID);
            if(todoId != null){
                deleteTodo(todoId);
            }else{
                Todo todo = data.getParcelableExtra(TodoEditActivity.KEY_TODO);
                updateTodo(todo);
            }
        }
    }

    private void loadData(){
        todos = ModelUtils.read(this, TODOS, new TypeToken<List<Todo>>(){});
        if(todos == null){
            todos = new ArrayList<>();
        }
    }

    private void deleteTodo(@NonNull String todoId){
        int position = 0;
        for(; position < todos.size(); position++){
            Todo todo = todos.get(position);
            if(TextUtils.equals(todo.id,todoId)){
                todos.remove(todo);
                break;
            }
        }

        adapter.notifyItemRemoved(position);
        ModelUtils.save(this,TODOS,todos);
    }

    private void updateTodo(Todo todo){
        boolean found = false;
        int position = 0;
        for(; position < todos.size(); position++){
            Todo item = todos.get(position);
            if(TextUtils.equals(todo.id,item.id)){
                todos.set(position,todo);
                found = true;
                break;
            }
        }

        if(!found) {
            todos.add(todo);
            adapter.notifyItemInserted(position);
        }else{
            adapter.notifyItemChanged(position);
        }
        ModelUtils.save(this,TODOS,todos);
    }

    public void updateTodo(final int position, boolean isChecked) {
        todos.get(position).done = isChecked;

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(position);
                ModelUtils.save(MainActivity.this,TODOS,todos);
            }
        };
        handler.post(r);
    }
}
