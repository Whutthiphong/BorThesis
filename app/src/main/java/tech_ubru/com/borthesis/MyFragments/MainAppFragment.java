package tech_ubru.com.borthesis.MyFragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.R;
import tech_ubru.com.borthesis.Scanner.ScannerActivity;

import static android.Manifest.permission.CAMERA;

public class MainAppFragment extends Fragment    {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_app,container,false);
        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().hide();
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
