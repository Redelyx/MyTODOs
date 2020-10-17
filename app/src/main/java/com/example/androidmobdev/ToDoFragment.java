package com.example.androidmobdev;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ToDoFragment extends Fragment {
    private static final String TAG = "ToDo Fragment";
    private ToDo todo;

    public ToDoFragment() {}

    public ToDo getToDo() {return todo;}		                                                                  //getter
    public void setToDo(ToDo todo) {this.todo = todo;}                                                            //setter


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.todo_fragment, container, false);
        TextView TextView = (TextView)rootView.findViewById(R.id.todoTextView);
        ToDo todo = getToDo();
        if(todo == null){
            TextView.setText("Nothing to see here!");
        }else{
            TextView.setText(String.valueOf(todo.toString()));
        }
        return rootView;
    }
}
