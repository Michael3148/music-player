package com.example.musicplayer;

import java.io.Serializable;

public class Audiomodel implements Serializable {   //use pracelabel for larger objects,Serializable for smaller apps
    String path;
    String title;
    String duration;

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
        this.path = path;
        this.title = title;
        this.duration = duration;
    }
}
