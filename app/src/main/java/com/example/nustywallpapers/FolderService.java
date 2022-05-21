package com.example.nustywallpapers;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;

public class FolderService extends Service {
    public FolderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        File directory = new File(Environment.getExternalStorageDirectory().toString());
        FolderHandler fh;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            fh = new FolderHandler(directory,
                    FileObserver.CREATE |
                    FileObserver.DELETE |
                    FileObserver.DELETE_SELF |
                    FileObserver.MODIFY |
                    FileObserver.MOVE_SELF
            );
        } else {
            fh = new FolderHandler(Environment.getExternalStorageDirectory().toString(),
                FileObserver.CREATE |
                    FileObserver.DELETE |
                    FileObserver.DELETE_SELF |
                    FileObserver.MODIFY |
                    FileObserver.MOVE_SELF
            );
        }
        fh.startWatching();
        return START_STICKY;
    }
}