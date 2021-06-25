package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gallery.Adapter.ViewPagerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FullScreenActivity extends AppCompatActivity {

    String path;
    Integer currentItem;
    ArrayList<String> photos;
    ArrayList<String> names;
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Toolbar toolbar = findViewById(R.id.toolbar_fullScreenActivity);
        setSupportActionBar(toolbar);

        View view = toolbar.getChildAt(0);
        view.setOnClickListener(v -> {
            startActivity(new Intent(FullScreenActivity.this, MainActivity.class));
            finish();
        });

        path = getIntent().getStringExtra("path");
        photos = getIntent().getStringArrayListExtra("images");
        names = getIntent().getStringArrayListExtra("names");
        viewPager = findViewById(R.id.viewPager);

        for(int i=0;i< photos.size();i++) {
            if(photos.get(i).equals(path)){
                currentItem = i;
            }
        }

        pagerAdapter = new ViewPagerAdapter(this, photos);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentItem);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.download){
            downloadImage(photos.get(viewPager.getCurrentItem()));
            return true;
        }
        return true;
    }

    private void downloadImage(String imageURL){

        boolean success = true;
        final String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
        File f = new File(Environment.getExternalStorageDirectory(), "Gallery");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Files.createDirectory(Paths.get(f.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (!f.exists()) {
                f.mkdir();
                f.mkdirs();
                success = true;
            }
        }

        if (success) {
            Glide.with(getApplicationContext())
                    .load(imageURL)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                            Toast.makeText(getApplicationContext(), "Saving Image...", Toast.LENGTH_SHORT).show();
                            saveImage(bitmap, f, fileName);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            Toast.makeText(getApplicationContext(), "Failed to Download Image!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveImage(Bitmap image, File storageDir, String imageFileName) {

        File imageFile = new File(storageDir, imageFileName);
        try {
            OutputStream fOut = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.close();
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error while saving image!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}