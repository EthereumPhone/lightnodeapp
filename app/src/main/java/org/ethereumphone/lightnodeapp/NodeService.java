package org.ethereumphone.lightnodeapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import geth.Context;
import geth.Geth;
import geth.Node;
import geth.NodeConfig;

public class NodeService extends Service {
    Node node;
    Context ctx;
    NodeConfig nodeConfig;
    public static final String CHANNEL_ID = "NodeService";

    @Override
    public void onCreate() {
        super.onCreate();
        nodeConfig = Geth.newNodeConfig();
        Geth.setVerbosity(4);
        ctx = Geth.newContext();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Light Node")
                .setContentText(input)
                //.setSmallIcon(R.drawable.)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        try {
            node = Geth.newNode(getFilesDir()+"/.ethNode", nodeConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            node.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            node.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
