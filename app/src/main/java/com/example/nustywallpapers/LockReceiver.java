package com.example.nustywallpapers;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String path = PathHandler.loadPath(context);
        Log.d("PATH", "PATH IS: "+path);

        List<DocumentFile> wallpapers = new ArrayList<>();
        //TODO: GET SAVED PATH.

        String uri = PathHandler.loadUri(context);
        if (uri.equals("-1")) {
            Log.e("URI ERROR", "SAVED URI INVALID");
        } else {
            Uri data = Uri.parse(uri);
            DocumentFile directory = DocumentFile.fromTreeUri(context, data);
            DocumentFile[] files = directory.listFiles();
            for (DocumentFile f : files) {
                if (f.isFile()) {
                    Log.d("TYPE", f.getType());
                    if (f.getType().matches("(^)image")) {
                        wallpapers.add(f);
                    }
                }
            }

        }


        //TODO: GET LIST OF FILES IN PATH. (SAVE I LIST)
        //TODO: FIND CURRENT IMAGE (SAVED IN PREFERENCES)
        //TODO: CALL WALLPAPER HANDLER SERVICE WITH INDEX
    }
}