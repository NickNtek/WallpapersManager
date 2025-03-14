package com.example.wallpapermanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.wallpapermanager.DataBaseHelper.ExceptionLogHelper;
import com.example.wallpapermanager.DataBaseHelper.ImageDbHelper;
import com.example.wallpapermanager.Model.ExceptionModel;
import com.example.wallpapermanager.Model.ImageModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LockReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        ImageDbHelper dbHelper = new ImageDbHelper(context);
        ExceptionLogHelper errodDb = new ExceptionLogHelper(context);
        WallpaperHandler wh = new WallpaperHandler(context);

        ImageModel current = dbHelper.findCurrent();
        ImageModel next = new ImageModel();

        if (current != null) {
            while (true) {
                try {
                    next = findNext(current, context);
                    Uri uri = Uri.parse(next.getPath() + "%2F" + next.getName());
                    wh.setLockWall(uri);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    ExceptionModel em = errodDb.exceptionConverter(e, "LockReceiver");

                    dbHelper.updateCurrent(current, false);
                    current=next;
                    int status = dbHelper.deleteById(next.getId());
                    String error = "Image with name \"" + next.getName() + "\" cannot be set as a wallpaper";
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                    em.setMessage(error);
                    errodDb.insert(em);
                } finally {
                    dbHelper.updateCurrent(current, false);
                    dbHelper.updateCurrent(next, true);
                }
            }
        } else {
            Log.e("RECEIVER", "cannot find current wallpaper");
            next = dbHelper.findFirst();
            if (next != null) {
                while (true) {
                    try {
                        String uri = next.getPath()+"%2F"+next.getName();
                        wh.setLockWall(Uri.parse(uri));
                        dbHelper.updateCurrent(next, true);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (next.isFirst() && next.isLast()){
                            String error = "You have only one image on your folder and cannot be processed by the application.";
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                            ExceptionModel em = errodDb.exceptionConverter(e, "LockReceiver");
                            em.setMessage(error);
                            errodDb.insert(em);
                            break;
                        }
                        dbHelper.updateCurrent(next, false);
                        next =findNext(next, context);
                    }

                }

            } else {
                Log.e("RECEIVER", "cannot find first image");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                ExceptionModel em = new ExceptionModel("RECEIVER", "cannot find first image", dtf.format(now), "LockReceiver");
                errodDb.insert(em);
            }

        }

    }

    public ImageModel findNext(ImageModel current, Context context) {
        ImageDbHelper dbHelper = new ImageDbHelper(context);
        ImageModel next;
        int id = current.getId();
        do {
            id++;
            next = dbHelper.findById(id);
            if (current.isLast()){
                next = dbHelper.findFirst();
                break;
            }
        } while ( next.getId() == -1);
        return next;
    }
}