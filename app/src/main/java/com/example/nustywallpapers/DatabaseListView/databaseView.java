package com.example.nustywallpapers.DatabaseListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.nustywallpapers.ImageDbHelper;
import com.example.nustywallpapers.ImageModel;
import com.example.nustywallpapers.R;

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


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageAdapter = new ImageAdapter(this, images);
        imageAdapter.setClickListener(this);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, imageAdapter.getItem(position), Toast.LENGTH_SHORT).show();
    }
}