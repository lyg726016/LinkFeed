package com.lyg.lyg.comment_friends_05;

import android.widget.CheckBox;

/**
 * Created by L on 2017-06-27.
 */

public class page999_Contact_for_Listview {
    private String phone_number="";
    private String name = "";
    private CheckBox checkBox;
    //17.07.02 checkbox 여러개 클릭되는 오류
    private int is_selected = 0;


    public page999_Contact_for_Listview(String name, String phone_number) {
        super();
        this.name = name;
        this.phone_number = phone_number;
    }

    public String get_Phone_number() {
        return phone_number;
    }
    public void set_phone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String get_name() {
        return name;
    }
    public void set_name(String name) {
        this.name = name;
    }
    public void set_CheckBox(CheckBox checkBox)
    {
        this.checkBox = checkBox;
    }
    public CheckBox get_checkBox()
    {
        return this.checkBox;
    }
    //17.07.02 checkbox 여러개 클릭되는 오류

    public void set_is_selected(int value){
        this.is_selected = value;
    }
    public int get_is_selected(){
        return this.is_selected;
    }


}
