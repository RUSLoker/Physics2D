package com.example.physics2d;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class LimitSetter extends Fragment implements View.OnClickListener {

    String[] strings;
    int[] defaults;
    boolean ft = true;

    public LimitSetter() {
        // Required empty public constructor
    }

    public static LimitSetter newInstance() {
        return new LimitSetter();
    }

    @SuppressLint("SetTextI18n")
    public void changeCase(int id){
        View view = getView();
        TextView text = view.findViewById(R.id.text);
        EditText value = view.findViewById(R.id.value);
        text.setText(strings[id]);
        value.setText(Integer.toString(defaults[id]));
        if(ft){
            view.findViewById(R.id.m10).setOnClickListener(this);
            view.findViewById(R.id.m5).setOnClickListener(this);
            view.findViewById(R.id.m1).setOnClickListener(this);
            view.findViewById(R.id.p1).setOnClickListener(this);
            view.findViewById(R.id.p5).setOnClickListener(this);
            view.findViewById(R.id.p10).setOnClickListener(this);
            ft = false;
        }
    }

    public int getLimit(){
        View view = getView();
        EditText value = view.findViewById(R.id.value);
        String str = value.getText().toString();
        return Integer.parseInt(str);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strings = getResources().getStringArray(R.array.gameModes);
        defaults = getResources().getIntArray(R.array.defaultValues);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_limit_setter, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        View view = getView();
        EditText valueView = view.findViewById(R.id.value);
        int value = getLimit();
        switch (id){
            case R.id.m10:
                value += -10;
                break;
            case R.id.m5:
                value += -5;
                break;
            case R.id.m1:
                value += -1;
                break;
            case R.id.p1:
                value += 1;
                break;
            case R.id.p5:
                value += 5;
                break;
            case R.id.p10:
                value += 10;
                break;
        }
        if(value < 0) value = 0;
        if(value > 999) value = 999;
        valueView.setText(Integer.toString(value));
    }
}