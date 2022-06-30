package com.example.nustywallpapers;

import static android.content.Context.MODE_PRIVATE;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class WallpaperHandler {

    private Context context;
    private WallpaperManager wp;
    public static final String LOCK_CHECKBOX_KEY = "lockScreen";


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public WallpaperHandler(Context context) {
        this.context = context;
        this.wp =  WallpaperManager.getInstance(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int setLockWall(Uri next) {
        boolean lock =PathHandler.loadValue(context, LOCK_CHECKBOX_KEY).equals("1");
        int screenFlag;
        if (lock) {
            screenFlag = WallpaperManager.FLAG_LOCK;
        } else {
            screenFlag = WallpaperManager.FLAG_SYSTEM;
        }
        try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), next);
            wp.setBitmap(bitmap, null, false, screenFlag);
            PathHandler.saveValue(context, "current", next.toString());
            Log.d("TIMER", "WALLPAPER CHANGED AFTER "+ new Date().getTime());
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
