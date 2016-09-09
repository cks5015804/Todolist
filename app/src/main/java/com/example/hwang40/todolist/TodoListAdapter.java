package com.example.hwang40.todolist;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.hwang40.todolist.models.Todo;
import com.example.hwang40.todolist.utils.UIUtils;

import java.util.List;


public class TodoListAdapter extends RecyclerView.Adapter{

    private MainActivity activity;
    private List<Todo> data;

    public TodoListAdapter(MainActivity activity, List<Todo> data){
        this.activity = activity;
        this.data = data;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        Todo todo = data.get(position);
        ((TodoListViewHolder)vh).todoText.setText(todo.text);
        ((TodoListViewHolder)vh).todoCheckBox.setChecked(todo.done);
        setupItem(position,(TodoListViewHolder)vh);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private void setupItem(final int position, TodoListViewHolder vh){
        Todo todo = data.get(position);
        UIUtils.setTextViewStrikeThrough(vh.todoText, todo.done);

        vh.todoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.updateTodo(position, isChecked);
            }
        });

        final Todo todo2 = data.get(position);
        vh.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,TodoEditActivity.class);
                intent.putExtra(TodoEditActivity.KEY_TODO, todo2);
                activity.startActivityForResult(intent, MainActivity.REQ_CODE_TODO_EDIT);
            }
        });
    }

    public class TodoListViewHolder extends RecyclerView.ViewHolder {
        TextView todoText;
        CheckBox todoCheckBox;

        public TodoListViewHolder(View itemView) {
            super(itemView);
            this.todoText = (TextView) itemView.findViewById(R.id.main_list_item_text);
            this.todoCheckBox = (CheckBox) itemView.findViewById(R.id.main_list_item_check);
        }
    }
}
