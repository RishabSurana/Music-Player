package randomtechnologies.supermusic.database;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;
import randomtechnologies.supermusic.model.NewSong;

/**
 * Created by Rishab.s on 8/12/2017.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

 /*   //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(SongsQueue.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<SongsQueue> getSongsQueue() {

        return realm.where(SongsQueue.class).findAll();
    }

    //query a single item with the given id
    public SongsQueue getSong(String id) {

        return realm.where(SongsQueue.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasSongsInQueue() {
        return !realm.allObjects(SongsQueue.class).isEmpty();
    }

    //query example
    public RealmResults<SongsQueue> querySongs() {

        return realm.where(SongsQueue.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }*/



    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(NewSong.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<NewSong> getSongsQueue() {

        return realm.where(NewSong.class).findAll();
    }

    //query a single item with the given id
    public NewSong getSong(String id) {

        return realm.where(NewSong.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasSongsInQueue() {
        return !realm.allObjects(NewSong.class).isEmpty();
    }
}