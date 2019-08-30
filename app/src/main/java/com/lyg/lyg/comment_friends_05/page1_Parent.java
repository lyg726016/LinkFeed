package com.lyg.lyg.comment_friends_05;

import java.util.ArrayList;

/**
 * Created by L on 2017-05-20.
 */

public class page1_Parent {
    private String name;
    private ArrayList<page1_Child> list = new ArrayList<page1_Child>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<page1_Child> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<page1_Child> productList) {
        this.list = productList;
    }
}
