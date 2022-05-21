package com.example.nustywallpapers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

public class LockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String path = PathHandler.loadPath(context);
        Log.d("PATH", "PATH IS: "+path);
        //TODO: GET SAVED PATH.
        //TODO: GET LIST OF FILES IN PATH. (SAVE I LIST)
        //TODO: FIND CURRENT IMAGE (SAVED IN PREFERENCES)
        //TODO: CALL WALLPAPER HANDLER SERVICE WITH INDEX
    }
}