package com.example.androidmobdev;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "Search Fragment";

    private List<ToDo> todos = null;
    private RecyclerView mRecyclerView = null;
    private LinearLayoutManager mLayoutManager = null;
    private MyAdapter mAdapter = null;

    public void setToDoList(List<ToDo> todos) {this.todos = todos;}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        init(rootView);

        return rootView;
    }
    private void init(View rootView){
        //recupero la recycler view tramite id
        mRecyclerView  = (RecyclerView)rootView.findViewById(R.id.my_search_recycler_view);                        //devo recuperare i componenti dalla vista e non dall'activity

        mLayoutManager  = new LinearLayoutManager(getActivity());                                           // use a linear layout manager
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);                                        //orientamento verticale
        mLayoutManager.scrollToPosition(0);                                                                 //parte dalla posizione 0

        mRecyclerView.setLayoutManager(mLayoutManager);                                                     //associo il layout che ho deciso alla recicler view

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //TODOLIST IS SPECIFIED IN THE ACTIVITY
        //specify an adapter
        mAdapter = new MyAdapter(this.todos, getActivity());

        mRecyclerView.setAdapter(mAdapter); //setto l'adapter sulla recyclerview
    }



}
