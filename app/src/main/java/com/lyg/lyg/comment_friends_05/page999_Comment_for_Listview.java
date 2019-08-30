package com.lyg.lyg.comment_friends_05;

/**
 * Created by L on 2017-06-19.
 */

public class page999_Comment_for_Listview {
    private String name = "";
    private String content = "";

    page999_Comment_for_Listview(String name, String content){
        this.name = name;
        this.content = content;
    }

    public String getComment() {
        return content;
    }

    public void setComment(String sequence) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
