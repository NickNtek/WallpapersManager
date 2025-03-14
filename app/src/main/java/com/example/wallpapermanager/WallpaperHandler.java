package com.example.wallpapermanager;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.Date;

public class WallpaperHandler {

    private Context context;
    private WallpaperManager wp;
    public static final String LOCK_CHECKBOX_KEY = "lockScreen";
    public static final String WIDTH = "screen_width";
    public static final String HEIGHT = "screen_height";

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
    public int setLockWall(Uri next) throws IOException {
        boolean lock =PathHandler.loadValue(context, LOCK_CHECKBOX_KEY).equals("1");
        int screenFlag;
        if (lock) {
            screenFlag = WallpaperManager.FLAG_LOCK;
        } else {
            screenFlag = WallpaperManager.FLAG_SYSTEM;
        }

            int screenHeight = Integer.parseInt(PathHandler.loadValue(context, HEIGHT));
            int screenWidth = Integer.parseInt(PathHandler.loadValue(context, WIDTH));

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), next);

            int imageWidth = bitmap.getWidth();
            int imageHeight = bitmap.getHeight();

            int top = (imageHeight-screenHeight)/2;
            int bottom = (imageHeight+screenHeight)/2;
            int left = (imageWidth-screenWidth)/2;
            int right = (imageWidth+screenWidth)/2;

            if (top<0){
                top = 0;
            }

            if (left<0){
                left = 0;
            }

            if (bottom>screenHeight){
                bottom = screenHeight;
            }

            if (right>screenWidth) {
                right = screenWidth;
            }

            if (left + right > bitmap.getWidth()) {
                right = bitmap.getWidth();
            }

            if (top + bottom > bitmap.getHeight()) {
                bottom = bitmap.getHeight();
            }

            //Bitmap wallpaper = Bitmap.createScaledBitmap(bitmap, left, top, true, );
            wp.setBitmap(bitmap, null, false, screenFlag);
            PathHandler.saveValue(context, "current", next.toString());
            Log.d("TIMER", "WALLPAPER CHANGED AFTER "+ new Date().getTime());
            return 1;


    }


}
