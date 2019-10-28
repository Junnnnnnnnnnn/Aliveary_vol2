package com.example.navermappractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;

public class WriteFragment extends Fragment {

    MapFragment mapFragment;
    MarkerButtonFragment MBF;
    Button buttonWriteSave;
    CharSequence writeTextToCS;
    EditText textArea;
    public static WriteFragment newInstance(){
        return new WriteFragment();
    }
    @Nullable
    @Override
    public  View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);

//       EditText writeText = view.findViewById(R.id.);

        buttonWriteSave = (Button)view.findViewById(R.id.write_Save);
        buttonWriteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }
    public CharSequence getWriteText(){
        return writeTextToCS;
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_fragment, fragment).commit();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    private void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
