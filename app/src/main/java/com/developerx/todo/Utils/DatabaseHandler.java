package com.developerx.todo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developerx.todo.Model.todoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private  static  final  int VERSION =1;
    private  static final  String NAME = "toDoListDatabase";
    private  static final String TODO_TABLE = "todo";
    private  static final String ID = "id";
    private  static final String TASK = "task";
    private  static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db ;

    public DatabaseHandler(Context context){
        super(context,NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }
    @Override
    public  void  onUpgrade(SQLiteDatabase db,int oldVersion , int newVersion){

        // drop the order tables
        String query = "DROP TABLE IF  EXISTS " + TODO_TABLE;
        db.execSQL(query);
    }
    public  void  openDatabase(){
        db= this.getWritableDatabase();
    }
    public  void  insertTask(todoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS , task.getStatus());
        db.insert(TODO_TABLE,null,cv);

    }

    public List<todoModel> getAllTasks(){
        List<todoModel> tasklist = new ArrayList<>();
        Cursor  cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE,null,null,null,null,null,null,null);
            if (cur !=null){
                if (cur.moveToFirst()){
                    do {
                        todoModel task = new todoModel();
                        task.setId(cur.getColumnIndex(ID));
                        task.setStatus(cur.getColumnIndex(STATUS));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        tasklist.add(task);
                    }while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return  tasklist;
    }

    public  void  updateTask(int id , String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        db.update(TODO_TABLE,cv, ID + "=?",new String[] {String.valueOf(id)});
    }
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public  void  deleteTask(int id){
        db.delete(TODO_TABLE, ID + "=?",new String[] {String.valueOf(id)});
    }

}
