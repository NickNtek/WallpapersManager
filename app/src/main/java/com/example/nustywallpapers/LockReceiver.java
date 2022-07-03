package com.example.nustywallpapers;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LockReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        ImageDbHelper dbHelper = new ImageDbHelper(context);
        WallpaperHandler wh = new WallpaperHandler(context);

        ImageModel current = dbHelper.findCurrent();
        if (current != null) {
            int id = current.getId();
            ImageModel next;
            do {
                id++;
                next = dbHelper.findById(id);
            } while (next == null);
            String uri = next.getPath()+"%2F"+next.getName();
            wh.setLockWall(Uri.parse(uri));
            dbHelper.updateCurrent(next, true);
            dbHelper.updateCurrent(current, false);
        } else {
            Log.e("RECEIVER", "cannot find current wallpaper");
            ImageModel first = dbHelper.findFirst();
            if (first != null) {
                String uri = first.getPath()+"%2F"+first.getName();
                wh.setLockWall(Uri.parse(uri));
                dbHelper.updateCurrent(first, true);
            } else {
                Log.e("RECEIVER", "cannot find first image");
            }

        }

    }
}