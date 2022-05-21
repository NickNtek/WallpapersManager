package com.example.nustywallpapers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

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

    public static String pathConcat(List<String> list) {
        String path = "/storage/";
        for (String item : list){
            if (item.matches("(.*):(.*)")) {
                String[] items = item.split(":");
                if( items[0].equals("primary")) {
                    path+="emulated/0/"+items[1];
                } else {
                    path+=items[0]+"/"+items[1];
                }
            }
        }

        return path;
    }
}
