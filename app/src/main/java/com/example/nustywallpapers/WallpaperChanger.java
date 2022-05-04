package com.example.nustywallpapers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class WallpaperChanger extends Service {
    //TODO: GET LIST OF IMAGES
    ArrayList<File> files;
    //TODO: CHANGE WALLPAPER ON LOCK SCREEN

    public WallpaperChanger() {
    }

    @Override
    public void onCreate() {
        files = new ArrayList<>();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int length = intent.getIntExtra("length", -1);
        if (length < 0) {
            Toast.makeText(this, "image files didn't reach service", Toast.LENGTH_SHORT).show();
            stopSelf();
        } else {
            for (int i = 0; i<length; i++){
                files.add(i, (File) intent.getExtras().get("image["+i+"]"));
                Log.d("Image", files.get(i).getName());
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Closed", Toast.LENGTH_SHORT).show();
    }
}