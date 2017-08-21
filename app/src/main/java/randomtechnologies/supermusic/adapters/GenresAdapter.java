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
import randomtechnologies.supermusic.model.Genres;
import randomtechnologies.supermusic.model.Song;

/**
 * Created by HP on 08-08-2017.
 */
public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MyViewHolder>  {

    public interface GenresSelectedListener{
        void GenreSelected(int position);
    }

    private Context context;
    private GenresSelectedListener genresSelectedListener;
    private ArrayList<Genres> genres = new ArrayList<>();

    public GenresAdapter(ArrayList<Genres> genres, Context context, GenresSelectedListener listener) {
        this.genres = genres;
        this.context = context;
        this.genresSelectedListener = listener;
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

        Genres currentSong = genres.get(position);

        holder.genresTitle.setText(currentSong.getName());

        holder.genresTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genresSelectedListener.GenreSelected(position);
            }
        });

    }

    @Override
    public int getItemCount() {

        if(genres != null)
            return genres.size();
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