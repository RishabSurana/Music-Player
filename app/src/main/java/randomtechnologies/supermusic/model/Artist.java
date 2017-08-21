package randomtechnologies.supermusic.model;

/**
 * Created by HP on 08-08-2017.
 */
public class Artist {

    public long id;
    public String name;

    public Artist(long thisId, String thisTitle) {

        id = thisId;
        name = thisTitle;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
