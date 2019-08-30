package com.lyg.lyg.comment_friends_05;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;


/**
 * Created by Junyoung on 2016-06-23.
 */

public class page2 extends Fragment {

    private View rootView;

    //grid_view_for_un_read_comment
    private GridView gridView;
    private page2_GridView_Adapter gridAdapter;
    private ArrayList<page2_Gird> gridArrayList = new ArrayList<page2_Gird>();
    //grid_view_for_read_comment
    private GridView gridView_for_read;
    private page2_GridView_Adapter_for_read_comment gridAdapter_for_read;
    private ArrayList<page2_Gird_for_read> gridArrayList_for_read = new ArrayList<page2_Gird_for_read>();
    //database_manager
    private page999_Database_Manager database_manager;
    private Cursor not_read_comment_cursor;
    private Cursor read_comment_cursor;
    //comment_info
    private String name = "";
    private String phone_number = "";
    private int emotion = 0;
    private String url = "";

    //17.07.13
    //admob
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        rootView = inflater.inflate(R.layout.page2, container, false);
        database_manager = new page999_Database_Manager(this.getContext());


        //database_manager.insert_Comments("1", "1", "1", "1", "2017-06-17", 1, 0,0);
        //GRID_VIEW
        gridView = (GridView) rootView.findViewById(R.id.page2_gird_view);
        gridAdapter = new page2_GridView_Adapter(getContext(), R.layout.page2_grid_for_unread_comment, gridArrayList);
        gridView.setAdapter(gridAdapter);

        get_un_read_comment();

        //GRID_VIEW_for_read - 17.07.02 제거 -> 구글 ads로 변경
        /*
        gridView_for_read = (GridView) rootView.findViewById(R.id.page2_last_comment_grid_view);
        gridAdapter_for_read = new page2_GridView_Adapter_for_read_comment(getActivity(), R.layout.page2_grid_for_unread_comment, gridArrayList_for_read);
        gridView_for_read.setAdapter(gridAdapter_for_read);
        */

        //GRID_VIEW_for_read - 17.07.02 제거 -> 구글 ads로 변경
        //get_read_comment();

        //17.07.13
        //admob
        MobileAds.initialize(getContext(), "ca-app-pub-4142137880317559~2585524228");
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });

        return rootView;
    }

    public void get_un_read_comment()
    {
        not_read_comment_cursor = database_manager.get_Not_Read_Comment();
        if(not_read_comment_cursor != null) {
            if (not_read_comment_cursor.moveToFirst()&& not_read_comment_cursor.getCount() != 0) {
                do {
                    name = not_read_comment_cursor.getString(not_read_comment_cursor.getColumnIndex("NAME"));
                    phone_number = not_read_comment_cursor.getString(not_read_comment_cursor.getColumnIndex("PHONE_NUMBER"));
                    url= not_read_comment_cursor.getString(not_read_comment_cursor.getColumnIndex("URL"));
                    emotion = not_read_comment_cursor.getInt(not_read_comment_cursor.getColumnIndex("EMOTION"));
                    //add to gridview
                    page2_Gird page2_grid = new page2_Gird(name, phone_number, url, emotion);
                    gridAdapter.add(page2_grid);
                } while (not_read_comment_cursor.moveToNext());
                not_read_comment_cursor.close();
            } else {
                Log.d("PAGE2_ONCREATE", "not_read_comment_cursor moveToFirst end");
            }
        }else{
            page2_Gird page2_grid = new page2_Gird("새로운 댓글이 없습니다", phone_number, "http://blog.naver.com/lyg2717", 1);
            gridAdapter.add(page2_grid);


            Log.d("PAGE2_ONCREATE", "not_read_comment_cursor is null");
        }
        gridAdapter.notifyDataSetChanged();
        super.onResume();
    }

    //17.07.02 이미 읽은 comment 가져오기 -> 시간 순으로 정렬해서 최근 8개 까지만 출력되도록 설정
    //8개까지는 완료, sqlite 에서 date 순으로 정렬 완료 -> 출력이 이상함 .
    /*
    public void get_read_comment()
    {
        int count = 0;
        read_comment_cursor = database_manager.get_Read_Comment();
        if(read_comment_cursor != null) {
            if (read_comment_cursor.moveToFirst()&& read_comment_cursor.getCount() != 0) {
                do {
                    if(count < 4) {
                        name = read_comment_cursor.getString(read_comment_cursor.getColumnIndex("NAME"));
                        phone_number = read_comment_cursor.getString(read_comment_cursor.getColumnIndex("PHONE_NUMBER"));
                        url = read_comment_cursor.getString(read_comment_cursor.getColumnIndex("URL"));
                        emotion = read_comment_cursor.getInt(read_comment_cursor.getColumnIndex("EMOTION"));
                        Log.d("get_read_comment", name + phone_number + url + emotion);

                        //add to gridview
                        gridAdapter_for_read.add(new page2_Gird_for_read(name, phone_number, url, emotion));
                    }else{
                        break;
                    }
                    count++;
                } while (read_comment_cursor.moveToNext());
                read_comment_cursor.close();
            } else {
                Log.d("PAGE2_ONCREATE", "read_comment_cursor moveToFirst end");
            }
        }else{
            gridAdapter_for_read.add(new page2_Gird_for_read("No comment\nDo LINK feed", phone_number, "http://blog.naver.com/lyg2717", 1));
            Log.d("PAGE2_ONCREATE", "read_comment_cursor is null");
        }
        gridAdapter_for_read.notifyDataSetChanged();
        super.onResume();
    }
    */
    //17.07.13
    //fragment_life_cycle
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
        //17.06.17
        //Activity 종료 시 서비스 종료? -> 종료 안하는게 낫다, 계속 켜져있게.
        //stopService(new Intent(this, page999_Service_Always_Top.class));
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }
}
