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
import java.util.List;

public class LockReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String path = PathHandler.loadValue(context, "path");
        String current = PathHandler.loadValue(context, "current");
        boolean next = false;
        WallpaperHandler wh = new WallpaperHandler(context);
        Log.d("PATH", "PATH IS: "+path);
        //TODO: GET SAVED PATH.

        String uri = PathHandler.loadValue(context, "uri");
        if (uri.equals("-1")) {
            Log.e("URI ERROR", "SAVED URI INVALID");
        } else {
            Uri data = Uri.parse(uri);
            DocumentFile directory = DocumentFile.fromTreeUri(context, data);
            DocumentFile[] files = directory.listFiles();
            for (DocumentFile f : files) {
                if (f.isFile()) {
                    Log.d("TYPE", f.getType());
                    if (f.getType().matches("(^)image(.*)")) {
                        Log.d("NAME", f.getName());
                        Log.d("URI", f.getUri().toString());
                        if (current.equals("-1")) {
                            int n = wh.setLockWall(f.getUri());
                            break;
                        }

                        if (next) {
                            int n = wh.setLockWall(f.getUri());
                            next = false;
                            break;
                        }
String string = f.getUri().toString();
                        if (current.equals(f.getUri().toString())) {
                            next = true;
                        }
                    }
                }
            }


        }


        //TODO: GET LIST OF FILES IN PATH. (SAVE I LIST)
        //TODO: FIND CURRENT IMAGE (SAVED IN PREFERENCES)
        //TODO: CALL WALLPAPER HANDLER SERVICE WITH INDEX
    }
}