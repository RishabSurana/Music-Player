package randomtechnologies.supermusic.fragments;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import randomtechnologies.supermusic.R;
import randomtechnologies.supermusic.SongListActivity;
import randomtechnologies.supermusic.adapters.GenresAdapter;
import randomtechnologies.supermusic.adapters.TracksAdapter;
import randomtechnologies.supermusic.helper.Config;
import randomtechnologies.supermusic.helper.DividerItemDecoration;
import randomtechnologies.supermusic.model.Genres;


public class GenresFragment extends Fragment implements GenresAdapter.GenresSelectedListener{

    ArrayList<Genres> genresList = new ArrayList<>();

    Context mContext;
    GenresAdapter adapter;
    RecyclerView rvGenres;

    public GenresFragment() {
        // Required empty public constructor
    }

    public static GenresFragment newInstance() {
        GenresFragment fragment = new GenresFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_genres, container, false);
        rvGenres = (RecyclerView) rootView.findViewById(R.id.rvGenres);

        genresList = new ArrayList<>();
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        getGenresList();

        adapter = new GenresAdapter(genresList, mContext, this);

        rvGenres.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvGenres.setItemAnimator(new DefaultItemAnimator());
        rvGenres.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        rvGenres.setAdapter(adapter);
    }


    private void getGenresList() {
        int indexName, indexId;
        long genreId;
        Uri uri;
        Cursor genrecursor;
        Cursor tempcursor;

        genrecursor =  mContext.getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,null,null, null, null);
        if(genrecursor != null && genrecursor.moveToFirst())
        {
            do{

                indexName = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                indexId = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
                System.out.println("GENRE NAME: "+genrecursor.getString(indexName));
                System.out.println("======================================");

                String name = genrecursor.getString(indexName);
                long id = Long.parseLong(genrecursor.getString(indexId));

                genresList.add(new Genres(name, id));

                uri = MediaStore.Audio.Genres.Members.getContentUri("external", id);
                tempcursor = mContext.getContentResolver().query(uri, null, null,null,null);

                if(tempcursor != null && tempcursor.moveToFirst())
                {
                    do{
                        int titleIndex =tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                        int sondId = tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);

                        System.out.println("Song Name: "+tempcursor.getString(titleIndex));
                        System.out.println("Song path: " + tempcursor.getString(sondId));
                    }while(tempcursor.moveToNext());
                }
            }while(genrecursor.moveToNext());

            genrecursor.close();
        }
    }

    private void getGenresList2() {

        //-------------------------------------

        int index;
        long genreId;
        int count;
        Uri uri;
        Cursor genrecursor;
        Cursor tempcursor;
        String[] proj1 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
        String[] proj2={MediaStore.Audio.Media.DISPLAY_NAME};

        genrecursor =  mContext.getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,proj1,null, null, null);
        if(genrecursor != null && genrecursor.moveToFirst())
        {
            do{
                count = genrecursor.getCount();
                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                System.out.println("GENRE NAME: "+genrecursor.getString(index));
                System.out.println("======================================");


                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
                genreId=Long.parseLong(genrecursor.getString(index));
                uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

                tempcursor = mContext.getContentResolver().query(uri, proj2, null,null,null);
                System.out.println("Total Songs: "+tempcursor.getCount());
                if(tempcursor.moveToFirst())
                {
                    do{
                        index=tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                        System.out.println("    Song Name: "+tempcursor.getString(index));

                    }while(tempcursor.moveToNext());
                }
                System.out.println("======================================");
            }while(genrecursor.moveToNext());

            genrecursor.close();
        }


    }

    @Override
    public void GenreSelected(int position) {

        Intent intent = new Intent(getContext(), SongListActivity.class);
        intent.putExtra(Config.NAVIGATION_SONGS_TYPE,Config.GENRES);
        intent.putExtra(Config.NAVIGATION_ID,genresList.get(position).getId());
        startActivity(intent);

        Toast.makeText(getContext(), genresList.get(position).getId() + "", Toast.LENGTH_SHORT).show();

        makeToast(position + "");
    }

    private void makeToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }
}
