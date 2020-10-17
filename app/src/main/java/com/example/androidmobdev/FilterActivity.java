package com.example.androidmobdev;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    public static String TAG = "FilterActivity";

    private Context mContext = null;
    private int mType = 0;
    private Date from = null;
    private Date to = null;
    private String cat = null;
    private List<ToDo> todos = null;

    private static final String EXTERNAL_DOCUMENT_FILENAME = "todos.csv";
    private static final String DOCUMENT_APPLICATION_TYPE = "text/csv";
    private static final int EXTERNAL_FILE_CREATE_REQUEST_ID = 1818;

    public List<ToDo> getTodos() {
        return todos;
    }

    public void setTodos(List<ToDo> todos) {
        this.todos = todos;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);                                          //imposto il layout
        Log.d(TAG,"onCreate() called !");

        this.mContext = this;

        setupToolBar();

        setCat(this.getIntent().getExtras().getString("cat"));

        Long longFrom = this.getIntent().getExtras().getLong("from",0);
        setFrom(Converters.fromTimestamp(longFrom));

        Long longTo = this.getIntent().getExtras().getLong("to",0);
        setTo(Converters.fromTimestamp(longTo));

        setmType(this.getIntent().getExtras().getInt("type",0));


        Log.d(TAG,"cat = " + this.cat);
        Log.d(TAG,"type = " + this.mType);

        SearchFragment searchFragment = new SearchFragment();

        switch(this.mType){
            case 0:
                setTodos(ToDoManager.getInstance(mContext).searchToDoByCategory(this.cat));break;
            case 1:
                setTodos(ToDoManager.getInstance(mContext).searchToDoByDate(this.from, this.to)); break;
            case 2:
                setTodos(ToDoManager.getInstance(mContext).searchToDoByDueDate(this.from, this.to)); break;
            default:
                setTodos(ToDoManager.getInstance(mContext).getToDoList());
                Toast.makeText(mContext, "There was an error filtering your ToDo!", Toast.LENGTH_SHORT).show(); break;
        }
        searchFragment.setToDoList(this.todos);
        getSupportFragmentManager().beginTransaction().add(R.id.container, searchFragment).commit();
    }

    private void setupToolBar(){
        //ToolBar and ActionBar Settings
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                            //gestione del menu, intercetto il "click" (gestisco le azioni)
        if(item.getItemId() == R.id.action_export){
            exportToDo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportToDo() {
        try{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(DOCUMENT_APPLICATION_TYPE);
                intent.putExtra(Intent.EXTRA_TITLE, EXTERNAL_DOCUMENT_FILENAME);

                startActivityForResult(intent, EXTERNAL_FILE_CREATE_REQUEST_ID);
            }else {
                Toast.makeText(getApplicationContext(), "Function not available !", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXTERNAL_FILE_CREATE_REQUEST_ID) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null && data.getData() != null) {
                        ToDoManager.getInstance(getApplicationContext()).exportOnSharedDocument(data.getData(), this.todos);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

}