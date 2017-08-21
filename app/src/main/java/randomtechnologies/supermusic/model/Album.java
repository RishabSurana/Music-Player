package randomtechnologies.supermusic.model;

/**
 * Created by HP on 09-08-2017.
 */
public class Album {

    long id;
    String title;
    String path;

    public Album(long thisId, String thisTitle, String path) {
        this.id = thisId;
        this.title = thisTitle;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }
}
