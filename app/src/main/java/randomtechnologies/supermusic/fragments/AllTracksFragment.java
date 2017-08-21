package randomtechnologies.supermusic.fragments;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.adapters.NewSongsAdapter;
import randomtechnologies.supermusic.helper.StatusListener;
import randomtechnologies.supermusic.model.NewSong;
import randomtechnologies.supermusic.services.MusicService;

public class AllTracksFragment extends BaseFragment implements NewSongsAdapter.SongSelectedListener{


    Context context;
    public static ArrayList<NewSong> tracks;
    RecyclerView rvTracks;
    NewSongsAdapter adapter;

    StatusListener statusListener;

    public AllTracksFragment() {
        // Required empty public constructor
    }

    public static AllTracksFragment newInstance(Context context) {
        AllTracksFragment fragment = new AllTracksFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            statusListener = (StatusListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View rootView = LayoutInflater. from(getContext()).inflate(R.layout.fragment_all_tracks, container, false);
        View rootView = inflater.inflate(R.layout.fragment_all_tracks, container, false);
        rvTracks = (RecyclerView) rootView.findViewById(R.id.rvTracks);

        tracks = new ArrayList<>();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getSongs();
        adapter = new NewSongsAdapter(tracks, getContext(), this);
        rvTracks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvTracks.setItemAnimator(new DefaultItemAnimator());
        rvTracks.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        rvTracks.setAdapter(adapter);

        }

    @Override
    public void SongSelected(int position) {
        MusicService.addSongToListAndPlay(tracks.get(position));

        statusListener.songStartedListener();
        statusListener.addSongListener(tracks.get(position));
    }

    @Override
    public void songSelectedForQueue(int position) {
        if(MusicService.getMediaplayerStatus()){
            MusicService.addSongToListAndPlayIfNotPlayingAnyOtherSong(tracks.get(position));
        }else {
            MusicService.addSongToListAndPlay(tracks.get(position));
        }
            statusListener.songStartedListener();
            statusListener.addSongListener(tracks.get(position));
    }

    private void makeToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void getSongs() {

        String[] _projection = {
                MediaStore.Audio.Media._ID, // very important unique id of that particular song
                MediaStore.Audio.Media.TITLE, // i think we should display this
                MediaStore.Audio.Media.ARTIST, // obviously artist name
                MediaStore.Audio.Media.DATA, // this looks like the path
                MediaStore.Audio.Media.DISPLAY_NAME, // a little long name
                MediaStore.Audio.Media.DURATION, // this is duration in long
                MediaStore.Audio.Media.ALBUM, // i think this
                MediaStore.Audio.Media.ALBUM_ID, // useless
                MediaStore.Audio.Media.ARTIST_ID, // a unique artist id

        };

        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;
        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContext().getContentResolver().query(uri, _projection, null, null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    tracks.add(new NewSong(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                            cursor.getLong(7), cursor.getLong(8)
                    ));

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e("Media", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
   /* private void getSongs() {

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int lengthColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);

            int displayNameColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DISPLAY_NAME);
            int isMusicColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.IS_MUSIC);
            int albumId = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int random = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            *//*int pic = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.IS_MUSIC);
            int path = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ART);*//*


            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                String thisAlbum = musicCursor.getString(albumColumn);
                String thisDuration = musicCursor.getString(lengthColumn);


                String thisDisplayName = musicCursor.getString(displayNameColumn);
                boolean isMusic = Boolean.parseBoolean(musicCursor.getString(isMusicColumn));
                long thisalbumId = Long.parseLong(musicCursor.getString(albumId));

                tracks.add(new Song(thisId, thisTitle, thisArtist
                        , thisAlbum, thisDuration, thisDisplayName, isMusic, thisalbumId));
            }
            while (musicCursor.moveToNext());
        }
    }*/




    /*//connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            makeToast("heheeh");
            //pass list
            musicSrv.setList(tracks);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
*/