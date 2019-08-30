package com.lyg.lyg.comment_friends_05;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by L on 2016-02-25.
 */
public class page999_Sms_Incoming extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    //message_tokenizer
    private static StringTokenizer stringTokenizer;
    private String phoneNumber;
    private String message;
    private String name;
    private Date date;
    //comment_friend_message
    private String [] comment_friend_message;
    //notification
    Notification Notifi;
    NotificationManager Notifi_M;



    //sms
    SmsMessage currentMessage;
    //db
    page999_Database_Manager database_manager;


    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        database_manager =  new page999_Database_Manager(context);
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
        phoneNumber = currentMessage.getDisplayOriginatingAddress().toString();
        message = currentMessage.getDisplayMessageBody().toString();
        name = database_manager.get_Name_as_Phone_Number(phoneNumber);
        if(name == null)
        {
            name = "Unknown Friend";
        }
        if(message.contains("comment_friend"))
        {
            comment_friend_message = message.split("<!%");
            //intent로 activity에 값 넘겨(phoneNumber, comment_friend_message[])
            //comment_friend_message[0] = "comment_friend" / 1 = url / 2 = 댓글 내용 / 3 = emotion / 4 = type(text, image, else)
            try {
                database_manager.insert_Comments(phoneNumber,
                        name,
                        comment_friend_message[1],
                        comment_friend_message[2],
                        getDateTime(),
                        Integer.parseInt(comment_friend_message[3]),
                        0,
                        0);
            }catch (Exception e){
                Toast.makeText(context, "SMS recieve error", Toast.LENGTH_SHORT);
            }
            //17.06.29
            //다른 앱에서 작업을 하지 않도록 -> 그래도 작업함
            abortBroadcast();
            //17.07.01
            //문자 수신시 알림창에 추가 -> 누르면 어플 실행되고, 해당 웹뷰 실행되도록.
            set_Notification(context, comment_friend_message[1], name, comment_friend_message[2]);
        }else{
            //do nothing
        }
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    //문자 수신 시 알림창 -> 클릭 시 어플리케이션 실행
    private void set_Notification(Context context, String url, String name, String comment){
        //set_notification_manager
        Notifi_M = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, page999_Dialog_for_Webview.class);
        intent.putExtra("sms_url", url);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notifi = new Notification.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("LINK feed")
                .setContentText(name + " : " + comment)
                .setSmallIcon(R.drawable.tablayout_comments_white)
                .setTicker("친구에게 댓글이 도착했습니다.")
                .setContentIntent(pendingIntent)
                .build();

        Notifi.flags = Notification.FLAG_AUTO_CANCEL;
        Notifi_M.notify( 777 , Notifi);
    }


}