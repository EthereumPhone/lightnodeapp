package org.ethereumphone.lightnodeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclickNode(View view) {
        startService();
    }

    public void onClickDestroy(View view) {
        stopService();
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, NodeService.class);
        serviceIntent.putExtra("inputExtra", "Geth Node running in background");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, NodeService.class);
        stopService(serviceIntent);
    }

}