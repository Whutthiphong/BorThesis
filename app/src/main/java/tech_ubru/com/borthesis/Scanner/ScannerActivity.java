package tech_ubru.com.borthesis.Scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import tech_ubru.com.borthesis.Details.ThesisDetail;
import tech_ubru.com.borthesis.R;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView zXingScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  zXingScannerView =new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }



    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
//        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(ScannerActivity.this, ThesisDetail.class);
        intent.putExtra("qrcode", result.getText().trim());

        startActivity(intent);
        zXingScannerView.resumeCameraPreview(this);

    }
}
