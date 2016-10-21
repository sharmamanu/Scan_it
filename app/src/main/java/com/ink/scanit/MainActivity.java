package com.ink.scanit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final int URL = 1;

    private Button mScanCodeButton;

    private TextView mScannedValueTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScanCodeButton = (Button) findViewById(R.id.scan_it_button);
        mScannedValueTV = (TextView) findViewById(R.id.scanned_value);

        mScanCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan(); // `this` is the current Activity
            }
        });
    }

    boolean checkCameraPermission() {

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                doAction(filterResult(result.getContents()),result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    int filterResult(String result){
        if(result.contains("http")){
            return URL;
        }else {
            return 2;
        }
//        return -1;
    }

    void doAction(int resultCode,String resultString){
        switch (resultCode){
            case URL:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(resultString));
                // Create and start the chooser
                Intent chooser = Intent.createChooser(i, "Open with");
                startActivity(i);
                break;
            case 2:
                mScannedValueTV.setText(resultString);
                break;
            default:
                Toast.makeText(this, "Bar code type unknown.Cannot complete action.", Toast.LENGTH_SHORT).show();
        }
    }
}
