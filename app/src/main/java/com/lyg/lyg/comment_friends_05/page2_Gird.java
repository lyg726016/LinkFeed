package com.lyg.lyg.comment_friends_05;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by L on 2017-06-17.
 */

public class page2_Gird {
    private Bitmap image;
    private String phone_number="";
    private int emotion = 0;
    private String name = "";
    private String url = "";


    public page2_Gird(String name, String phone_number, String url, int emotion) {
        super();
        this.name = name;
        this.emotion = emotion;
        this.phone_number = phone_number;
        this.url = url;
    }

    public Bitmap get_Image() {
        return image;
    }
    public void set_Image(Bitmap image) {
        this.image = image;
    }

    public String get_Phone_number() {
        return phone_number;
    }
    public void set_phone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int get_emotion() {
        return emotion;
    }
    public void set_emotion(int emotion) {
        this.emotion = emotion;
    }

    public String get_name() {
        return name;
    }
    public void set_name(String name) {
        this.name = name;
    }

    public String get_url() {
        return url;
    }
    public void set_url(String url) {
        this.url = url;
    }
}