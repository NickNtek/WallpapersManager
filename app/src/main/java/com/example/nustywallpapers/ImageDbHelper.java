package com.example.nustywallpapers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ImageDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "WALLPAPERS";
    public static final int VERSION = 1;
    public static final String KEY_ID = "id";
    public static final String PATH = "path";
    public static final String NAME = "img_name";
    public static final String HASH = "image_hash_code";
    public static final String CURRENT = "current_wallpaper";

    public ImageDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+DB_NAME+" ("
                    +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    +PATH+" TEXT NOT NULL,"
                    +NAME+" TEXT NOT NULL,"
                    +HASH+" TEXT NOT NULL,"
                    +CURRENT+" BOOLEAN NOT NULL"
                +");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(ImageModel imageModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PATH, imageModel.getPath());
        values.put(NAME, imageModel.getName());
        values.put(HASH, imageModel.getHash());
        values.put(CURRENT, imageModel.isCurrent());

        return sqLiteDatabase.insert(DB_NAME, null, values);
    }

    public long deleteAll() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return  sqLiteDatabase.delete(DB_NAME, null, null);
    }

    public ArrayList<ImageModel> getAll() {
        ArrayList<ImageModel> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String path = cursor.getString(1);
                String name = cursor.getString(2);
                int hash = cursor.getInt(3);
                int current = cursor.getInt(4);
                boolean currentFlag;
                if ( current == 1 ){
                    currentFlag = true;
                } else {
                    currentFlag = false;
                }
                ImageModel image = new ImageModel(id, path, name, hash, currentFlag);
                images.add(image);
            } while (cursor.moveToNext());
        }
        return images;
    }
}
