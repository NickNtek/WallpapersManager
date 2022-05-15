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

        SharedPreferences sp = context.getSharedPreferences("MySharedPrefs", context.MODE_PRIVATE);
        String path = sp.getString("path", "-1");
        Log.d("PATH", "PATH IS: "+path);
    }
}