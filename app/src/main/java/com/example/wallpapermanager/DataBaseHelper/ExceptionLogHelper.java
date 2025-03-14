package com.example.wallpapermanager.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.wallpapermanager.Model.ExceptionModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class ExceptionLogHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "EXCEPTIONS";
    public static final int VERSION = 1;
    public static final String KEY_ID = "id";
    public static final String MESSAGE = "message";
    public static final String STACKTRACE = "stacktrace";
    public static final String LOGDATE = "logdate";
    public static final String CLASS_NAME = "class_name";

    public ExceptionLogHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+DB_NAME+" ("
                        +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        +MESSAGE+" TEXT NOT NULL,"
                        +STACKTRACE+" BLOB NOT NULL,"
                        +LOGDATE+" LONG NOT NULL,"
                        +CLASS_NAME+" TEXT NOT NULL"
                        +");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insert(ExceptionModel em) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MESSAGE, em.getMessage());
        values.put(STACKTRACE, em.getStacktrace());
        values.put(LOGDATE, em.getLogDate());
        values.put(CLASS_NAME, em.getClassName());

        return sqLiteDatabase.insert(DB_NAME, null, values);
    }

    public ArrayList<ExceptionModel> getAll() {
        ArrayList<ExceptionModel> exceptionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String message = cursor.getString(1);
                String stacktrace = cursor.getString(2);
                String logdate = cursor.getString(3);
                //long logdateLong = cursor.getInt(3);
                //Date logdate = new Date(logdateLong);

                String classname = cursor.getString(4);

                ExceptionModel em = new ExceptionModel(id, message, stacktrace, logdate, classname);
                exceptionList.add(em);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exceptionList;
    }

    public ExceptionModel getByID(int id) {
        ExceptionModel em = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_NAME, null, KEY_ID+"=?", new String[] {Integer.toString(id)}, null, null, null);
        if (cursor.moveToFirst()) {
                String message = cursor.getString(1);
                String stacktrace = cursor.getString(2);
                String logdate = cursor.getString(3);
                //long logdateLong = cursor.getInt(3);
                //Date logdate = new Date(logdateLong);

                String classname = cursor.getString(4);

                em = new ExceptionModel(id, message, stacktrace, logdate, classname );
        }
        cursor.close();
        db.close();
        return em;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ExceptionModel exceptionConverter(Exception e, String className) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ExceptionModel em = new ExceptionModel(e.getMessage(), Arrays.toString(e.getStackTrace()), dtf.format(now), className);
        return em;
    }

}
