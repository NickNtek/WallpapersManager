package com.example.nustywallpapers;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private String path = null;
    public static final String GRANTED = "Permission Granted";
    public static final String DENIED = "Permission Denied";
    TextView pathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pathView = findViewById(R.id.path_view);

        if (path == null) {
            path = Environment.getExternalStorageDirectory().getPath();
            path += "/Pictures";
            pathView.setText(path);
        }


        //GET FILE PERMISSIONS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            BreadToast(GRANTED);
        } else {
            resultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            //BreadToast(DENIED);
        }

        //TODO: ACCESS FOLDER WITH IMAGES (STATIC FOLDER)
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
/*
       for (File f : files) {
            Log.d("Files", "FileName: "+f.getName());
        }
*/



        //TODO: IMPLEMENT SERVICE - NOT SERVICE BUT BROADCAST RECEIVER?
        Intent intent = new Intent(this, WallpaperChanger.class);
        for (int i = 0; i < files.length; i++) {
            intent.putExtra("image["+i+"]", files[i]);
        }
        intent.putExtra("length", files.length);
        startService(intent);

        //BROADCAST RECEIVER
        BroadcastReceiver br = new ScreenLockReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        this.registerReceiver(br, intentFilter);
        intent.setAction("com.example.nustywallpapers");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


        //TODO: CHANGE SAID FOLDER DEPENDING ON FIELD (DO I NEED TO SAVE IT IN A DB? ANY OTHER WAY TO SAVE IT?)

        //TODO: MAKE APP RUN ALL THE TIME FROM STARTUP


    }


    //TODO: DIRECTORY PICKER
    public void directoryPick(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        Uri treeUri = data.getData();
        if (treeUri == null) {
            Log.e("ERROR", "No Data Passed On Document Picker");
            throw new NullPointerException();
        }

        // get list of files
        DocumentFile directory = DocumentFile.fromTreeUri(this, treeUri);
        DocumentFile[] files = directory.listFiles();

        //get path
        Uri directoryUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri));
        path = PathHandler.getPath(this, directoryUri);
        savePath();

    }



    //GET FILE PERMISSIONS
    private ActivityResultLauncher<String> resultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    BreadToast(GRANTED);
                } else {
                    BreadToast(DENIED);
                }
            });

    private void BreadToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPath();
        pathView.setText(path);
        BreadToast(pathView.getText().toString());
    }

    private void loadPath() {
        SharedPreferences sp = getSharedPreferences("pathPrefs", Context.MODE_PRIVATE);
        path = sp.getString("path", path);
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePath();
    }

    private void savePath() {
        SharedPreferences sp = getSharedPreferences("pathPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("path", path);
        editor.commit();
    }


}