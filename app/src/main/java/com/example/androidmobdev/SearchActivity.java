package com.example.androidmobdev;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public static String TAG = "SearchActivity";

    private Context mContext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity); //imposto il layout
        Log.d(TAG,"onCreate() called !");

        this.mContext = this;

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
        getMenuInflater().inflate(R.menu.search, menu);

        // Initialise menu item search_fragment bar
        // with id and take its object
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // attach setOnQueryTextListener
        // to search_fragment view defined above
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {

                        // If the list contains the search_fragment query
                        // than filter the adapter
                        // using the filter method
                        // with the query as its argument

                        List<ToDo> result = ToDoManager.getInstance(mContext).searchToDo(query);
                        if (result.size()>0) {
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setToDoList(result);
                            getSupportFragmentManager().beginTransaction().add(R.id.container, searchFragment).commit();
                        }
                        else {
                            // Search query not found in List View
                            Utilities.showSnackBar(SearchActivity.this, "Not found!");
                            //Toast.makeText(SearchActivity.this,"Not found",Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search_fragment query
                    // when the user is typing search_fragment
                    @Override
                    public boolean onQueryTextChange(String newQuery)
                    {
                        SearchFragment searchFragment1 = new SearchFragment();
                        searchFragment1.setToDoList(ToDoManager.getInstance(mContext).searchToDo(newQuery));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment1).commit();
                        return false;
                    }
                });
        return true;
    }

}