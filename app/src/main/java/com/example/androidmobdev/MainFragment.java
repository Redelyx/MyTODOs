package com.example.androidmobdev;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = "Main Fragment";

    private RecyclerView mRecyclerView = null;
    private LinearLayoutManager mLayoutManager = null;
    private MyAdapter mAdapter = null;
    private LiveData<List<ToDo>> mLiveDataList = null;
    private ImageButton addButton = null;

    private Context mContext = null;
    private ToDoManager toDoManager = null;

    private int mType;

    public MainFragment(int i){
        this.mType = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called!");
        final View rootView = inflater.inflate(R.layout.list_fragment, container, false);

        this.mContext = getActivity();
        this.toDoManager = ToDoManager.getInstance(mContext);

        init(rootView);
        observeLogData();

        return rootView;
    }

    private void init(View rootView){
        //recupero la recycler view tramite id
        mRecyclerView  = (RecyclerView)rootView.findViewById(R.id.my_recycler_view);                        //devo recuperare i componenti dalla vista e non dall'activity

        mLayoutManager  = new LinearLayoutManager(mContext);                                           // use a linear layout manager
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);                                        //orientamento verticale
        mLayoutManager.scrollToPosition(0);                                                                 //parte dalla posizione 0

        mRecyclerView.setLayoutManager(mLayoutManager);                                                     //associo il layout che ho deciso alla recicler view

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // specify an adapter
        switch(this.mType){
            case 1:
                mAdapter = new MyAdapter(ToDoManager.getInstance(mContext).getYetToDoList(), mContext);break;
            case 2:
                mAdapter = new MyAdapter(ToDoManager.getInstance(mContext).getDoneList(), mContext);break;
            default:
                mAdapter = new MyAdapter(ToDoManager.getInstance(mContext).getToDoList(), mContext);break;
        }

        mRecyclerView.setAdapter(mAdapter); //setto l'adapter sulla recyclerview

        addButton = (ImageButton) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG,"Add Button Clicked !");
                addToDoDialog();
            }
        });
    }

    private void observeLogData(){
        switch(this.mType){
            case 1:
                mLiveDataList = this.toDoManager.getYetToDoListLiveData();break;
            case 2:
                mLiveDataList = this.toDoManager.getDoneListLiveData();break;
            default:
                mLiveDataList = this.toDoManager.getToDoListLiveData();break;
        }

        mLiveDataList.observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                if(toDos != null){
                    Log.d(TAG, "Update Log List Received ! List Size: " + toDos.size());
                    refreshRecyclerView(toDos, 0);
                }
                else
                    Log.e(TAG, "Error observing Log List ! Received a null Object !");
            }
        });
    }

    private void refreshRecyclerView(List<ToDo> updatedList, int scrollPosition){
        mAdapter.setmDataset(updatedList);
        mAdapter.notifyDataSetChanged();
        if(scrollPosition >= 0)
            mLayoutManager.scrollToPosition(scrollPosition);
    }

    private void addToDoDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View v = getLayoutInflater().inflate(R.layout.add_todo, null);

        final ToDo todo = new ToDo();
        final EditText nameInput = (EditText)v.findViewById(R.id.name_input);
        final TextView textduedate = (TextView) v.findViewById(R.id.duedate_res);
        final EditText noteInput = (EditText)v.findViewById(R.id.note_input);
        final Spinner categoryInput = (Spinner) v.findViewById(R.id.category_input);
        Button okButton = (Button)v.findViewById(R.id.dialog_ok);
        Button cancelButton = (Button)v.findViewById(R.id.dialog_cancel);
        final Button duedateButton = (Button)v.findViewById(R.id.due_date_input);

        dialog.setView(v);

        final AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("New To Do");

        duedateButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog.Builder datedialog = new AlertDialog.Builder(mContext);
                 View v1 = getLayoutInflater().inflate(R.layout.date_dialog, null);

                 final DatePicker duedateInput = (DatePicker) v1.findViewById(R.id.date_input);
                 duedateInput.setMinDate(Calendar.getInstance().getTimeInMillis());
                 Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
                 Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);

                 datedialog.setView(v1);

                 final AlertDialog alertDialog1 = datedialog.create();
                 alertDialog1.setCanceledOnTouchOutside(false);
                 alertDialog1.setTitle("New To Do");

                 cancelButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         alertDialog1.dismiss();
                     }
                 });

                 okButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         int month = duedateInput.getMonth()+1;
                         final String strDueDate = duedateInput.getYear() +  "-" + month + "-" + duedateInput.getDayOfMonth() ;
                         Date dueDate = null;
                         try {
                             dueDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(strDueDate);
                         } catch (ParseException e) {
                             e.printStackTrace();
                         }
                         todo.setDuedate(dueDate);
                         textduedate.setText(strDueDate);
                         alertDialog1.dismiss();

                     }
                 });
                 alertDialog1.show();
             }

         });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Calendar.getInstance().getTime();
                String name = nameInput.getText().toString();
                String category = categoryInput.getSelectedItem().toString();

                if(name.matches("")){
                    Toast.makeText(mContext, "Give your ToDo a name!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else{
                    todo.setName(name);
                    todo.setCategory(category);
                    todo.setDate(date);
                    todo.setNote(noteInput.getText().toString());

                    ToDoManager.getInstance(mContext).addToDo(todo);
                    mAdapter.notifyDataSetChanged();

                    mLayoutManager.scrollToPosition(0);

                    View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
                    Snackbar.make(rootView, "New ToDo created!", Snackbar.LENGTH_SHORT).show();

                    //Toast.makeText(mContext, "New ToDo created!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }
}
