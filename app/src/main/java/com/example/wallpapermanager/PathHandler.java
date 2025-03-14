package com.example.wallpapermanager;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class PathHandler {

    //save path when activity closes
    public static void saveValue(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(key, value);
        editor.apply();
    }


    //load path when activity resumes
    public static String loadValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("MySharedPrefs", MODE_PRIVATE);

        String value = sp.getString(key, "-1");
        return value;
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
