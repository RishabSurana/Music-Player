package randomtechnologies.supermusic.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.model.Album;

/**
 * Created by HP on 09-08-2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder>  {

    public interface AlbumsSelectedListener{
        void AlbumSelected(int position);
    }

    private Context context;
    private AlbumsSelectedListener albumsSelectedListener;
    private ArrayList<Album> albumList = new ArrayList<>();

    public AlbumsAdapter(ArrayList<Album> albums, Context context, AlbumsSelectedListener listener) {
        this.albumList = albums;
        this.context = context;
        this.albumsSelectedListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater. from(parent.getContext()).inflate(R.layout.albums_template, parent, false);
        /*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tracks_template, parent, false);*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Album currentAlbum = albumList.get(position);

        holder.albumsTitle.setText(currentAlbum.getTitle());

        if(currentAlbum.getPath() != null && !currentAlbum.getPath().isEmpty())
            holder.albumArt.setImageBitmap(BitmapFactory.decodeFile(currentAlbum.getPath()));
        else{
            holder.albumArt.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.albumArt.setImageResource(R.mipmap.ic_launcher);
        }


    }

    @Override
    public int getItemCount() {

        if(albumList != null)
            return albumList.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView albumsTitle;
        public AppCompatImageView albumArt;

        public MyViewHolder(View view) {
            super(view);
            albumsTitle = (TextView) view.findViewById(R.id.tvTitle);
            albumArt = (AppCompatImageView) view.findViewById(R.id.albumArt);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    albumsSelectedListener.AlbumSelected(getAdapterPosition());
                }
            });
        }
    }
}