package com.example.androidmobdev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    private static final String TAG = "Info Fragment";

    public InfoFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.info_fragment, container, false);

        return rootView;
    }

}

