package com.example.androidmobdev;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToDoActivity extends AppCompatActivity{
    public static String TAG = "ToDoActivity";
    static final String INFO_ID_EXTRA = "myToDoId";

    private Button cancelButton;
    private Button editButton;

    private ToDo todo = null;
    private int id;
    private Context mContext = null;

    public ToDo getTodo() {
        return todo;
    }

    public void setTodo(ToDo todo) {
        this.todo = todo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);      //imposto il layout

        Log.d(TAG,"onCreate() called !");

        mContext = this;
        this.cancelButton = findViewById(R.id.delete);
        this.editButton = findViewById(R.id.edit);

        if (savedInstanceState == null) {

            ToDoFragment toDoFragment = new ToDoFragment();

            id = this.getIntent().getExtras().getInt(INFO_ID_EXTRA,0);

            this.todo = ToDoManager.getInstance(mContext).searchToDoById(id);

            toDoFragment.setToDo(this.todo);

            getSupportFragmentManager().beginTransaction().add(R.id.container, toDoFragment).commit();    //retrocompatibilità, transazioni si gestiscono tramite fragment manager, inserisco il mio fragment nella transaction, poi commit
            //r.id.container è l'id del frame layout in cui inserire il fragment
        }

        setupToolBar();
    }

    private void setupToolBar(){
        //ToolBar and ActionBar Settings
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            case R.id.delete:
                delete();
                return true;
            case R.id.edit:
                edit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.remove_element_message);
        builder.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ToDoManager.getInstance(mContext).removeToDo(todo);
                Utilities.showSnackBar(ToDoActivity.this, "ToDo deleted!");
                ToDoFragment toDoFragment1 = new ToDoFragment();
                toDoFragment1.setToDo(ToDoManager.getInstance(mContext).searchToDoById(id));
                getSupportFragmentManager().beginTransaction().replace(R.id.container, toDoFragment1).commit();

            }
        }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Utilities.showSnackBar(ToDoActivity.this, "Not deleted!");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void edit(){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.add_todo, null);

        final EditText nameInput = (EditText)v.findViewById(R.id.name_input);
        final TextView dueDateText = (TextView) v.findViewById(R.id.duedate_res);
        final EditText noteInput = (EditText)v.findViewById(R.id.note_input);
        final Spinner categoryInput = (Spinner) v.findViewById(R.id.category_input);
        Button okButton = (Button)v.findViewById(R.id.dialog_ok);
        Button cancelButton = (Button)v.findViewById(R.id.dialog_cancel);
        final Button dueDateButton = (Button)v.findViewById(R.id.due_date_input);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.edit_category, android.R.layout.simple_spinner_item);
        categoryInput.setAdapter(adapter);
        nameInput.setHint(R.string.unchanged);
        noteInput.setHint(R.string.unchanged);

        dialog.setView(v);

        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Edit To Do");

        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder dateDialog = new android.app.AlertDialog.Builder(mContext);
                View v1 = getLayoutInflater().inflate(R.layout.date_dialog, null);

                final DatePicker dueDateInput = (DatePicker) v1.findViewById(R.id.date_input);
                dueDateInput.setMinDate(Calendar.getInstance().getTimeInMillis());

                Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
                Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);

                dateDialog.setView(v1);

                final android.app.AlertDialog alertDialog1 = dateDialog.create();
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
                        int month = dueDateInput.getMonth()+1;
                        final String strDueDate = dueDateInput.getYear() +  "-" + month + "-" + dueDateInput.getDayOfMonth() ;
                        Date dueDate = null;
                        try {
                            dueDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(strDueDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(dueDate != null){
                            ToDoManager.getInstance(mContext).editToDoDueDate(getTodo(), dueDate);
                        }

                        dueDateText.setText(strDueDate);
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
                String name = nameInput.getText().toString();
                String category = categoryInput.getSelectedItem().toString();
                String note = noteInput.getText().toString();

                if(!name.matches("")) ToDoManager.getInstance(mContext).editToDoName(getTodo(), name);
                if(!category.matches("-" )) ToDoManager.getInstance(mContext).editToDoCategory(getTodo(), category);
                if(!note.matches("")) ToDoManager.getInstance(mContext).editToDoNote(getTodo(), note);

                Utilities.showSnackBar(ToDoActivity.this, "ToDo edited!");
                ToDoFragment toDoFragment1 = new ToDoFragment();
                toDoFragment1.setToDo(getTodo());
                getSupportFragmentManager().beginTransaction().replace(R.id.container, toDoFragment1).commit();

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}

