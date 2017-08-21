package randomtechnologies.supermusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.model.Song;

/**
 * Copyrighted content
 * You will be sent to prison if found copying or stealing by any means
 * Created by Rishab Surana on 05-08-2017.
 */
public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.MyViewHolder>  {

    public interface SongSelectedListener{
        void SongSelected(int position);
    }

    private Context context;
    private SongSelectedListener songSelectedListener;
    private ArrayList<Song> tracks = new ArrayList<>();

    public TracksAdapter(ArrayList<Song> tracks, Context context, SongSelectedListener fContext) {
        this.tracks = tracks;
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

        Song currentSong = tracks.get(position);

        holder.trackTitle.setText(currentSong.getTitle() + "\n" + currentSong.getAlbumId());
        holder.trackArtist.setText(currentSong.getArtist());
        holder.trackDuration.setText(currentSong.getLength() + "\n" + currentSong.getAlbum() + "\n" + currentSong.getID());

        holder.ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songSelectedListener.SongSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {

        if(tracks != null)
            return tracks.size();
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
        }
    }
}
