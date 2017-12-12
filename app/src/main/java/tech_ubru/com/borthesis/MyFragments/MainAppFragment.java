package tech_ubru.com.borthesis.MyFragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.R;
import tech_ubru.com.borthesis.Scanner.ScannerActivity;

public class MainAppFragment extends Fragment    {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_app,container,false);
        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff8c00")));


        Button btn_scanner = rootView.findViewById(R.id.btn_scanner);
        btn_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                startActivity(new Intent(getContext(), ScannerActivity.class));
            }
        });

        return rootView;
    }
}
