package randomtechnologies.supermusic.model;

/**
 * Created by Rishab.s on 8/3/2017.
 */

public class Song {

    private long id;
    private String title;
    private String artist;
    private String album;
    private String length;
    private String displayName;
    private boolean isMusic;
    private long albumId;

    public Song(long songID, String songTitle, String songArtist, String thisAlbum,
                String thisDuration, String thisDisplayName, boolean isMusic, long albumId) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        album = thisAlbum;
        length = thisDuration;
        displayName = thisDisplayName;
        this.isMusic = isMusic;
        this.albumId = albumId;
    }

    public long getId() {
        return id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}

    public String getDisplayName() {
        return displayName;
    }

    public boolean getIsMusic() {
        return isMusic;
    }

    public String getAlbum() {
        return album;
    }

    public String getLength() {
        return length;
    }

}
