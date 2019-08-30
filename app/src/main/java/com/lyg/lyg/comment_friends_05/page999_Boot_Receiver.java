package com.lyg.lyg.comment_friends_05;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by L on 2017-07-12.
 */

public class page999_Boot_Receiver extends BroadcastReceiver {

    //notification
    Notification Notifi;
    NotificationManager Notifi_M;

        public void onReceive(Context context, Intent intent) {

            //17.08.04 - 필요에 대한 의문 -> 삭제
            /*
            //set_notification
            set_Notification(context);
            */

        }

    //부팅 완료 시 알림창 -> 클릭 시 서비스 및 어플리케이션 실행
    private void set_Notification(Context context){
        Notifi_M = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, page999_Service_Always_Top.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notifi = new Notification.Builder(context)
                .setContentTitle("LINK feed")
                .setContentText("링크 복사 후 클릭하세요.")
                .setSmallIcon(R.drawable.tablayout_comments_white)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        Notifi_M.notify( 888 , Notifi);
    }

}
