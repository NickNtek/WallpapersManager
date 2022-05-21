package com.example.nustywallpapers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String GRANTED = "Permission Granted";
    public static final String DENIED = "Permission Denied";

    TextView pathView;

    //TODO: 2 CONCAT PATH SEGMENTS TO MAKE PATH VIEW???

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pathView = findViewById(R.id.path_view);

        //GET FILE PERMISSIONS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            BreadToast(GRANTED);
        } else {
            resultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            //BreadToast(DENIED);
        }

        //start Receiver
        BroadcastReceiver br = new LockReceiver();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(br, intentFilter);

        pathView.setText(PathHandler.loadPath(this));

        if (pathView.getText().toString().matches("/storage/(.*)")) {
            Toast.makeText(this, "GOOD JOB", Toast.LENGTH_SHORT).show();
            serviceStarter();
        } else {
            Toast.makeText(this, "OH FUCK OFF YOU BITCH", Toast.LENGTH_SHORT).show();
        }
    }


    //GET FILE PERMISSIONS
    private ActivityResultLauncher<String> resultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    //BreadToast(GRANTED);
                    Log.d("Storage Permission", GRANTED);
                } else {
                    //BreadToast(DENIED);
                    Log.d("Storage Permission", DENIED);

                }
            });

    private void BreadToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    //open system folder picker
    public void directoryPick(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        getFolderUri.launch(intent);
    }

    // handle the result returned from folder picker
    ActivityResultLauncher<Intent> getFolderUri = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent i = result.getData();
                        Log.d("CODED_PATH", i.getData().getEncodedPath());
                        Log.d("PATH", i.getData().getPath());
                        Log.d("DATA", i.getData().toString());
                        List<String> list = i.getData().getPathSegments();
                        for (String item : list) {
                            Log.d("PATH_SEGMENT", item);
                        }
                        pathView.setText(i.getData().getPath());
                        String path = PathHandler.pathConcat(list);
                        PathHandler.savePath(getApplicationContext(), "uri", i.getData().toString());
                        PathHandler.savePath(getApplicationContext(), "path", path);
                    }
                }
            });

    @Override
    protected void onPause() {
        super.onPause();
        PathHandler.savePath(this, "path", pathView.getText().toString());
    }


    @Override
    protected void onResume() {
        super.onResume();
        pathView.setText(PathHandler.loadPath(this));
    }

    private void serviceStarter() {
        //START FILE OBSERVER SERVICE
        Intent directoryObserver = new Intent(this, FolderService.class);
        directoryObserver.putExtra("dir", pathView.getText().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(directoryObserver);
        } else {
            Toast.makeText(this, "GAMW TO SPITI", Toast.LENGTH_LONG).show();
            startService(directoryObserver);
        }
    }
}