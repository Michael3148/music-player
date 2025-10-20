package com.example.musicplayer;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ActivityMusiclistadapterBinding;

import java.util.ArrayList;
import java.util.List;

public class Musiclistadapter extends RecyclerView.Adapter<Musiclistadapter.Viewholder>{
    ActivityMusiclistadapterBinding binding;
    private ArrayList<Audiomodel>songslist;
    Context context;
    private long backPressedTime;
    private Toast backToast;

    public Musiclistadapter(ArrayList<Audiomodel> songslist, Context context) {
        this.songslist = songslist;
        this.context = context;
    }

    public Musiclistadapter(ArrayList<Audiomodel> songlist) {
        this.songslist=songlist;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_musiclistadapter,parent,false);
        return new Musiclistadapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, @SuppressLint("RecyclerView") int position) {
        Audiomodel songdata=songslist.get(position);
        holder.titletextview.setText(songdata.getTitle());
        if (songdata!=null) {
            if (Mymediaplayer.currentindex == position) {
                holder.titletextview.setBackgroundResource(R.drawable.redbox);
                holder.titletextview.setTextColor(Color.parseColor("#00FF00"));
                holder.iconimageview.setImageResource(R.drawable.blueplaymusic);
            } else {
                holder.titletextview.setBackgroundResource(R.drawable.seekbarlayout);
                holder.iconimageview.setImageResource(R.drawable.musicicon);
                holder.titletextview.setTextColor(Color.parseColor("#000000"));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mymediaplayer.getInstance().reset();
                Mymediaplayer.currentindex=position;
                Intent intent=new Intent(context, Lastplayer.class);
                intent.putExtra("List",songslist);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    public void updateList(ArrayList<Audiomodel> newList) {
        this.songslist = newList;   // or whatever variable you hold data in
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return songslist.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder{
        TextView titletextview;
        ImageView iconimageview;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titletextview=itemView.findViewById(R.id.musictitletext);
            iconimageview=itemView.findViewById(R.id.iconview);
        }
    }
}