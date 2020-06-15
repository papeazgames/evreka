package com.sarslan.demos.evreka.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.sarslan.demos.evreka.R;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }
    protected boolean checkIfNetworkConnected(){
        if(!isNetworkConnected()){
            Toast.makeText(getApplicationContext(), R.string.network_err, Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }
}
