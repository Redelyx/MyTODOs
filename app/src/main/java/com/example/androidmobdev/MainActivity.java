package com.example.androidmobdev;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mContext = null;
    public static String TAG = "MainActivity";
    private AppSectionsPagerAdapter mAppSectionsPagerAdapter = null;
    private ViewPager mViewPager = null;
    private TabLayout tabLayout = null;

    private Long from = null;
    private Long to = null;

    public void setFrom(Long from) {
        this.from = from;
    }
    public void setTo(Long to) {
        this.to = to;
    }
    public Long getFrom() {
        return from;
    }
    public Long getTo() {
        return to;
    }

    private static final String EXTERNAL_DOCUMENT_FILENAME = "todos.csv";
    private static final String DOCUMENT_APPLICATION_TYPE = "text/csv";
    private static final int EXTERNAL_FILE_CREATE_REQUEST_ID = 1818;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate() called !");

        this.mContext = this;

        setupToolBar();
        setupViewPager();
        setupTabLayout();

        createNotificationChannel();

        setAlarm();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
               //do nothing to avoid inconsistency due to activities with old ToDo
                //Toast.makeText(mContext, "Nothing else to see!", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setAlarm() {
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 1000*10,
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    private void createNotificationChannel() {
        //Because you must create the notification channel before posting any
        //notifications on Android 8.0 and higher, you should execute this code as soon as
        //your app starts. It's safe to call this repeatedly because creating an existing
        //notification channel performs no operation
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel("notifyTODO", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            getSystemService((NotificationManager.class)).createNotificationChannel(channel);
        }
    }

    private void setupToolBar(){
        //ToolBar and ActionBar Settings
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    private void setupViewPager(){

        mAppSectionsPagerAdapter  = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager  = (ViewPager)findViewById(R.id.container);

        mViewPager.setAdapter(mAppSectionsPagerAdapter);

    }

    private void setupTabLayout(){
        //TabLayout with ViewPager
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                  //passo l 'oggetto menu vuoto da popolare e uso inflate per popolarlo (associo il menu)
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                            //gestione del menu, intercetto il "click" (gestisco le azioni)
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       switch(item.getItemId()){
           case R.id.action_info:
               openInfoActivity();
               return true;
           case R.id.action_filter:
               filter();
               return true;
           case R.id.action_search:
               openSearchActivity();
               return true;
           case R.id.action_export:
               exportToDo();
               return true;
           case R.id.check_alarm:
               checkAlarm();
               return true;
       }
        return super.onOptionsItemSelected(item);
    }

    private void checkAlarm() {
        Toast.makeText(mContext, "Alarms checked!",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long timeAtButtonClick = System.currentTimeMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick+1000, pendingIntent);
    }

    private void openInfoActivity(){
        Log.d(TAG,"openInfoActivity() called !");
        startActivity(new Intent(this,InfoActivity.class));
    }

    private void openSearchActivity(){
        Log.d(TAG,"openSearchActivity() called !");
        startActivity(new Intent(this,SearchActivity.class));
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

    private void filter(){
        AlertDialog.Builder dialog = new AlertDialog.Builder((mContext));
        View v = getLayoutInflater().inflate(R.layout.pick_filter, null);

        Button filterCategoryButton = (Button)v.findViewById(R.id.filter_category);
        Button filterDateButton = (Button)v.findViewById(R.id.filter_date);
        Button filterDueDateButton = (Button)v.findViewById(R.id.filter_duedate);

        Button cancelButton = (Button)v.findViewById(R.id.dialog_cancel);

        dialog.setView(v);

        final AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Filter by:");

        filterCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });

        filterDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });

        filterDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dueDateDialog();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void categoryDialog(){
        final AlertDialog.Builder categoryDialog = new AlertDialog.Builder(mContext);
        View v1 = getLayoutInflater().inflate(R.layout.category_dialog, null);

        final Spinner categoryInput = (Spinner) v1.findViewById(R.id.category_input);
        Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
        Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);


        categoryDialog.setView(v1);

        final AlertDialog alertDialog1 = categoryDialog.create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.setTitle("Filter by category:");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = categoryInput.getSelectedItem().toString();

                Intent intent = new Intent(mContext, FilterActivity.class);

                intent.putExtra("cat", category);
                intent.putExtra("type", 0);
                Log.d(TAG,"cat = " + category);


                mContext.startActivity(intent);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        alertDialog1.show();
    }

    private void dateDialog(){
        final AlertDialog.Builder dateDialog = new AlertDialog.Builder(mContext);
        View v1 = getLayoutInflater().inflate(R.layout.range_picker, null);

        Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
        Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);
        Button fromButton =  (Button)v1.findViewById(R.id.from_input);
        Button toButton = (Button)v1.findViewById(R.id.to_input);

        final TextView fromResult = (TextView)v1.findViewById(R.id.from);
        final TextView toResult = (TextView)v1.findViewById(R.id.to);

        dateDialog.setView(v1);

        final AlertDialog alertDialog1 = dateDialog.create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.setTitle("Filter by date:");

        fromButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog.Builder dateDialog = new AlertDialog.Builder(mContext);
                 View v1 = getLayoutInflater().inflate(R.layout.date_dialog, null);

                 Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
                 Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);
                 final DatePicker dateInput = (DatePicker) v1.findViewById(R.id.date_input);

                 dateDialog.setView(v1);

                 final AlertDialog alertDialog1 = dateDialog.create();
                 alertDialog1.setCanceledOnTouchOutside(false);
                 alertDialog1.setTitle("From:");

                 okButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         int month = dateInput.getMonth()+1;
                         final String strDate = dateInput.getYear() +  "-" + month + "-" + dateInput.getDayOfMonth() ;
                         setFrom(Utilities.dateToTimestamp(Utilities.fromString(strDate)));
                         fromResult.setText(strDate);
                         alertDialog1.dismiss();
                     }
                 });

                 cancelButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         alertDialog1.dismiss();
                     }
                 });

                 alertDialog1.show();
             }
         });

        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dateDialog = new AlertDialog.Builder(mContext);
                View v1 = getLayoutInflater().inflate(R.layout.date_dialog, null);

                Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
                Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);
                final DatePicker dateInput = (DatePicker) v1.findViewById(R.id.date_input);
                dateInput.setMinDate(getFrom());

                dateDialog.setView(v1);

                final AlertDialog alertDialog1 = dateDialog.create();
                alertDialog1.setCanceledOnTouchOutside(false);
                alertDialog1.setTitle("To:");

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int month = dateInput.getMonth()+1;
                        final String strDate = dateInput.getYear() +  "-" + month + "-" + dateInput.getDayOfMonth() ;
                        setTo(Utilities.dateToTimestamp(Utilities.fromString(strDate)));
                        toResult.setText(strDate);
                        alertDialog1.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });

                alertDialog1.show();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FilterActivity.class);

                intent.putExtra("from", getFrom());
                intent.putExtra("to", getTo());
                intent.putExtra("type", 1);

                mContext.startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        alertDialog1.show();
    }

    private void dueDateDialog(){
        final AlertDialog.Builder dueDateDialog = new AlertDialog.Builder(mContext);
        View v1 = getLayoutInflater().inflate(R.layout.range_picker, null);

        Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
        Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);
        Button fromButton =  (Button)v1.findViewById(R.id.from_input);
        Button toButton = (Button)v1.findViewById(R.id.to_input);

        final TextView fromResult = (TextView)v1.findViewById(R.id.from);
        final TextView toResult = (TextView)v1.findViewById(R.id.to);

        dueDateDialog.setView(v1);

        final AlertDialog alertDialog1 = dueDateDialog.create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.setTitle("Filter by due date:");

        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dateDialog = new AlertDialog.Builder(mContext);
                View v1 = getLayoutInflater().inflate(R.layout.date_dialog, null);

                Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
                Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);
                final DatePicker dateInput = (DatePicker) v1.findViewById(R.id.date_input);

                dateDialog.setView(v1);

                final AlertDialog alertDialog1 = dateDialog.create();
                alertDialog1.setCanceledOnTouchOutside(false);
                alertDialog1.setTitle("From:");

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int month = dateInput.getMonth()+1;
                        final String strDate = dateInput.getYear() +  "-" + month + "-" + dateInput.getDayOfMonth() ;
                        setFrom(Utilities.dateToTimestamp(Utilities.fromString(strDate)));
                        fromResult.setText(strDate);
                        alertDialog1.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });

                alertDialog1.show();
            }
        });

        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dateDialog = new AlertDialog.Builder(mContext);
                View v1 = getLayoutInflater().inflate(R.layout.date_dialog, null);

                Button okButton = (Button)v1.findViewById(R.id.dialog_ok);
                Button cancelButton = (Button)v1.findViewById(R.id.dialog_cancel);
                final DatePicker dateInput = (DatePicker) v1.findViewById(R.id.date_input);
                dateInput.setMinDate(getFrom());

                dateDialog.setView(v1);

                final AlertDialog alertDialog1 = dateDialog.create();
                alertDialog1.setCanceledOnTouchOutside(false);
                alertDialog1.setTitle("To:");

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int month = dateInput.getMonth()+1;
                        final String strDate = dateInput.getYear() +  "-" + month + "-" + dateInput.getDayOfMonth() ;
                        setTo(Utilities.dateToTimestamp(Utilities.fromString(strDate)));
                        toResult.setText(strDate);
                        alertDialog1.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });

                alertDialog1.show();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FilterActivity.class);

                intent.putExtra("from", getFrom());
                intent.putExtra("to", getTo());
                intent.putExtra("type", 2);

                mContext.startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        alertDialog1.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXTERNAL_FILE_CREATE_REQUEST_ID) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null && data.getData() != null) {
                        int type = mViewPager.getCurrentItem();
                        List<ToDo> todos;
                        switch(type){
                            case 1:
                                todos = ToDoManager.getInstance(this).getYetToDoList();break;
                            case 2:
                                todos = ToDoManager.getInstance(this).getDoneList();break;
                            default:
                                todos = ToDoManager.getInstance(this).getToDoList(); break;
                        }
                        ToDoManager.getInstance(getApplicationContext()).exportOnSharedDocument(data.getData(), todos);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }
}
