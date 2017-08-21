package randomtechnologies.supermusic.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by HP on 10-08-2017.
 *//*
public class NewSong {

    *//*public long id;
    public String title;
    public String artist;
    public String data;
    public String displayName;
    public String duration;
    public String album;
    public String albumId;
    public String albumKey;
    public String artistId;
    public String bookmark;
    public String composer;
    public String track;*//*




  *//*  public NewSong(long id, String string, String string1, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11) {


        this.id = id;
        this.title = string;
        this.artist = string1;
        this.data = string2;
        this.displayName = string3;
        this.duration = string4;
        this.album = string5;
        this.albumId = string6;
        this.albumKey = string7;
        this.artistId = string8;
        this.bookmark = string9;
        this.composer = string10;
        this.track = string11;
    }*//*


    public long id;
    public String title;
    public String artist;
    public String data;
    public String displayName;
    public String duration;
    public String album;
    public long albumId;
    public long artistId;

    public NewSong(long id, String title, String artist, String data, String displayName, String duration, String album, long albumId, long artistId) {


        this.id = id;
        this.title = title;
        this.artist = artist;
        this.data = data;
        this.displayName = displayName;
        this.duration = duration;
        this.album = album;
        this.albumId = albumId;
        this.artistId = artistId;

    }

    public String getDurationString(){

        String durationString = "";
        long hour;
        long mins;
        long seconds;
        try{

            long length = Long.parseLong(duration);
            length %= 1000;

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



    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getData() {
        return data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbum() {
        return album;
    }

    public long getAlbumId() {
        return albumId;
    }



    public long getArtistId() {
        return artistId;
    }

  *//*  public String getTrack() {
        return track;
    }

    public String getAlbumKey() {
        return albumKey;
    }
    public String getBookmark() {
        return bookmark;
    }

    public String getComposer() {
        return composer;
    }*//*
}*/



public class NewSong extends RealmObject {



    @PrimaryKey
    private long id;

    private String title;
    private String artist;
    private String data;
    private String displayName;
    private String duration;
    private String album;
    private long albumId;
    private long artistId;

    public NewSong(){}

    public NewSong(long id, String title, String artist, String data, String displayName, String duration, String album, long albumId, long artistId) {


        this.id = id;
        this.title = title;
        this.artist = artist;
        this.data = data;
        this.displayName = displayName;
        this.duration = duration;
        this.album = album;
        this.albumId = albumId;
        this.artistId = artistId;

    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }
}