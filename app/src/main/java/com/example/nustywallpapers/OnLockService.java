package com.example.nustywallpapers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Collections;


public class OnLockService extends Service {
    BroadcastReceiver broadcastReceiver;

    public static class OnLockReceiver extends BroadcastReceiver {


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.
            boolean random = PathHandler.loadValue(context, MainActivity.RANDOM_IMAGE_CHECKBOX_KEY).equals("1");

            ImageDbHelper dbHelper = new ImageDbHelper(context);
            WallpaperHandler wh = new WallpaperHandler(context);

            ImageModel current = dbHelper.findCurrent();
            ImageModel next = new ImageModel();

            if (current != null) {
                while (true) {
                    try {
                        if (random) {
                            next = findRandom(context);
                        } else {
                            next = findNext(current, context);
                        }
                        Uri uri = Uri.parse(next.getPath() + "%2F" + next.getName());
                        wh.setLockWall(uri);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        dbHelper.updateCurrent(current, false);
                        current=next;
                        int status = dbHelper.deleteById(next.getId());
                        String error = "Image with name \"" + next.getName() + "\" cannot be set as a wallpaper";
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                    } finally {
                        dbHelper.updateCurrent(current, false);
                        dbHelper.updateCurrent(next, true);
                    }
                }
            } else {
                Log.e("RECEIVER", "cannot find current wallpaper");
                if (random) {
                    next = findRandom(context);
                } else {
                    next = dbHelper.findFirst();
                }
                if (next != null) {
                    while (true) {
                        try {
                            String uri = next.getPath()+"%2F"+next.getName();
                            wh.setLockWall(Uri.parse(uri));
                            dbHelper.updateCurrent(next, true);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (next.isFirst() && next.isLast()){
                                String error = "You have only one image on your folder and cannot be processed by the application.";
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                break;
                            }
                            dbHelper.updateCurrent(next, false);
                            next =findNext(next, context);
                        }

                    }

                } else {
                    Log.e("RECEIVER", "cannot find first image");
                }

            }
            dbHelper.close();
        }

        public ImageModel findNext(ImageModel current, Context context) {
            ImageDbHelper dbHelper = new ImageDbHelper(context);
            ImageModel next;
            int id = current.getId();
            do {
                id++;
                next = dbHelper.findById(id);
                if (current.isLast()){
                    next = dbHelper.findFirst();
                    break;
                }
            } while ( next.getId() == -1);
            dbHelper.close();
            return next;
        }

        public ImageModel findRandom(Context context) {
            ImageDbHelper dbHelper = new ImageDbHelper(context);
            ImageModel next = dbHelper.findRandom();
            dbHelper.close();
            return next;
        }

        //constructor
        public OnLockReceiver() {

        }
    }

    public OnLockService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        broadcastReceiver = new OnLockReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(broadcastReceiver, intentFilter);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_MIN);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}