package com.example.nustywallpapers;

import android.os.Build;
import android.os.FileObserver;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.List;

public class FolderHandler extends FileObserver {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public FolderHandler(File file, int mask) {
        super(file, mask);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public FolderHandler(File file) {
        super(file);
    }

    public FolderHandler(String file, int mask) {
        super(file, mask);
    }

    public FolderHandler(String file) {
        super(file);
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        String TAG = "IMAGE";
        if (event == FileObserver.CREATE) {
            Log.d(TAG, "NEW");
        } else if (event == FileObserver.DELETE) {
            Log.d(TAG, "DELETE");
        } else if (event == FileObserver.DELETE_SELF) {
            Log.d(TAG, "DELETE_SELF");
        }else if (event == FileObserver.MODIFY) {
            Log.d(TAG, "MODIFY");
        }
    }
}
