package com.example.musicplayer;


import static com.example.musicplayer.Mymediaplayer.currentindex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplayer.Mymediaplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Lastplayer extends AppCompatActivity {
    ArrayList<Audiomodel> songlist;
    Audiomodel currentsong;
    TextView titletv, currenttimeeee, totaltimeeee;
    ImageButton playbtn, backbtn, forwardbtn;
    SeekBar seekBarrrr;
    ImageView musicicon;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lasyplayer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        titletv = findViewById(R.id.songtitle);
        currenttimeeee = findViewById(R.id.currenttime);
        totaltimeeee = findViewById(R.id.totaltime);
        seekBarrrr = findViewById(R.id.seekBar);
        playbtn = findViewById(R.id.pause);
        backbtn = findViewById(R.id.previous);
        forwardbtn = findViewById(R.id.next);
        musicicon = findViewById(R.id.musiciconbig);

        titletv.setSelected(true);  // TODO to make your music title marque

        songlist = (ArrayList<Audiomodel>) getIntent().getSerializableExtra("List");



        setresourcewithmusic();

        Lastplayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mediaPlayer = Mymediaplayer.getInstance();
                if (mediaPlayer != null) {
                    seekBarrrr.setProgress(mediaPlayer.getCurrentPosition());
                    currenttimeeee.setText(converttommss(mediaPlayer.getCurrentPosition() + ""));
                    if (mediaPlayer.isPlaying()) {
                        playbtn.setImageResource(R.drawable.play);
                        musicicon.setRotation((x++) * 3);
                    } else {
                        playbtn.setImageResource(R.drawable.pause);
                        musicicon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this, 100);
            }
        });
        seekBarrrr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            MediaPlayer mediaPlayer = Mymediaplayer.getInstance();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //leave it empty
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //leave it empty
            }
        });
    }

    private void playmusic() {
        MediaPlayer mediaPlayer = Mymediaplayer.getInstance();

        if (Mymediaplayer.getInstance().isPlaying()) {

        }
        else {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(currentsong.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                seekBarrrr.setProgress(0);
                seekBarrrr.setMax(mediaPlayer.getDuration());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //playnextsong();
                //TODO work on this part
            }
        });
    }

    private void playnextsong() {
        if (currentindex < songlist.size() - 1) {
            // Move to the next index
            currentindex++;

            // Get the MediaPlayer instance
            MediaPlayer player = Mymediaplayer.getInstance();

            // Reset and set new data source
            player.reset();
            try {
                player.setDataSource(String.valueOf(songlist.get(currentindex)));
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Optionally: Loop back to first song
            // Mymediaplayer.currentindex = 0;
            Toast.makeText(getApplicationContext(), "No next song", Toast.LENGTH_SHORT).show();
        }
        setresourcewithmusic();
    }

    private void playprevoiussong() {
        if (currentindex > 0) {
            // Move to the previous index
            currentindex--;

            MediaPlayer player = Mymediaplayer.getInstance();
            player.reset();
            try {
                player.setDataSource(String.valueOf(songlist.get(currentindex)));
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Optionally: Go to last song
            // Mymediaplayer.currentindex = songList.size() - 1;
            Toast.makeText(getApplicationContext(), "No previous song \n #1st", Toast.LENGTH_SHORT).show();
        }
        setresourcewithmusic();
    }

    private void pauseplay() {
        MediaPlayer mediaPlayer = Mymediaplayer.getInstance();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    void setresourcewithmusic() {
        MediaPlayer mediaPlayer = Mymediaplayer.getInstance();

        currentsong = songlist.get(currentindex);
        titletv.setText(currentsong.getTitle());
        totaltimeeee.setText(converttommss(currentsong.getDuration()));

        playbtn.setOnClickListener(v -> pauseplay());
        forwardbtn.setOnClickListener(v -> playnextsong());
        backbtn.setOnClickListener(v -> playprevoiussong());
        playmusic();


    }

    @SuppressLint("DefaultLocale")
    public static String converttommss(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}