package com.example.musicplayer;

import static android.provider.UserDictionary.Words._ID;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
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

    @SuppressLint("NotifyDataSetChanged")
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
        //Uri audioUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(_ID)); //TODO : some instruction to follow


        if (checkpermision() == false) {
            requestpermission();
            return;
        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA, // TODO : change this like (This line only works for android 10- mobiles)
                MediaStore.Audio.Media.DURATION
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"; // Collects actual music files (NOT notification and ringtone files)
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI , projection, selection, null, null); //TODO : Change the sort order in to "MediaStore.Audio.Media.TITLE + " ASC" instead of null


        // query(...) = is a request to android for a specific data , for this time it was an audio.
        //getContentResolver() = a built-in function to collect the same query in a device.
        //MediaStore.Audio.Media.EXTERNAL_CONTENT_URI = this tells to collect audio stored in external device.("MediaStore.Audio.Media.INTERNAL_CONTENT_URI" =this API will gives you some android basic songs like notification and closing)
        //Cursor = is a pointer that collects all about the audio's file info (Title, Duration, path), BUT DOESN'T DISPLAY BY IT SELF.


        while (cursor.moveToNext()){ // “Keep moving to the next row in the cursor, until there are no more rows (audio) left"
            Audiomodel songdata = new Audiomodel(cursor.getString(1), // index =1, MediaStore.Audio.Media.DATA
                                                 cursor.getString(0), // index =0, MediaStore.Audio.Media.TITLE
                                                 cursor.getString(2));// index =2, MediaStore.Audio.Media.DURATION (CREATING A SONG OBJECT WITH (PATH,TITLE,DURATION)

            //song data = is an object of type Audiomodel, representing one single song at that moment

            if (new File(songdata.getPath()).exists()) { // this condition checks weather the audio is still in the device or not
                songlist.add(songdata); // if the IF condition is true and the song exist , add it into the music player application
            }
            adapter.notifyDataSetChanged(); // Tells the RecyclerView: “Hey, my data has changed — refresh the list.”
        }

        if (songlist.size() == 0) {
            binding.nosongfound.setVisibility(View.VISIBLE); // If there is no audio in the device display NOSONGFOUND message
        } else {
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(this)); // Show the list vertically one item over another
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
            //binding.recyclerview.setAdapter(new Musiclistadapter(songlist, getApplicationContext()));
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