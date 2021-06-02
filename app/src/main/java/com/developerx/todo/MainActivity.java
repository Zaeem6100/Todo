package com.developerx.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.developerx.todo.Adaptor.TodoAdaptor;
import com.developerx.todo.Model.todoModel;
import com.developerx.todo.Utils.AddNewTask;
import com.developerx.todo.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListner {

    private RecyclerView taskRecyclerView ;
    private TodoAdaptor taskAdapter;
    private List<todoModel> tasklist;
    private DatabaseHandler db;
    private FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        db = new DatabaseHandler(this);
        db.openDatabase();

         taskRecyclerView = findViewById(R.id.tasksRecyclerView);
         taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         taskAdapter = new TodoAdaptor(db,this);
         taskRecyclerView.setAdapter(taskAdapter);
         fab = findViewById(R.id.fab);

         ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
         itemTouchHelper.attachToRecyclerView(taskRecyclerView);
         tasklist = new ArrayList<>();

//         //dummy data
//        todoModel task = new todoModel();
//        task.setTask("Test task");
//        task.setStatus(0);
//        task.setId(1);
//        tasklist.add(task);
//        tasklist.add(task);
//        tasklist.add(task);
//        tasklist.add(task);
//        taskAdapter.setTask(tasklist);

        tasklist = db.getAllTasks();
        Collections.reverse(tasklist);
        taskAdapter.setTask(tasklist);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {

        tasklist = db.getAllTasks();
        Collections.reverse(tasklist);
        taskAdapter.setTask(tasklist);
        taskAdapter.notifyDataSetChanged();
    }
}