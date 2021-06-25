package com.example.androidmobdev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ToDoFragment extends Fragment {
    private static final String TAG = "ToDo Fragment";
    private ToDo todo;

    public ToDoFragment() {}

    public void setToDo(ToDo todo) {this.todo = todo;}                                       //setter

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.todo_fragment, container, false);
        TextView TextView = (TextView)rootView.findViewById(R.id.todoTextView);
        if(todo == null){
            TextView.setText("Nothing to see here!");
            getActivity().findViewById(R.id.delete).setEnabled(false);
            getActivity().findViewById(R.id.edit).setEnabled(false);
        }else{

            TextView.setText(todo.toString());
        }
        return rootView;
    }
}
