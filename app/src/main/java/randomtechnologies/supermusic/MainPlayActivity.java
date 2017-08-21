package randomtechnologies.supermusic;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

import randomtechnologies.supermusic.helper.Config;
import randomtechnologies.supermusic.model.NewSong;
import randomtechnologies.supermusic.services.MusicService;

public class MainPlayActivity extends BaseHandler {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    static ArrayList<NewSong> songsList = new ArrayList<>();
    int currentSongPosition;

    TextView tvStart, tvEnd;
    TextView tvTitle, tvSecTitle;
    ImageView ivPlayPause, ivNext, ivPrevious;
    ImageView ivShuffle, ivQueue;

    SeekBar sbProgress;
    private boolean isPaused;

    /*public boolean mServiceBound;
    private MusicService musicService;

    public ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
            showShortToast("on play service disconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.ServiceBinder myBinder = (MusicService.ServiceBinder) service;
            musicService = myBinder.getService();
            musicService.registerClient(MainPlayActivity.this);
            mServiceBound = true;
            showShortToast("sevice  play connected");
        }
    };*/
    private float OFFSET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_play);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewsById();
        setControlImages();
        setControlListeners();

        songsList = MusicService.getSongsList();
        currentSongPosition = MusicService.getCurrentSongPosition();
        Toast.makeText(this, songsList.size() + "", Toast.LENGTH_SHORT).show();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(100, 0, 100, 0);
        //mViewPager.setPaddingRelative(40, 0, 40, 0);
        mViewPager.setPageMargin(5);
//        mViewPager.setOffscreenPageLimit(10);

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {


                // code which worked but view pager was jumping
                /*showShortToast("on transform " + position);
                int pageWidth = mViewPager.getMeasuredWidth() -
                        mViewPager.getPaddingLeft() - mViewPager.getPaddingRight();
                int pageHeight = mViewPager.getHeight();
                int paddingLeft = mViewPager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() -
                        (mViewPager.getScrollX() + paddingLeft)) / pageWidth;
                int max = pageHeight / 10;

                if (transformPos < -1)
                { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(0.4f);// to make left transparent
                    page.setScaleY(0.7f);
                }
                else if (transformPos == 0)  // for center
                { // [-1,1]
                    page.setScaleY(1f);
                    page.setAlpha(1f);
                }
                else
                { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(0.4f);// to make right transparent
                    page.setScaleY(0.7f);
                }
*/

                int pageWidth = mViewPager.getMeasuredWidth() - mViewPager.getPaddingLeft() - mViewPager.getPaddingRight();
                int pageHeight = mViewPager.getHeight();
                int paddingLeft = mViewPager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() - (mViewPager.getScrollX() + paddingLeft)) / pageWidth;


                float normalizedposition = Math.abs(Math.abs(transformPos) - 1);

                if ((normalizedposition + 0.5f) > 1f) {
                    page.setAlpha(1f);
                    page.setScaleY(1f);
                } else {
                    page.setScaleY(normalizedposition + 0.5f);
                    page.setAlpha(normalizedposition + 0.5f);
                }

                /*int max = -pageHeight / 10;

                if (transformPos < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setTranslationY(0);
                } else if (transformPos <= 1) { // [-1,1]
                    page.setTranslationY(max * (1-Math.abs(transformPos)));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setTranslationY(0);
                }*/
            }
        });
    }


    private void setControlImages() {

        // play pause button
        if (MusicService.isPlaying()) {
            ivPlayPause.setImageResource(R.mipmap.ic_pause_black);
        } else {
            ivPlayPause.setImageResource(R.mipmap.ic_play_black);
        }

        // shuffle button
        if (getPassiveBooleanSharedPrefs(Config.SHUFFLE_MODE)) {
            ivShuffle.setColorFilter(ContextCompat.getColor(MainPlayActivity.this,
                    R.color.folderscolor), PorterDuff.Mode.MULTIPLY);
            //showShortToast("Shuffle on");
        } else {
            ivShuffle.setColorFilter(Color.argb(255, 0, 0, 0));
            //showShortToast("Shuffle off");
        }

        // queue button
        if (getActiveSharedPrefs(Config.QUEUE_MODE)) {
            ivQueue.setColorFilter(ContextCompat.getColor(MainPlayActivity.this,
                    R.color.albumscolor), PorterDuff.Mode.MULTIPLY);
            showShortToast("Queue on");
        } else {
            ivQueue.setColorFilter(Color.argb(255, 0, 0, 0));
            showShortToast("Queue off");
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
                        Toast.makeText(MainPlayActivity.this, "player is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicService.playNext();
            }
        });


        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicService.playPrev();
            }
        });


        ivShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBooleanSharedPrefs(Config.SHUFFLE_MODE);
                if (getPassiveBooleanSharedPrefs(Config.SHUFFLE_MODE)) {
                    ivShuffle.setColorFilter(ContextCompat.getColor(MainPlayActivity.this,
                            R.color.folderscolor), PorterDuff.Mode.MULTIPLY);
                    showShortToast("Shuffle on");

                } else {
                    ivShuffle.setColorFilter(Color.argb(255, 0, 0, 0));
                    showShortToast("Shuffle off");
                }
            }
        });

        ivQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBooleanSharedPrefs(Config.QUEUE_MODE);
                if (getActiveSharedPrefs(Config.QUEUE_MODE)) {
                    ivQueue.setColorFilter(ContextCompat.getColor(MainPlayActivity.this,
                            R.color.albumscolor), PorterDuff.Mode.MULTIPLY);
                    showShortToast("Queue on");
                } else {
                    ivQueue.setColorFilter(Color.argb(255, 0, 0, 0));
                    showShortToast("Queue off");
                }
            }
        });

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int progress = sbProgress.getProgress();
                if (MusicService.getMediaplayerStatus()) {
                    MusicService.seek(progress);
                }
            }
        });
    }

    private void setNewSongInPlayer() {
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREF", MODE_PRIVATE);
        sbProgress.setMax(sharedPreferences.getInt(Config.CURRENT_SONG_DURATION, Integer.MAX_VALUE));

        tvStart.setText("00:00");
        tvTitle.setText(sharedPreferences.getString(Config.CURRENT_SONG_TITLE, ""));
        tvSecTitle.setText(sharedPreferences.getString(Config.CURRENT_SONG_ARTIST, ""));
        tvEnd.setText(Config.getDurationString(sharedPreferences.getInt(Config.CURRENT_SONG_DURATION, 0) + ""));

        if (MusicService.getMediaplayerStatus()) {
            sbProgress.setProgress(MusicService.getCurrentPosition());
        } else {
            sbProgress.setProgress(0);
        }

        currentSongPosition = sharedPreferences.getInt(Config.SONG_CURRENT_POSITION, 0);
        mViewPager.setCurrentItem(currentSongPosition, true);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songsList.get(currentSongPosition).getData());


        data = mmr.getEmbeddedPicture();

        if (data != null) {
            ivBgBlur.animate().setDuration(750).alpha(0f);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    ivBgBlur.setImageBitmap(blur(BitmapFactory.decodeByteArray(data, 0, data.length)));
                    ivBgBlur.animate().setDuration(500).alpha(1f);
                }
            }, 750);


        }else{
            showShortToast("data is null");
        }

    }
    byte[] data;
    private static final float BLUR_RADIUS = 25f;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        initializeService();
        setMajorPlayerVariables();
        setNewSongInPlayer();
    }

    private void initializeService() {

        Intent playIntent = new Intent(this, MusicService.class);
        startService(playIntent);
        MusicService.registerClient(MainPlayActivity.this);
//        getApplicationContext().bindService(playIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;

        MusicService.unRegisterClient();

        /*if (mServiceBound)
            getApplicationContext().unbindService(mServiceConnection);*/
    }

    private void setMajorPlayerVariables() {

      /*  Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                if(MusicService.getMediaplayerStatus() && MusicService.isPlaying()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sbProgress.setProgress(MusicService.getCurrentPosition());
                        }
                    });
                }
            }
        });
        thread.start();*/

        if (MusicService.getMediaplayerStatus() && MusicService.isPlaying()) {
            sbProgress.setProgress(MusicService.getCurrentPosition());
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isPaused) {
                    setMajorPlayerVariables();
                }
            }
        }, 950);
    }

    @Override
    public void songUpdatedListener() {
        setNewSongInPlayer();
    }

    private void findViewsById() {

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvSecTitle = (TextView) findViewById(R.id.tvSecTitle);

        tvStart = (TextView) findViewById(R.id.tvCurrentTime);
        tvEnd = (TextView) findViewById(R.id.tvTotalTime);

        ivPlayPause = (ImageView) findViewById(R.id.ivPlayPause);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivPrevious = (ImageView) findViewById(R.id.ivPrevious);
        ivShuffle = (ImageView) findViewById(R.id.ivShuffle);
        ivQueue = (ImageView) findViewById(R.id.ivQueue);

        ivBgBlur = (ImageView) findViewById(R.id.ivBgBlur);

        sbProgress = (SeekBar) findViewById(R.id.seek);
    }

    ImageView ivBgBlur;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        TextView textView;
        ImageView ivArt;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_play, container, false);
            textView = (TextView) rootView.findViewById(R.id.section_label);
            ivArt = (ImageView) rootView.findViewById(R.id.ivArt);
            textView.setText(songsList.get(getArguments().getInt(ARG_SECTION_NUMBER) - 1).getTitle());


            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(songsList.get(getArguments().getInt(ARG_SECTION_NUMBER) - 1).getData());

            byte[] data = mmr.getEmbeddedPicture();

            if (data != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                ivArt.setImageBitmap(bm);
            }
            // textView.setText(songsList.size()+"");
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // return 0;
            return songsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position + "";
        }
    }
}
