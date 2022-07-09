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
import java.io.IOException;
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
        ImageModel next = new ImageModel();

        if (current != null) {
            while (true) {
                try {
                    next = findNext(current, context);
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
            next = dbHelper.findFirst();
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
        return next;
    }
}