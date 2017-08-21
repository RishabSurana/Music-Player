package randomtechnologies.supermusic.helper;

/**
 * Created by HP on 10-08-2017.
 */

public class Config {

    //https://stackoverflow.com/questions/15049097/android-mediaplayer-reset-is-invoking-oncompletion
    //https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
    //https://stackoverflow.com/questions/4178682/how-to-start-a-new-thread-in-a-service

    // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
    // for navigation of sub songs

    // for activity and service
    // https://stackoverflow.com/questions/14695537/android-update-activity-ui-from-service
    //https://stackoverflow.com/questions/20594936/communication-between-activity-and-service
    //https://softwareengineering.stackexchange.com/questions/305986/how-should-a-service-communicate-with-an-activity-in-real-time

    //http://www.vogella.com/tutorials/AndroidServices/article.html#service-start-process-and-execution
    //http://www.truiton.com/2014/11/bound-service-example-android/

    //seek bar
    //https://stackoverflow.com/questions/4679540/how-do-i-make-a-seekbar-update-itself-after-touch

    //unbind service
    //https://stackoverflow.com/questions/8614335/android-how-to-safely-unbind-a-service

    // view pager animation in player activity
    //https://stackoverflow.com/questions/30572605/viewpager-show-next-and-before-item-preview-on-screen
    //https://stackoverflow.com/a/39662219
    //https://stackoverflow.com/a/32392838

    // how to use value animator
    //https://stackoverflow.com/questions/33870408/android-how-to-use-valueanimator
    public static final String NAVIGATION_SONGS_TYPE = "song_navigation_type";
    public static final String NAVIGATION_ID = "navigation_id";

    public static final String GENRES = "genres";
    public static final String ALBUMS = "albums";
    public static final String ARTISTS = "artists";


    // for shared preference

    public static final String QUEUE_MODE = "queue_mode";
    public static final String SHUFFLE_MODE = "shuffle_mode";


    public static final String SONG_CURRENT_POSITION = "song_current_position";
    public static final String CURRENT_SONG_TITLE = "current_song_title";
    public static final String CURRENT_SONG_SUB_TITLE = "current_song_sub_title";
    public static final String CURRENT_SONG_ID = "current_Song_id";
    public static final String CURRENT_SONG_ARTIST = "current_Song_artist";
    public static final String CURRENT_SONG_DISPLAY_NAME = "current_Song_display_name";
    public static final String CURRENT_SONG_DURATION = "current_Song_duration";
    public static final String CURRENT_SONG_ALBUM = "current_Song_album";
    public static final String CURRENT_SONG_ALBUM_ID = "current_Song_album_id";
    public static final String CURRENT_SONG_ARTIST_ID = "current_Song_artist_id";

    public static String getDurationString(String duration){

        String durationString = "";
        long hour;
        long mins;
        long seconds;
        try{

            long length = Long.parseLong(duration);
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
            return duration;
        }
    }

}




