package com.example.navermappractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainButtonFragment extends Fragment{
    public static MainButtonFragment newinstance(){
        return new MainButtonFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (ViewGroup)inflater.inflate(R.layout.fragment_mainbutton,container,false);
        return view;

    }
}
