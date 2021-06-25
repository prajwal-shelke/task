package com.example.gallery;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gallery.Adapter.imageListAdapter;
import com.example.gallery.model.Photo;
import com.example.gallery.viewModel.imageListViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    imageListAdapter adapter;
    List<Photo> list;
    List<String> images;
    List<String> names;
    imageListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        images = new ArrayList<>();
        names = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        viewModel = ViewModelProviders.of(this).get(imageListViewModel.class);
        viewModel.getImageListObserver().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {

                if(photos != null){
                    list = photos;
                    for (Photo p: photos){
                        images.add(p.getUrlS());
                    } for (Photo p: photos){
                        names.add(p.getTitle());
                    }
                    adapter = new imageListAdapter(MainActivity.this, list, new imageListAdapter.PhotoListener() {
                        @Override
                        public void onPhotoClick(String path) {
                            Intent intent = new Intent(MainActivity.this, FullScreenActivity.class);
                            intent.putExtra("path", path);
                            intent.putStringArrayListExtra("images", (ArrayList<String>) images);
                            intent.putStringArrayListExtra("names", (ArrayList<String>) names);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            }
        });
        viewModel.makeAPICall();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "onRequestPermissionsResult: Granted");
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                reqPerm ();
                Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void reqPerm (){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

}