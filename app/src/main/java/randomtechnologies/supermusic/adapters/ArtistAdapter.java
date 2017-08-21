package randomtechnologies.supermusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.model.Artist;
import randomtechnologies.supermusic.model.Genres;

/**
 * Created by HP on 09-08-2017.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder>  {

    public interface ArtistSelectedListener{
        void ArtistSelected(int position);
    }

    private Context context;
    private ArtistSelectedListener artistSelectedListener;
    private ArrayList<Artist> artists = new ArrayList<>();

    public ArtistAdapter(ArrayList<Artist> artists, Context context, ArtistSelectedListener listener) {
        this.artists = artists;
        this.context = context;
        this.artistSelectedListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater. from(parent.getContext()).inflate(R.layout.genres_template, parent, false);
        /*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tracks_template, parent, false);*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Artist currentArtist = artists.get(position);

        holder.genresTitle.setText(currentArtist.getName());

        holder.genresTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artistSelectedListener.ArtistSelected(position);
            }
        });

    }

    @Override
    public int getItemCount() {

        if(artists != null)
            return artists.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView genresTitle;

        public MyViewHolder(View view) {
            super(view);
            genresTitle = (TextView) view.findViewById(R.id.tvGenreTitle);
        }

    }
}