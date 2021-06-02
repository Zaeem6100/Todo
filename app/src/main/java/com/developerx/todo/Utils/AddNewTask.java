package com.developerx.todo.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.developerx.todo.DialogCloseListner;
import com.developerx.todo.Model.todoModel;
import com.developerx.todo.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public  static  final  String TAG = "ActionBottomDialog";
    private EditText newTaskText ;
    private Button newTaskSaveButton;
    private  DatabaseHandler db;


    public  static  AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void  onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }
    @Override
    public void  onViewCreated(View view ,Bundle bundle){
        super.onViewCreated(view,bundle);
        newTaskText =getView().findViewById(R.id.newTaskText);
        newTaskSaveButton =getView().findViewById(R.id.newTaskButton);
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final  Bundle bundle1 =getArguments();
        if (bundle1!=null){
            isUpdate =true;
            String task = bundle1.getString("task");
            newTaskText.setText(task);
            if (task.length()>0){
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.design_default_color_primary)) ;
            }
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.design_default_color_primary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final  boolean ISupdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if (ISupdate){
                    db.updateTask(bundle1.getInt("id"),text);
                }else {
                    todoModel task = new todoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });

    }
    @Override
    public void  onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListner){
            ((DialogCloseListner)activity).handleDialogClose(dialog);
        }
    }

}
