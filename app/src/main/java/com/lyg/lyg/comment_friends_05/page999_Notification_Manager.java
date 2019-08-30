package com.lyg.lyg.comment_friends_05;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by L on 2017-08-07.
 */

//Notification에서 close 버튼 클릭 시 id를 통해 notification 제거 -> 정상작동 -> 알림창이 제거 됐는 지 activity에서는 확인 불가 -> 문구 출력 부적합 발생

public class page999_Notification_Manager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}
