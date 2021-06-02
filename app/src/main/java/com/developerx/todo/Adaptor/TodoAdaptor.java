package com.developerx.todo.Adaptor;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.developerx.todo.MainActivity;
import com.developerx.todo.Model.*;
import com.developerx.todo.R;
import com.developerx.todo.Utils.AddNewTask;
import com.developerx.todo.Utils.DatabaseHandler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.developerx.todo.Utils.AddNewTask.TAG;

public class TodoAdaptor extends RecyclerView.Adapter<TodoAdaptor.ViewHolder> {
    private List<todoModel> todolist;
    private MainActivity activity;
    private DatabaseHandler db;



    public TodoAdaptor(DatabaseHandler db, MainActivity activity) {
        this.activity = activity;
        this.db = db;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklayout,parent,false);
        return new ViewHolder(itemView);
    }

    public Context getContext() {
        return activity;
    }

    public void onBindViewHolder(ViewHolder holder , int position ){
        db.openDatabase();
        todoModel item  = todolist.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.updateStatus(item.getId(),0);
                }
                else {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    public  int getItemCount(){
        return todolist.size();
    }
    private boolean toBoolean(int n){
        return n!=0;
    }

    public  void  setTask(List<todoModel> todolist){
        this.todolist = todolist;
        notifyDataSetChanged();
    }
    public void setTasks(List<todoModel> todolist){
        this.todolist = todolist;
        notifyDataSetChanged();
    }
    public  void  deleteItem(int position){
        todoModel item = todolist.get(position);
        db.deleteTask(item.getId());
        todolist .remove(position);
        notifyItemRemoved(position);
    }

    public  void  editItem(int position){
        todoModel item = todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        public ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.checkbox);
        }
    }
}
