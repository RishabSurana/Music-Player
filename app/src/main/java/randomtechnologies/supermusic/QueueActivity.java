package randomtechnologies.supermusic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Queue;

import io.realm.Realm;
import io.realm.RealmResults;
import randomtechnologies.supermusic.adapters.NewSongsAdapter;
import randomtechnologies.supermusic.adapters.QueueAdapter;
import randomtechnologies.supermusic.database.RealmController;
import randomtechnologies.supermusic.model.NewSong;
import randomtechnologies.supermusic.services.MusicService;

public class QueueActivity extends AppCompatActivity implements QueueAdapter.QueueSongSelectedListener {




    RecyclerView rvMusicQueue;
    QueueAdapter nsAdapter;




    RecyclerView rvQueue;
    QueueAdapter adapter;
    ArrayList<NewSong> queue;
    Realm realm;

    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RealmController.with(QueueActivity.this).clearAll();
                setData();
                Snackbar.make(view, "Deleted all songs from queue", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.realm = RealmController.with(this).getRealm();
        setData();
        actionModeCallback = new ActionModeCallback();
    }

    private void setData(){
        RealmResults<NewSong> resultSet = RealmController.with(this).getSongsQueue();
        adapter = new QueueAdapter(new ArrayList<>(resultSet), this, this);
        rvQueue = (RecyclerView) findViewById(R.id.rvQueue);
        rvQueue.setLayoutManager(new LinearLayoutManager(this));
        rvQueue.setAdapter(adapter);

        // for music service queue
        nsAdapter = new QueueAdapter(MusicService.getSongsList(), this, this);
        rvMusicQueue = (RecyclerView) findViewById(R.id.rvQueue2);
        rvMusicQueue.setLayoutManager(new LinearLayoutManager(this));
        rvMusicQueue.setAdapter(nsAdapter);

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_delete_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteSelectedSongs();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;
          /*  rvQueue.post(new Runnable() {
                @Override
                public void run() {
                    adapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });*/
        }
    }

    private void deleteSelectedSongs() {

    }

    @Override
    public void songClickListener(int position) {

        if(adapter.getSelectedItemCount() > 0){
            enableActionMode(position);
        }else{
            // code to start the current position song
        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void songLongClickListener(int position) {

        enableActionMode(position);
    }
}
