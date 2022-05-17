package com.example.nustywallpapers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class PathHandler {

    //save path when activity closes
    public static void savePath(Context context, String key, String path) {
        SharedPreferences sp = context.getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(key, path);
        editor.apply();
    }


    //load path when activity resumes
    public static String loadPath(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MySharedPrefs", MODE_PRIVATE);

        String path = sp.getString("path", "-1");
        return path;
    }
}
