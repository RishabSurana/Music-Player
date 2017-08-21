package randomtechnologies.supermusic.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.model.NewSong;

/**
 * Copyrighted content
 * You will be sent to prison if found copying or stealing by any means
 * Created by Rishab Surana on 05-08-2017.
 */
public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.MyViewHolder>  {

    private SparseBooleanArray selectedItems;
    private boolean reverseAllAnimations;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private int currentSelectedIndex;

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public void toggleSelection(int pos) {

        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);

    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    public interface QueueSongSelectedListener{
        void songClickListener(int position);

        void songLongClickListener(int position);
    }

    private Context context;
    private QueueSongSelectedListener queueSongSelectedListener;
    private ArrayList<NewSong> songs = new ArrayList<>();

    public QueueAdapter(ArrayList<NewSong> songs, Activity context, QueueSongSelectedListener fContext) {
        this.songs = songs;
        this.context = context;
        this.queueSongSelectedListener = fContext;

        selectedItems = new SparseBooleanArray();
        animationItemsIndex= new SparseBooleanArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater. from(parent.getContext()).inflate(R.layout.queue_template, parent, false);
        /*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tracks_template, parent, false);*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        NewSong currentSong = songs.get(position);

        holder.trackTitle.setText(currentSong.getTitle());
        holder.trackArtist.setText(currentSong.getArtist());
        holder.trackDuration.setText(getDurationString(currentSong.getDuration()));
        applyIconAnimation(holder, position);
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {

            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.trackcolor));
            if (currentSelectedIndex == position) {
                resetCurrentIndex();
            }
        } else {
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.folderscolor));
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                resetCurrentIndex();
            }
        }
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public String getDurationString(String duration){

        String durationString = "";
        long hour;
        long mins;
        long seconds;
        try{

            long length = Long.parseLong(duration);
            length %= 1000;

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
        public RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            trackTitle = (TextView) view.findViewById(R.id.tvTitle);
            trackArtist = (TextView) view.findViewById(R.id.tvSec);
            trackDuration = (TextView) view.findViewById(R.id.tvTime);
            parent = (RelativeLayout) view.findViewById(R.id.parent);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    queueSongSelectedListener.songClickListener(getAdapterPosition());
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                Toast.makeText(context, "long", Toast.LENGTH_SHORT).show();
                    queueSongSelectedListener.songLongClickListener(getAdapterPosition());
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    return true;
                }
            });
        }
    }
}