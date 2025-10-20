package com.example.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;

public class Mymediaplayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance() {
        if (instance == null) {
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static int currentindex = 0;
}