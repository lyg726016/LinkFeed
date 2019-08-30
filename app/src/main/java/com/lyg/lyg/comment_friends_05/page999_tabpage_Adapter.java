package com.lyg.lyg.comment_friends_05;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Junyoung on 2016-06-23.
 */

public class page999_tabpage_Adapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public page999_tabpage_Adapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                page1 page1 = new page1();
                return page1;
            case 1:
                page2 page2 = new page2();
                return page2;
            case 2:
                page3 page3 = new page3();
                return page3;
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}