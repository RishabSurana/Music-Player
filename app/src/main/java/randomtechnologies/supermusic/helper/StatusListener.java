package randomtechnologies.supermusic.helper;

import randomtechnologies.supermusic.model.NewSong;

/**
 * Created by Rishab.s on 8/12/2017.
 */

public interface StatusListener {

    void songStartedListener();

    void addSongListener(NewSong newSong);
}
