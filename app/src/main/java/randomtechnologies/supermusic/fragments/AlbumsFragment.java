package randomtechnologies.supermusic.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.SongListActivity;
import randomtechnologies.supermusic.adapters.AlbumsAdapter;
import randomtechnologies.supermusic.adapters.ArtistAdapter;
import randomtechnologies.supermusic.helper.Config;
import randomtechnologies.supermusic.helper.DividerItemDecoration;
import randomtechnologies.supermusic.model.Album;
import randomtechnologies.supermusic.model.Artist;

public class AlbumsFragment extends Fragment implements AlbumsAdapter.AlbumsSelectedListener {

    RecyclerView rvAlbum;
    AlbumsAdapter adapter;



    ArrayList<Album> albums = new ArrayList<>();
    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_albums, container, false);

        rvAlbum = (RecyclerView) view.findViewById(R.id.rvAlbums);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getAlbums();

        adapter = new AlbumsAdapter(albums, getContext(), this);

        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvAlbum.setItemAnimator(new DefaultItemAnimator());
       // rvAlbum.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rvAlbum.setAdapter(adapter);
    }

    private void getAlbums() {

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {

            //get columns
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
            int artColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM_ART);

            //add songs to list
            albums.clear();
            do {

                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(artistColumn);
                String path = musicCursor.getString(artColumn);
                albums.add(new Album(thisId, thisTitle, path));

                /*if( path != null && !path.equals("")){
                    File file = new File(path);
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    iv.setImageBitmap(myBitmap);
                }*/
            }
            while (musicCursor.moveToNext());
        }
    }

    public static Fragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void AlbumSelected(int position) {

        Intent intent = new Intent(getContext(), SongListActivity.class);
        intent.putExtra(Config.NAVIGATION_SONGS_TYPE,Config.ALBUMS);
        intent.putExtra(Config.NAVIGATION_ID,albums.get(position).getId());
        startActivity(intent);

        Toast.makeText(getContext(), albums.get(position).getId() + "", Toast.LENGTH_SHORT).show();

        Toast.makeText(getContext(), albums.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
}
