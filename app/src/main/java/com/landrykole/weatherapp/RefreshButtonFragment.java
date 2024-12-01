package com.landrykole.weatherapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class RefreshButtonFragment extends Fragment {

    private OnRefreshButtonClickListener callback;

    public interface OnRefreshButtonClickListener {
        void onRefreshButtonClick();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnRefreshButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
            + " must implement OnRefreshButtonClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_refresh_button, container, false);
        Button refreshButton = view.findViewById(R.id.button_refresh);
        refreshButton.setOnClickListener(v -> callback.onRefreshButtonClick());
        return view;
    }

}