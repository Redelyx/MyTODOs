package com.example.androidmobdev;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToDoActivity extends AppCompatActivity{
    public static String TAG = "ToDoActivity";
    private ToDo todo = null;
    private int position = 0;
    private Context mContext = null;

    public ToDo getTodo() {
        return todo;
    }

    public void setTodo(ToDo todo) {
        this.todo = todo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);      //imposto il layout
        Log.d(TAG,"onCreate() called !");

        if (savedInstanceState == null) {

            ToDoFragment toDoFragment = new ToDoFragment();

            position = this.getIntent().getExtras().getInt("myToDoPos",0);

            setTodo(ToDoManager.getInstance(getApplicationContext()).getToDoList().get(position));

            toDoFragment.setToDo(this.todo);

            getSupportFragmentManager().beginTransaction().add(R.id.container, toDoFragment).commit();    //retrocompatibilità, transazioni si gestiscono tramite fragment manager, inserisco il mio fragment nella transaction, poi commit
            //r.id.container è l'id del frame layout in cui inserire il fragment
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle(R.string.app_todo_title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        //attivo il tasto di navigazione per tornare indietro
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {                            //gestione del menu, intercetto il "click" (gestisco le azioni)
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_info:
                openInfoActivity();
                return true;
            case R.id.delete:
                delete();
                return true;
            case R.id.edit:
                edit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openInfoActivity(){
        Log.d(TAG,"openInfoActivity() called !");
        startActivity(new Intent(this,InfoActivity.class));
    }

    private void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.remove_element_message);
        builder.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ToDoManager.getInstance(getApplicationContext()).removeToDo(todo);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "ToDo deleted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Not deleted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void edit(){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.add_todo, null);

        final EditText nameInput = (EditText)v.findViewById(R.id.name_input);
        final TextView textduedate = (TextView) v.findViewById(R.id.duedate_res);
        final EditText noteInput = (EditText)v.findViewById(R.id.note_input);
        final Spinner categoryInput = (Spinner) v.findViewById(R.id.category_input);
        Button okButton = (Button)v.findViewById(R.id.dialog_ok);
        Button cancelButton = (Button)v.findViewById(R.id.dialog_cancel);
        final Button duedateButton = (Button)v.findViewById(R.id.due_date_input);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.edit_category, android.R.layout.simple_spinner_item);
        categoryInput.setAdapter(adapter);
        nameInput.setHint(R.string.unchanged);
        noteInput.setHint(R.string.unchanged);

        dialog.setView(v);

        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Edit To Do");

        duedateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder datedialog = new android.app.AlertDialog.Builder(mContext);
                View v1 = getLayoutInflater().inflate(R.layout.date_dialog, null);

                final DatePicker duedateInput = (DatePicker) v1.findViewById(R.id.date_input);
                duedateInput.setMinDate(Calendar.getInstance().getTimeInMillis());

                Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
                Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);

                datedialog.setView(v1);

                final android.app.AlertDialog alertDialog1 = datedialog.create();
                alertDialog1.setCanceledOnTouchOutside(false);
                alertDialog1.setTitle("Edit Due Date");

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
                        if(dueDate != null){
                            ToDoManager.getInstance(mContext).editToDoDueDate(getTodo(), dueDate);
                        }

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
                ToDo newToDo = new ToDo();
                String name = nameInput.getText().toString();
                String category = categoryInput.getSelectedItem().toString();
                String note = noteInput.getText().toString();

                if(!name.matches("")) ToDoManager.getInstance(mContext).editToDoName(getTodo(), name);
                if(!category.matches("-")) ToDoManager.getInstance(mContext).editToDoCategory(getTodo(), category);
                if(!note.matches("")) ToDoManager.getInstance(mContext).editToDoNote(getTodo(), note);

                Toast.makeText(mContext, "ToDo edited!", Toast.LENGTH_SHORT).show();

                ToDoFragment toDoFragment1 = new ToDoFragment();
                toDoFragment1.setToDo(getTodo());
                getSupportFragmentManager().beginTransaction().replace(R.id.container, toDoFragment1).commit();

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


}

