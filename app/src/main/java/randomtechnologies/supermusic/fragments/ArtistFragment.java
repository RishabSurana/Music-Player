package randomtechnologies.supermusic.fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.SongListActivity;
import randomtechnologies.supermusic.adapters.ArtistAdapter;
import randomtechnologies.supermusic.helper.Config;
import randomtechnologies.supermusic.helper.DividerItemDecoration;
import randomtechnologies.supermusic.model.Artist;

public class ArtistFragment extends Fragment implements ArtistAdapter.ArtistSelectedListener{


    ArrayList<Artist> artists;
    RecyclerView rvArtist;
    ArtistAdapter adapter;

    public ArtistFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        rvArtist = (RecyclerView) rootView.findViewById(R.id.rvArtists);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        artists = new ArrayList<>();
        getSongs();

        adapter = new ArtistAdapter(artists, getContext(), this);

        rvArtist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvArtist.setItemAnimator(new DefaultItemAnimator());
        rvArtist.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rvArtist.setAdapter(adapter);

    }

    private void getSongs() {

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri musicUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {

            //get columns
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);

            //add songs to list
            do {

                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(artistColumn);
                artists.add(new Artist(thisId, thisTitle));

            }
            while (musicCursor.moveToNext());
        }
    }

    public static Fragment newInstance() {
        return new ArtistFragment();
    }

    @Override
    public void ArtistSelected(int position) {

        Intent intent = new Intent(getContext(), SongListActivity.class);
        intent.putExtra(Config.NAVIGATION_SONGS_TYPE,Config.ARTISTS);
        intent.putExtra(Config.NAVIGATION_ID,artists.get(position).getId());
        startActivity(intent);

        Toast.makeText(getContext(), artists.get(position).getId() + "", Toast.LENGTH_SHORT).show();
    }
}
