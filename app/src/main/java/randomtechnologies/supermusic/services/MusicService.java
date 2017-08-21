package randomtechnologies.supermusic.services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import randomtechnologies.supermusic.BaseHandler;
import randomtechnologies.supermusic.MainActivity;
import randomtechnologies.supermusic.helper.Config;
import randomtechnologies.supermusic.helper.ISongUpdateListener;
import randomtechnologies.supermusic.helper.NotificationUtils;
import randomtechnologies.supermusic.model.NewSong;

/**
 * Created by Rishab Surana  on 06-08-2017.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static MediaPlayer player;
    private static ArrayList<NewSong> queue = new ArrayList<>();
    private static int currentSongPosition;
    private static Context context;

    private static String songTitle="";

    private static final int NOTIFY_ID = 12345;
    private static Random rand;

    private static long currentSongId;
    private static int length;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static ISongUpdateListener listener;


    private IBinder binder = new ServiceBinder();


    public static void registerClient(BaseHandler activtity) {
        listener = activtity;
    }

    public static void unRegisterClient(){
        listener = null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        currentSongPosition =0;
        context = getApplicationContext();

        rand=new Random();
        sharedPreferences = context.getSharedPreferences("MYPREF", context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //initialize
        initMusicPlayer();
        //Toast.makeText(MusicService.this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    public void initMusicPlayer(){

        player = new MediaPlayer();

        //set player properties
        player.setWakeMode(context,
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //set listeners
        player.setOnPreparedListener(this);

        player.setOnErrorListener(this);

        player.setOnCompletionListener(this);
    }

    //pass song list
    public static void addSongToListAndPlayIfNotPlayingAnyOtherSong(NewSong song){
        if(queue.contains(song)){

            if(!player.isPlaying()){
                currentSongPosition = queue.indexOf(song);
                playSong();
            }else {
               // makeToast("Song already in queue");
            }
        }else{
            queue.add(song);
            if(!player.isPlaying()){
                currentSongPosition = queue.size() - 1;
                playSong();
            }
            //makeToast("Songs added in queue...");
        }

    }

    private static void makeToast(String s) {
       // Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

    }

    public static void addSongToListAndPlay(NewSong newSong) {
        if(queue.contains(newSong)){
            currentSongPosition = queue.indexOf(newSong);
            playSong();
        }else {
            queue.add(newSong);
            currentSongPosition = queue.size() - 1;
            playSong();
        }

    }

    // will not bind to any activity
    @Override
    public IBinder onBind(Intent intent) {
       return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        listener = null;
        Toast.makeText(context, "unbind", Toast.LENGTH_SHORT).show();
//        return super.onUnbind(intent);
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        makeToast("On start command");
        //Todo:Need to check what this constants do
        return Service.START_REDELIVER_INTENT;
    }

    private static NewSong currentSong;

    //an independent core method to play song
    public static void playSong(){

        player.reset();
        if(queue.isEmpty()){
            makeToast("que is empty ");
            return;
        }
/*
        if(currentSongPosition >= queue.size()){
            currentSongPosition = queue.size() - 1;
        }*/
        currentSong = queue.get(currentSongPosition);
        songTitle=currentSong.getTitle();
        currentSongId = currentSong.getId();

        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSongId);

        try {
            player.setDataSource(context, trackUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

            if(isQueueModeOn()){
                playNext();
            }else {
                playSong();
            }

        // this shit was giving 0 after some days
/*
        if (mp.getCurrentPosition() >  0) {

            if(isQueueModeOn()){
              playNext();
            }else {
                playSong();
            }
        }*/
        Log.e("tag" , "completed" + mp.getCurrentPosition());


        /*
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
        */
    }

    private static boolean isQueueModeOn() {
        return sharedPreferences.getBoolean(Config.QUEUE_MODE, true);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
       // makeToast("on error");
        mp.reset();
        return true;
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        //start playback


        /*mp.start();
        setSharedPrefsForCurrentSong();
        //notification
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
        Log.e("tag" , "onprepared");*/

        //makeToast("on prepared");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mp.start();
            }
        });
        thread.start();
        setSharedPrefsForCurrentSong();

        try{
            if(listener != null){
                Toast.makeText(context, "lis is not null", Toast.LENGTH_SHORT).show();
                listener.songUpdatedListener();
            }
            else {
                Toast.makeText(context, "listener is null", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("chutiyapa", e.toString());
        }

        NotificationUtils notificationManager = new NotificationUtils(context);
        notificationManager.makeNotification(currentSong.getTitle(), MusicService.isPlaying());
    }

    private void setSharedPrefsForCurrentSong() {

        editor.putInt(Config.SONG_CURRENT_POSITION, currentSongPosition);

        editor.putLong(Config.CURRENT_SONG_ID, currentSong.getId());
        editor.putString(Config.CURRENT_SONG_TITLE, currentSong.getTitle());
        editor.putString(Config.CURRENT_SONG_SUB_TITLE, currentSong.getArtist());
        editor.putString(Config.CURRENT_SONG_ARTIST, currentSong.getArtist());
        editor.putString(Config.CURRENT_SONG_DISPLAY_NAME, currentSong.getDisplayName());
        try{
            editor.putInt(Config.CURRENT_SONG_DURATION, Integer.parseInt(currentSong.getDuration()));
        }catch(Exception e){
            editor.putInt(Config.CURRENT_SONG_DURATION,0);
        }
        editor.putString(Config.CURRENT_SONG_ALBUM, currentSong.getAlbum());
        editor.putLong(Config.CURRENT_SONG_ALBUM_ID, currentSong.getAlbumId());
        editor.putLong(Config.CURRENT_SONG_ARTIST_ID, currentSong.getArtistId());

        editor.commit();

    }

    //skip to previous track
    public static void playPrev(){
        currentSongPosition--;
        if(currentSongPosition <0) currentSongPosition =queue.size()-1;
        playSong();
    }

    //skip to next
    public static void playNext(){
        if(isShuffleModeOn()){
            int newSong = currentSongPosition;
            while(newSong== currentSongPosition && queue.size() != 1){
                newSong=rand.nextInt(queue.size());
            }
            currentSongPosition = newSong;
            makeToast("Shuffle is on and random chosen position is ==>" + currentSongPosition);
        }
        else{
            currentSongPosition++;
            if(currentSongPosition >=queue.size()) currentSongPosition =0;
            makeToast("Shuffle is off and position in queue is of song number ==>" + currentSongPosition);
        }
        playSong();
    }

    private static boolean isShuffleModeOn() {
        return sharedPreferences.getBoolean(Config.SHUFFLE_MODE, false);
    }

    @Override
    public void onDestroy() {
        if(player != null)
            player.release();
        Log.e("Tag", "ONDesrtoy");
        makeToast("Service is destroyed");
    }

    public static boolean getMediaplayerStatus() {
        if(player == null && !player.isPlaying()){
            makeToast("getMediaPlayerStatus ==> \nplayer is null \n player is not playing");
            return false;
        }
       // makeToast("Player is not null");
        return true;
    }

    public static void pauseSong() {
        if(player.isPlaying()){
            player.pause();
            length = player.getCurrentPosition();
        }
    }

    public static void playOrResumeSong() {


        if(!player.isPlaying()){
            player.seekTo(length);
            player.start();

            if(!player.isPlaying()){
                // this means that service is service is again created player is not going to start
                // from above statement
                currentSongPosition = getCurrentSongPosition();
                makeToast(queue.size() + " ==> size" + currentSongPosition + " ==> current");
                playSong();
            }else{
                makeToast("dont worry player is already playing");
            }
        }else {
            playSong();
            makeToast("play song called");
        }
    }

    public static void setSongsQueue(ArrayList<NewSong> newSongs) {
        queue = newSongs;
    }

    public static int getCurrentPosition(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public static boolean isPlaying(){
        if(player != null)
            return player.isPlaying();
        return false;
    }

    public static void seek(int posn){
        length = posn;
        if(MusicService.isPlaying()){
            player.seekTo(length);
        }
    }

    public static String getCurrentDuration() {
        String durationString = "";
        int hour;
        int mins;
        int seconds;
        try{

            int length = player.getCurrentPosition();
            length /= 1000;

            seconds = length % 60;
            mins = length / (60);
            hour = length / (60*60);

            if(hour > 0)
                durationString = hour + ":";

            if(mins == 0){
                durationString += ("00:");
            }else if(mins > 0 && mins < 10){
                durationString += ("0" + mins + ":");
            }else if(mins >= 10){
                durationString += (mins + ":");
            }

            if(seconds == 0){
                durationString += "00";
            }else if(seconds < 10){
                durationString += "0" + seconds;
            }else if(seconds >= 10){
                durationString += (seconds + "");
            }


            return durationString;

        }catch (Exception e){
            return currentSong.getDuration();
        }
    }

    public static ArrayList<NewSong> getSongsList() {
        return queue;
    }

    public static int getCurrentSongPosition() {
        return sharedPreferences.getInt(Config.SONG_CURRENT_POSITION,0);
    }



    public class ServiceBinder extends Binder {

        public MusicService getService(){
            return MusicService.this;
        }
    }
}


    /*//toggle shuffle
    public static void toggleShuffleMode(){
        isShuffleModeOn = !isShuffleModeOn;
    }

    public static void setShuffleMode(boolean shuffleMode){
        isShuffleModeOn = shuffleMode;
    }

    public static void setIsQueueModeOn(boolean queueMode){
        isQueueModeOn = queueMode;
    }

    public static void toggleQueueMode(){
        isQueueModeOn = !isQueueModeOn;
    }*/
