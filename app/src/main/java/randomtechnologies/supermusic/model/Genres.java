package randomtechnologies.supermusic.model;

import android.provider.MediaStore;

/**
 * Created by HP on 08-08-2017.
 */

public class Genres {

    public String name;
    public long id;


    public Genres(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
