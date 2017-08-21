package randomtechnologies.supermusic.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import randomtechnologies.supermusic.MainPlayActivity;
import randomtechnologies.supermusic.R;

/**
 * Created by HP on 16-08-2017.
 */

public class NotificationUtils {

    Context mContext;

    public NotificationUtils(Context mContext){
        this.mContext = mContext;
    }

    public void makeNotification(String title, boolean isPlaying){

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_layout);

        Intent intent = new Intent(mContext, MainPlayActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity
                (mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setTextViewText(R.id.tvNotifTitle, title);
        remoteViews.setTextColor(R.id.tvNotifTitle, mContext.getColor(R.color.colorAccent));

        remoteViews.setImageViewResource(R.id.notif_play_pause,
                isPlaying ? R.mipmap.ic_pause_black : R.mipmap.ic_play_black);

        remoteViews.setImageViewResource(R.id.notif_next, R.mipmap.ic_next_black);
        remoteViews.setImageViewResource(R.id.notif_previous,R.mipmap.ic_previous_black);

        remoteViews.setImageViewBitmap(R.id.notif_play_pause, BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_play));
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(mContext).setAutoCancel(false).setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);

        NotificationManager notificationmanager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        Notification notification = builder.build();
        notification.contentView = remoteViews;
        notification.contentIntent = pendingIntent;
        notificationmanager.notify(12345, notification);
    }
}