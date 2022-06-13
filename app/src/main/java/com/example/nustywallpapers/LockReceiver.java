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
        Log.d("TIMER", "BROADCAST RECEIVED AT "+ new Date().getTime());
        String path = PathHandler.loadValue(context, "path");
        String current = PathHandler.loadValue(context, "current");
        boolean next = false, first = true;
        WallpaperHandler wh = new WallpaperHandler(context);

        String uri = PathHandler.loadValue(context, "uri");
        if (uri.equals("-1")) {
            Log.e("URI ERROR", "SAVED URI INVALID");
        } else {
            
            Uri data = Uri.parse(uri);
            DocumentFile directory = DocumentFile.fromTreeUri(context, data);
            DocumentFile[] files = directory.listFiles();
            DocumentFile firstFile = null;
            for (DocumentFile f : files) {
                if (f.isFile()) {
                    if (f.getType().matches("(^)image(.*)")) {
                        if (first) {
                            firstFile = f;
                            first = false;
                        }

                        if (current.equals("-1")) {
                            int n = wh.setLockWall(f.getUri());
                            break;
                        }

                        if (!next && f.equals(files[files.length - 1])) {
                            Log.d("TIMER", "LAST");
                            int n = wh.setLockWall(firstFile.getUri());
                            break;
                        }

                        if (current.equals(f.getUri().toString())) {
                            next = true;
                            continue;
                        }


                        if (next) {
                            int n = wh.setLockWall(f.getUri());
                            next = false;
                            break;
                        }
                    }
                }
            }


        }

    }
}