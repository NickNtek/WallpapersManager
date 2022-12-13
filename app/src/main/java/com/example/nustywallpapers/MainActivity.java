package com.example.nustywallpapers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nustywallpapers.DatabaseListView.databaseView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String GRANTED = "Permission Granted";
    public static final String DENIED = "Permission Denied";
    public static final String PATH_KEY = "path";
    public static final String LOCK_CHECKBOX_KEY = "lockScreen";
    public static final String WIDTH = "screen_width";
    public static final String HEIGHT = "screen_height";

    TextView pathView;
    CheckBox lockScreenCheckBox;
    ImageDbHelper imageDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pathView = findViewById(R.id.path_view);
        lockScreenCheckBox = findViewById(R.id.LockScreenCheckBox);
        imageDbHelper = new ImageDbHelper(this);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        PathHandler.saveValue(this, WIDTH, width+"");
        PathHandler.saveValue(this, HEIGHT, height+"");

        //GET FILE PERMISSIONS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            //BreadToast(GRANTED);
        } else {
            resultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            //BreadToast(DENIED);
        }

        //start Receiver
/*
        BroadcastReceiver br = new LockReceiver();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(br, intentFilter);
*/

        Intent intent = new Intent(this, OnLockService.class);
        startService(intent);
        //pathView.setText(PathHandler.loadValue(this, "path"));

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
                        List<String> list = i.getData().getPathSegments();

                        pathView.setText(PathHandler.pathConcat(list));
                        PathHandler.saveValue(MainActivity.this, PATH_KEY, pathView.getText().toString());

                        Uri uri = i.getData();
                        DocumentFile directory = DocumentFile.fromTreeUri(MainActivity.this, uri);
                        DocumentFile[] files;

                        boolean first = true;

                        ArrayList<ImageModel> forSave = new ArrayList<>();

                        imageDbHelper.deleteAll();
                        if (directory != null) {
                            files = directory.listFiles();
                            for (int count = 0; count< files.length; count++) {
                                DocumentFile f = files[count];
                                if (f.isFile()){
                                    if (f.getType().matches("(^)image(.*)")) {
                                        if (first) {

                                            ImageModel imageModel = new ImageModel(directory.getUri().toString(), f.getName(), f.getName().hashCode(), false, first, true);
                                            //imageDbHelper.insert(imageModel);
                                            forSave.add(imageModel);
                                            first = false;
                                            continue;
                                        }
                                        ImageModel imageModel = new ImageModel(directory.getUri().toString(), f.getName(), f.getName().hashCode(), false, first, true);
                                        //imageDbHelper.insert(imageModel);
                                        forSave.add(imageModel);
                                        forSave.get(count-1).setLast(false);

                                    }
                                }
                            }
                            imageDbHelper.saveAll(forSave);
                        } else {
                            Log.e("ERROR", "Directory is null");
                            BreadToast("directory is null");
                        }

                    }
                }
            });

    @Override
    protected void onPause() {
        super.onPause();
        PathHandler.saveValue(this, PATH_KEY, pathView.getText().toString());
        if (lockScreenCheckBox.isChecked()) {
            PathHandler.saveValue(this, LOCK_CHECKBOX_KEY, "1");
        } else {
            PathHandler.saveValue(this, LOCK_CHECKBOX_KEY, "0");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        pathView.setText(PathHandler.loadValue(this, PATH_KEY));
        lockScreenCheckBox.setChecked(PathHandler.loadValue(this, LOCK_CHECKBOX_KEY).equals("1"));
    }


    public void buttonTestFunction(View view) {
        ArrayList<ImageModel> images = imageDbHelper.getAll();
        for (ImageModel i : images) {
            Log.d("IMAGE", i.toString());
        }
        Intent intent = new Intent(this, databaseView.class);
        startActivity(intent);


/*
        ImageModel current = imageDbHelper.findCurrent();
        if (current == null) {
            Log.d("CURRENT", "NO CURRENT IMAGE");
        } else {
            Log.d("CURRENT", current.toString());
        }

        ImageModel first = imageDbHelper.findFirst();
        if (first == null) {
            Log.d("FIRST", "NO CURRENT IMAGE");
        } else {
            Log.d("FIRST", first.toString());
        }
*/


    }
}