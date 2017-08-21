package randomtechnologies.supermusic;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import randomtechnologies.supermusic.adapters.NewSongsAdapter;
import randomtechnologies.supermusic.helper.Config;
import randomtechnologies.supermusic.model.NewSong;
import randomtechnologies.supermusic.services.MusicService;

public class SongListActivity extends BaseHandler implements NewSongsAdapter.SongSelectedListener {


    RecyclerView rvAllSongs;
    Intent receivingIntent;
    TextView tv;

    ArrayList<NewSong> songList = new ArrayList<>();

    NewSongsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewsById();

        receivingIntent = getIntent();

        fetchSongsByCategory();


        adapter = new NewSongsAdapter(songList, this, this);

        rvAllSongs.setLayoutManager(new LinearLayoutManager(this));
        rvAllSongs.setItemAnimator(new DefaultItemAnimator());
        // rvAlbum.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rvAllSongs.setAdapter(adapter);

    }

    private void fetchSongsByCategory() {


        String navType = receivingIntent.getExtras().getString(Config.NAVIGATION_SONGS_TYPE);
        long navId = receivingIntent.getExtras().getLong(Config.NAVIGATION_ID);

        if(navType.equals(Config.ALBUMS)){
            Toast.makeText(SongListActivity.this, "get Songs by albums" + navId, Toast.LENGTH_SHORT).show();
            GetAllSongsByAlbum(navId);
        }else if(navType.equals(Config.ARTISTS)){
            Toast.makeText(SongListActivity.this, "get Songs by artist" + navId, Toast.LENGTH_SHORT).show();
            GetAllSongsByArtist(navId);
        }else if(navType.equals(Config.GENRES)){
            Toast.makeText(SongListActivity.this, "get Songs by genres" + navId, Toast.LENGTH_SHORT).show();
            GetAllSongsByGenres(navId);
        }
    }

    private void GetAllSongsByGenres(long navId) {

       /* String[] projection = {
                MediaStore.Audio.Media._ID, // very important unique id of that particular song
                MediaStore.Audio.Media.TITLE, // i think we should display this
                MediaStore.Audio.Media.ARTIST, // obviously artist name
                MediaStore.Audio.Media.DATA, // this looks like the path
                MediaStore.Audio.Media.DISPLAY_NAME, // a little long name
                MediaStore.Audio.Media.DURATION, // this is duration in long
                MediaStore.Audio.Media.ALBUM, // i think this id album id
                MediaStore.Audio.Media.ALBUM_ID, // useless
                MediaStore.Audio.Media.ALBUM_KEY,// useless // removed
                MediaStore.Audio.Media.ARTIST_ID, // a unique artist id
                MediaStore.Audio.Media.BOOKMARK,//useless //removed
                MediaStore.Audio.Media.COMPOSER,//useless
                MediaStore.Audio.Media.TRACK,//useless// removed
        };*/


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



        // https://stackoverflow.com/questions/9785370/display-all-song-from-the-sd-card-genre-wise
        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", navId);
        Cursor cursor = getContentResolver().query(uri, _projection, null,null,null);

        if(cursor != null && cursor.moveToFirst())
        {
            do{
                int titleIndex =cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                int sondId = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);

                System.out.println("Song Name: "+cursor.getString(titleIndex));
                System.out.println("Song path: " + cursor.getString(sondId));

                songList.add(new NewSong(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getLong(7), cursor.getLong(8)
                ));

                /*
                songList.add(new NewSong(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
                        cursor.getString(11), cursor.getString(12)
                ));*/

              /*  tv.append(cursor.getLong(0)+ " id\n" + cursor.getString(1)+ " title\n" + cursor.getString(2)+ " artist\n" +
                        cursor.getString(3)+ " data\n" + cursor.getString(4)+ " display name\n" + cursor.getString(5)+ " duration\n" + cursor.getString(6)+ " album\n" +
                        cursor.getString(7)+ " album id\n" + cursor.getString(8)+ " album key\n" + cursor.getString(9)+ " artist id\n" + cursor.getString(10)+ " bookmark\n" +
                        cursor.getString(11)+ " composer\n" + cursor.getString(12) + " track\n\n\n");*/


            }while(cursor.moveToNext());


        }
    }

    private void GetAllSongsByArtist(long navId) {
/*
        String[] projection = {
                MediaStore.Audio.Media._ID, // very important unique id of that particular song
                MediaStore.Audio.Media.TITLE, // i think we should display this
                MediaStore.Audio.Media.ARTIST, // obviously artist name
                MediaStore.Audio.Media.DATA, // this looks like the path
                MediaStore.Audio.Media.DISPLAY_NAME, // a little long name
                MediaStore.Audio.Media.DURATION, // this is duration in long
                MediaStore.Audio.Media.ALBUM, // i think this
                MediaStore.Audio.Media.ALBUM_ID, // useless
                MediaStore.Audio.Media.ALBUM_KEY,// useless
                MediaStore.Audio.Media.ARTIST_ID, // a unique artist id
                MediaStore.Audio.Media.BOOKMARK,//useless
                MediaStore.Audio.Media.COMPOSER,//useless
                MediaStore.Audio.Media.TRACK,//useless
        };*/

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

        String selectionString = "artist_id = " + navId;

        Cursor cursor = null;
        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContentResolver().query(uri, _projection, selectionString, null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {


                    songList.add(new NewSong(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                            cursor.getLong(7), cursor.getLong(8)
                    ));
                    /*
                    songList.add(new NewSong(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                            cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
                            cursor.getString(11), cursor.getString(12)
                            ));

*/
                   /* tv.append(cursor.getLong(0)+ " id\n" + cursor.getString(1)+ " title\n" + cursor.getString(2)+ " artist\n" +
                            cursor.getString(3)+ " data\n" + cursor.getString(4)+ " display name\n" + cursor.getString(5)+ " duration\n" + cursor.getString(6)+ " album\n" +
                            cursor.getString(7)+ " album id\n" + cursor.getString(8)+ " album key\n" + cursor.getString(9)+ " artist id\n" + cursor.getString(10)+ " bookmark\n" +
                            cursor.getString(11)+ " composer\n" + cursor.getString(12) + " track\n\n\n");*/

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

    private void GetAllSongsByAlbum(long navId) {

      /*  String[] projection = {
                MediaStore.Audio.Media._ID, // very important unique id of that particular song
                MediaStore.Audio.Media.TITLE, // i think we should display this
                MediaStore.Audio.Media.ARTIST, // obviously artist name
                MediaStore.Audio.Media.DATA, // this looks like the path
                MediaStore.Audio.Media.DISPLAY_NAME, // a little long name
                MediaStore.Audio.Media.DURATION, // this is duration in long
                MediaStore.Audio.Media.ALBUM, // i think this
                MediaStore.Audio.Media.ALBUM_ID, // useless
                MediaStore.Audio.Media.ALBUM_KEY,// useless
                MediaStore.Audio.Media.ARTIST_ID, // a unique artist id
                MediaStore.Audio.Media.BOOKMARK,//useless
                MediaStore.Audio.Media.COMPOSER,//useless
                MediaStore.Audio.Media.TRACK,//useless
        };*/

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

        final String selection = "album_id = " + navId;
        Cursor cursor = null;
        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContentResolver().query(uri, _projection, selection, null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {


                    songList.add(new NewSong(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                            cursor.getLong(7), cursor.getLong(8)
                    ));

                    /*tv.append(cursor.getLong(0)+ " id\n" + cursor.getString(1)+ " title\n" + cursor.getString(2)+ " artist\n" +
                            cursor.getString(3)+ " data\n" + cursor.getString(4)+ " display name\n" + cursor.getString(5)+ " duration\n" + cursor.getString(6)+ " album\n" +
                            cursor.getString(7)+ " album id\n" + cursor.getString(8)+ " album key\n" + cursor.getString(9)+ " artist id\n" + cursor.getString(10)+ " bookmark\n" +
                            cursor.getString(11)+ " composer\n" + cursor.getString(12) + " track\n\n\n");*/

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

    private void findViewsById() {
        rvAllSongs = (RecyclerView) findViewById(R.id.rvAllSongs);
        tv = (TextView) findViewById(R.id.tv);
    }

    @Override
    public void SongSelected(int position) {

        super.addSongListener(songList.get(position));
        MusicService.addSongToListAndPlay(songList.get(position));

    }

    @Override
    public void songSelectedForQueue(int position) {
        super.addSongListener(songList.get(position));
        if(MusicService.getMediaplayerStatus()){
            MusicService.addSongToListAndPlayIfNotPlayingAnyOtherSong(songList.get(position));
        }else {
            MusicService.addSongToListAndPlay(songList.get(position));
        }


//        makeToast(songList.get(position).getDisplayName() + " added to queue...");
    }

    private void makeToast(String s) {
        Toast.makeText(SongListActivity.this, s , Toast.LENGTH_SHORT).show();
    }
}
