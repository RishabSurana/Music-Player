package randomtechnologies.supermusic;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.realm.Realm;
import randomtechnologies.supermusic.database.RealmController;
import randomtechnologies.supermusic.helper.ISongUpdateListener;
import randomtechnologies.supermusic.helper.StatusListener;
import randomtechnologies.supermusic.model.NewSong;
import randomtechnologies.supermusic.services.MusicService;

/**
 * Created by HP on 12-08-2017.
 */
public class BaseHandler extends AppCompatActivity implements StatusListener, ISongUpdateListener {


    private Realm realm;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private MusicService musicService;

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Toast.makeText(BaseHandler.this, "service is running", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

   /* public boolean mServiceBound;
    public ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
            showShortToast("on service disconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.ServiceBinder myBinder = (MusicService.ServiceBinder) service;
            musicService = myBinder.getService();
            mServiceBound = true;
            showShortToast("sevice connected");
        }
    };*/

    /*public void initializeService() {

        if(!isMyServiceRunning(MusicService.class)){
            Intent playIntent = new Intent(this, MusicService.class);
            startService(playIntent);
            if(!mServiceBound){
                bindService(playIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                showShortToast("hha service connected called");
            }

        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

       /* if(mServiceBound){
            unbindService(mServiceConnection);
            showShortToast("Service destroyed");
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.with(this).getRealm();
        sharedPreferences = getSharedPreferences("MYPREF", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean getPassiveBooleanSharedPrefs(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getActiveSharedPrefs(String key){
        return sharedPreferences.getBoolean(key, true);
    }

    protected void showShortToast(String message){
        Toast.makeText(BaseHandler.this, message, Toast.LENGTH_SHORT).show();
    }

    public void toggleBooleanSharedPrefs(String key){
        editor.putBoolean(key, !sharedPreferences.getBoolean(key, false));
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void songStartedListener() {

    }

    @Override
    public void addSongListener(NewSong newSong) {

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(newSong);
        realm.commitTransaction();
    }

    @Override
    public void songUpdatedListener() {

    }
}
