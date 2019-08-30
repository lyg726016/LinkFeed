package com.lyg.lyg.comment_friends_05;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by L on 2017-05-12.
 */

public class page999_BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public page999_BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void onBackPressed_on_Dialog() {
            activity.finish();
    }

    private void showGuide() {
        toast = Toast.makeText(activity, "뒤로가기를 한번 더 누르면 종료됩니다",
                Toast.LENGTH_SHORT);
        toast.show();
    }
}