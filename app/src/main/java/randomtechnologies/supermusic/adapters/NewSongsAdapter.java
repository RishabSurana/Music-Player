package randomtechnologies.supermusic.adapters;

/**
 * Created by HP on 11-08-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.SongListActivity;
import randomtechnologies.supermusic.fragments.AllTracksFragment;
import randomtechnologies.supermusic.model.NewSong;

/**
 * Copyrighted content
 * You will be sent to prison if found copying or stealing by any means
 * Created by Rishab Surana on 05-08-2017.
 */
public class NewSongsAdapter extends RecyclerView.Adapter<NewSongsAdapter.MyViewHolder>  {


    public NewSongsAdapter(ArrayList<NewSong> tracks, Context context, AllTracksFragment fContext) {
        this.songs = tracks;
        this.context = context;
        this.songSelectedListener = fContext;
    }

    public interface SongSelectedListener{
        void SongSelected(int position);

        void songSelectedForQueue(int adapterPosition);
    }

    private Context context;
    private SongSelectedListener songSelectedListener;
    private ArrayList<NewSong> songs = new ArrayList<>();

    public NewSongsAdapter(ArrayList<NewSong> songs, SongListActivity context, SongSelectedListener fContext) {
        this.songs = songs;
        this.context = context;
        this.songSelectedListener = fContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater. from(parent.getContext()).inflate(R.layout.tracks_template, parent, false);
        /*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tracks_template, parent, false);*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        NewSong currentSong = songs.get(position);

        holder.trackTitle.setText(currentSong.getTitle());
        holder.trackArtist.setText(currentSong.getDuration());
        holder.trackDuration.setText(getDurationString(currentSong.getDuration()));

    }

    public String getDurationString(String duration){

        String durationString = "";
        long hour;
        long mins;
        long seconds;
        try{

            long length = Long.parseLong(duration);
            length /= 1000;

            seconds = length % 60;
            mins = length / (60);
            hour = length / (60*60);

            if(hour > 0)
                durationString = hour + ":";

            if(mins == 0){
                durationString += ("00:");
            }else if(mins > 0 && mins < 10){
                durationString += ("0" + mins + ":");
            }else if(mins >= 10){
                durationString += (mins + ":");
            }

            if(seconds == 0){
                durationString += "00";
            }else if(seconds < 10){
                durationString += "0" + seconds;
            }else if(seconds >= 10){
                durationString += (seconds + "");
            }


            return durationString;

        }catch (Exception e){
            return duration;
        }
    }

    @Override
    public int getItemCount() {

        if(songs != null)
            return songs.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView trackTitle, trackArtist, trackDuration;
        ImageView ivPlayPause;

        public MyViewHolder(View view) {
            super(view);
            trackTitle = (TextView) view.findViewById(R.id.tvTitle);
            trackArtist = (TextView) view.findViewById(R.id.tvSec);
            trackDuration = (TextView) view.findViewById(R.id.tvTime);

            ivPlayPause = (ImageView) view.findViewById(R.id.ivPlayPause);

            ivPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    songSelectedListener.SongSelected(getAdapterPosition());
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    songSelectedListener.songSelectedForQueue(getAdapterPosition());
                }
            });
        }
    }
}