package com.example.musicplayer;

import android.Manifest;

import android.content.pm.PackageManager;
import android.database.Cursor;

import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayer.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<Audiomodel> songlist = new ArrayList<>();
    Musiclistadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adapter = new Musiclistadapter(songlist);
        binding.recyclerview.setAdapter(adapter);


        if (checkpermision() == false) {
            requestpermission();
            return;
        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);

        while (cursor.moveToNext()) {
            Audiomodel songdata = new Audiomodel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if (new File(songdata.getPath()).exists())
                songlist.add(songdata);
        }
        if (songlist.size() == 0) {
            binding.nosongfound.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerview.setAdapter(new Musiclistadapter(songlist, getApplicationContext()));
        }

        binding.dots.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, view);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.option_shuffle) {
                    Collections.shuffle(songlist);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Scroll a little", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.option_atoz) {
                    Collections.sort(songlist, (o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Scroll a little", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.option_ztoa) {
                    Collections.sort(songlist, (o1, o2) -> o2.getTitle().compareToIgnoreCase(o1.getTitle()));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Scroll a little", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }
    boolean checkpermision() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestpermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Permission required form settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

    protected void onResume() {  //is a lifecycle method that is called when your activity comes to the foreground and becomes interactive again
        super.onResume();
        if (binding.recyclerview != null) {
            binding.recyclerview.setAdapter(new Musiclistadapter(songlist, getApplicationContext()));
        }
    }

    private boolean doubleBackToExitPressedOnce = false;

    //TODO : you have another notification in application see it

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed(); // Exit app
            finishAffinity();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        // Reset after 2 seconds
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000); // 2 seconds
    }
}