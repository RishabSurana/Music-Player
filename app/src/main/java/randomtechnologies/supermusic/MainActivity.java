package randomtechnologies.supermusic;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.RealmResults;
import randomtechnologies.supermusic.database.RealmController;
import randomtechnologies.supermusic.fragments.AlbumsFragment;
import randomtechnologies.supermusic.fragments.AllTracksFragment;
import randomtechnologies.supermusic.fragments.ArtistFragment;
import randomtechnologies.supermusic.fragments.FolderFragment;
import randomtechnologies.supermusic.fragments.GenresFragment;
import randomtechnologies.supermusic.helper.Config;
import randomtechnologies.supermusic.model.NewSong;
import randomtechnologies.supermusic.services.MusicService;
import randomtechnologies.supermusic.settings.SettingsActivity;

public class MainActivity extends BaseHandler
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ViewPager mViewPager;
    TabLayout tabLayout;
    Toolbar toolbar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    ImageView ivPlayPause;
    TextView tvTimer;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///[setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeObjects();

        setControlListeners();
        getStoragePermission();
    }


    @Override
    public void songUpdatedListener() {
        setControlVariablesAndIcons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;

        if (checkStoragePermission()) {
            initializeService();
        }
        setControlVariablesAndIcons();
        setControllerLayout();
        if (checkStoragePermission())
            setTimer();
    }

    private boolean checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }

    boolean isPaused;

    private void setTimer() {

        new Thread(new UpdateTimeRunnable()).start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /*if(MusicService.getMediaplayerStatus()){
                    tvTimer.setText(MusicService.getCurrentDuration());
                }else {
                    tvTimer.setText("00:00");
                }*/

                if (!isPaused)
                    setTimer();
                else
                    makeToast("Timer stopped in main activity");
            }
        }, 950);
    }

    /*public boolean mServiceBound;

    public ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
            showShortToast("on service disconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.ServiceBinder myBinder = (MusicService.ServiceBinder) service;
            musicService = myBinder.getService();
            musicService.registerClient(MainActivity.this);
            mServiceBound = true;
            showShortToast("sevice connected");
        }
    };
*/
    private MusicService musicService;

    public void initializeService() {

        /*
        *
        Intent playIntent = new Intent(this, MusicService.class);;
        if (!isMyServiceRunning(MusicService.class)) {
            startService(playIntent);
        }
        bindService(playIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        */

        Intent playIntent = new Intent(this, MusicService.class);
        startService(playIntent);
//      bindService(playIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        MusicService.registerClient(MainActivity.this);

    }



    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        /*if(mServiceBound){
            //unbindService(mServiceConnection);
            musicService.unRegisterClient(MainActivity.this);
            Toast.makeText(MainActivity.this, "Unregistered client", Toast.LENGTH_SHORT).show();
        }*/
        MusicService.unRegisterClient();
    }

    private void setControlVariablesAndIcons() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MusicService.isPlaying()) {
                    ivPlayPause.setImageResource(R.mipmap.ic_pause_black);
                } else {
                    ivPlayPause.setImageResource(R.mipmap.ic_play_black);
                }
            }
        }, 500);

        TextView tvTitle = (TextView) findViewById(R.id.tvPlayTitle);
        TextView tvSubTitle = (TextView) findViewById(R.id.tvPlaySubTitle);


        tvTitle.setText(String.format("%s", sharedPreferences.getString(Config.CURRENT_SONG_TITLE, "")));
        tvSubTitle.setText(String.format("%s", sharedPreferences.getString(Config.CURRENT_SONG_SUB_TITLE, "")));

//        MusicService.setIsQueueModeOn(sharedPreferences.getBoolean(Config.QUEUE_MODE, true));
//        MusicService.setShuffleMode(sharedPreferences.getBoolean(Config.QUEUE_MODE, false));

    }

    private void makeToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

   /* private void initializeService() {

        if(!super.isMyServiceRunning(MusicService.class)){
            Intent playIntent = new Intent(this, MusicService.class);
            startService(playIntent);
            if(!mServiceBound)
                bindService(playIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }*/


    private void initializeObjects() {
        sharedPreferences = getSharedPreferences("MYPREF", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ivPlayPause = (ImageView) findViewById(R.id.play_pause);
        tvTimer = (TextView) findViewById(R.id.tvTime);

        RealmResults<NewSong> resultSet = RealmController.with(this).getSongsQueue();
        MusicService.setSongsQueue(new ArrayList<>(resultSet));

        //this.realm = RealmController.with(this).getRealm();

    }

    private void setControllerLayout() {
        if (RealmController.with(this).hasSongsInQueue()) {

            Animation animBottomUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_up);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            findViewById(R.id.parentController).setLayoutParams(params);

            tvTimer.startAnimation(animBottomUp);

        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            findViewById(R.id.parentController).setLayoutParams(params);
        }
    }

    private void setControlListeners() {

        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MusicService.isPlaying()) {
                    ivPlayPause.setImageResource(R.mipmap.ic_play_black);
                    MusicService.pauseSong();
                } else {
                    ivPlayPause.setImageResource(R.mipmap.ic_pause_black);
                    if (MusicService.getMediaplayerStatus()) {
                        MusicService.playOrResumeSong();
                    } else {
                        Toast.makeText(MainActivity.this, "player is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private int getColorForTab(int position) {
        Resources resources = getResources();
        switch (position) {
            case 0:
                return resources.getColor(R.color.trackcolor);
            case 1:
                return resources.getColor(R.color.albumscolor);
            case 2:
                return resources.getColor(R.color.artistscolor);
            case 3:
                return resources.getColor(R.color.genrescolor);
            case 4:
                return resources.getColor(R.color.folderscolor);
        }

        return resources.getColor(R.color.trackcolor);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_queue) {
            startActivity(new Intent(MainActivity.this, QueueActivity.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), 0);
        } else if (id == R.id.activity_play) {
            startActivity(new Intent(MainActivity.this, MainPlayActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void songStartedListener() {
        setControllerLayout();
        setControlVariablesAndIcons();
    }

    @Override
    public void addSongListener(NewSong newSong) {

        super.addSongListener(newSong);
        setControllerLayout();
        setControlVariablesAndIcons();

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AllTracksFragment.newInstance(MainActivity.this);
                case 1:
                    return GenresFragment.newInstance();
                case 2:
                    return AlbumsFragment.newInstance();
                case 3:
                    return ArtistFragment.newInstance();
                case 4:
                    return FolderFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Playlist";
                case 1:
                    return "Genres";
                case 2:
                    return "Albums";
                case 3:
                    return "Artist";
                case 4:
                    return "Folders";
            }
            return null;
        }
    }

    private void getStoragePermission() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                //makeToast("Should show Request permission");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission so it can take screenshot of the best score");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (sharedPreferences.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                //makeToast("Previously Permission Request was cancelled with 'Dont Ask Again'");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission so it can take screenshot of the best score");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        {

                        }
                    }
                });
                builder.show();
            } else {
                //makeToast("Showing the permission request dialog");
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            gotPermission();
        }
    }

    private void gotPermission() {
        initializeService();

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                gotPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission so it can take screenshot of the best score");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                gotPermission();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                sentToSettings = false;
                gotPermission();
            }
        }
    }

    private class UpdateTimeRunnable implements Runnable {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (MusicService.getMediaplayerStatus()) {
                        tvTimer.setText(MusicService.getCurrentDuration());
                    } else {
                        tvTimer.setText("00:00");
                    }
                }
            });
        }
    }
}

 /*  int color = getColorForTab(tabLayout.getSelectedTabPosition());
        toolbar.setBackgroundColor(color);
        tabLayout.setBackgroundColor(color);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab selectedTab) {
                int colorFrom = ((ColorDrawable) toolbar.getBackground()).getColor();
                int colorTo = getColorForTab(selectedTab.getPosition());
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int color = (int) animator.getAnimatedValue();

                        toolbar.setBackgroundColor(color);
                        tabLayout.setBackgroundColor(color);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                    }

                });
                colorAnimation.start();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/