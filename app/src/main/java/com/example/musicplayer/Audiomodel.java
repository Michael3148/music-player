package com.example.musicplayer;

import java.io.Serializable;

public class Audiomodel implements Serializable {   //use pracelabel for larger objects,Serializable for smaller apps
    String path;
    String title;
    String duration;

    //This are the three piece of information you want to store

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Audiomodel(String path, String title, String duration) {
        this.path = path; //MediaStore.Audio.Media.DATA
        this.title = title; //MediaStore.Audio.Media.TITLE
        this.duration = duration; // MediaStore.Audio.Media.DURATION

        // Must be in the same order as in the cursor.getString(index) in MainActivity.
    }


    // This serializable class mainly uses to pass the object into Lastplayer.java class (Pass song information safely between activities).
    // Serialized (MainActivity) -> intent (Lastplayer) -> Deserialized (Lastplayer). That is the life path of the object and the use of Serializable class.
    // Also avoid crashing of activities.
}
