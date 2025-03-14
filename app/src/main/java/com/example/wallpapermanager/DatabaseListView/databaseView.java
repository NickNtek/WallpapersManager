package com.example.wallpapermanager.DatabaseListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wallpapermanager.DataBaseHelper.ImageDbHelper;
import com.example.wallpapermanager.Model.ImageModel;
import com.example.wallpapermanager.R;

import java.util.ArrayList;

public class databaseView extends AppCompatActivity implements  ImageAdapter.ItemClickListener{

    ImageAdapter imageAdapter;
    ImageDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_view);

        dbHelper = new ImageDbHelper(this);

        ArrayList<ImageModel> images = dbHelper.getAll();


        Configuration configuration = getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, screenWidthDp/80));

        imageAdapter = new ImageAdapter(this, images);
        imageAdapter.setClickListener(this);
        recyclerView.setAdapter(imageAdapter);
        dbHelper.close();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, imageAdapter.getItem(position), Toast.LENGTH_SHORT).show();
    }
}