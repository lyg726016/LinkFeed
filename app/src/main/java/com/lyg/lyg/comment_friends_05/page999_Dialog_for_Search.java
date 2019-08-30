package com.lyg.lyg.comment_friends_05;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by L on 2017-09-02.
 * Custo 알림창에서 Search_Btn 클릭 시 생성되는 Dialog
 * */

public class page999_Dialog_for_Search extends Activity {

    //17.09.02
    //dialog_for_notification_search
    private ImageView imageView_search;
    //onBackPress
    private page999_BackPressCloseHandler backPressCloseHandler;
    //page1_search target
    private View search_view;
    private ImageButton search_target_google;
    private ImageButton search_target_naver;
    private ImageButton search_target_youtube;
    private ImageButton search_target_zum;
    private String google;
    private String naver;
    private String youtube;
    private String zum;
    private String search;
    //webview
    private WebView webview_for_searchdialog;
    //search
    private ImageButton page999_search_btn;
    private EditText page999_search_edittext;

    //view_dialog
    private AlertDialog view_alertDialog;
    AlertDialog.Builder view_builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //SEARCH_FUCNTION
        //get view
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        search_view = mInflater.inflate(R.layout.page999_dialog_for_search, null);
        //page1_search target
        search_target_google = (ImageButton)search_view.findViewById(R.id.page999_searchtarget_google);
        search_target_naver = (ImageButton)search_view.findViewById(R.id.page999_searchtarget_naver);
        search_target_youtube = (ImageButton)search_view.findViewById(R.id.page999_searchtarget_youtube);
        search_target_zum = (ImageButton)search_view.findViewById(R.id.page999_searchtarget_zum);
        google = "https://www.google.co.kr/search?q=";
        naver ="https://search.naver.com/search.naver?ie=utf8&where=nexearch&query=";
        youtube = "https://www.youtube.com/results?search_query=";
        zum = "http://search.zum.com/search.zum?method=uni&option=accu&qm=f_typing&rd=1&query=";
        //webview
        webview_for_searchdialog = (WebView)search_view.findViewById(R.id.page999_search_webview);
        //page1_search
        page999_search_btn = (ImageButton)search_view.findViewById(R.id.page999_search_btn);
        page999_search_edittext = (EditText)search_view.findViewById(R.id.page999_search_edittext);


        //page1_search
        search = naver;
        search_target_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = zum;
                search_target_google.setVisibility(View.INVISIBLE);
                search_target_youtube.setVisibility(View.INVISIBLE);
                search_target_zum.setVisibility(View.VISIBLE);
                search_target_naver.setVisibility(View.INVISIBLE);
            }
        });
        search_target_zum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = google;
                search_target_google.setVisibility(View.VISIBLE);
                search_target_youtube.setVisibility(View.INVISIBLE);
                search_target_zum.setVisibility(View.INVISIBLE);
                search_target_naver.setVisibility(View.INVISIBLE);

            }
        });
        search_target_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = naver;
                search_target_google.setVisibility(View.INVISIBLE);
                search_target_youtube.setVisibility(View.INVISIBLE);
                search_target_zum.setVisibility(View.INVISIBLE);
                search_target_naver.setVisibility(View.VISIBLE);

            }
        });
        search_target_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = youtube;
                search_target_google.setVisibility(View.INVISIBLE);
                search_target_youtube.setVisibility(View.VISIBLE);
                search_target_zum.setVisibility(View.INVISIBLE);
                search_target_naver.setVisibility(View.INVISIBLE);
            }
        });

        //search_btn
        page999_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (page999_search_edittext.getText().toString().contains("www")) {
                    if (page999_search_edittext.getText().toString().contains("http")) {
                        webview_for_searchdialog.loadUrl(page999_search_edittext.getText().toString());
                    } else {
                        //webview load
                        webview_for_searchdialog.loadUrl("http://" + page999_search_edittext.getText().toString());
                    }

                } else {
                    if (page999_search_edittext.getText().toString().contains("http")) {
                        //webview load
                        webview_for_searchdialog.loadUrl(page999_search_edittext.getText().toString());
                    } else {
                        if (page999_search_edittext.getText().toString().contains(".com")
                                || page999_search_edittext.getText().toString().contains(".net")
                                || page999_search_edittext.getText().toString().contains(".kr")) {
                            webview_for_searchdialog.loadUrl("http://" + page999_search_edittext.getText().toString());
                        } else {
                            //위의 모든 조건이 아닌 경우 - 그냥 검색인 경우.
                            webview_for_searchdialog.loadUrl(search + page999_search_edittext.getText().toString());
                        }

                    }
                }
                //초기화
                page999_search_edittext.setText("");
                close_view_alert_dialog();

            }
        });
        set_search_dialog();
    }

    private void set_search_dialog() {
        //listview_initialize - 17.06.20
        view_builder = new AlertDialog.Builder(getApplicationContext());
        view_builder.setView(search_view);
        view_alertDialog = view_builder.create();
        view_alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        view_alertDialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view_alertDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        if (search_view.getParent() != null) {
            ((ViewGroup) search_view.getParent()).removeView(search_view);
            view_alertDialog.show();
        } else {
            view_alertDialog.show();
        }
    }

    private void close_view_alert_dialog()
    {
        if (view_alertDialog != null) {
            view_alertDialog.dismiss();
        } else {
            Log.d("close_view_alert_dialog", "view_alertDialog is null");
        }
    }
    //뒤로가기 누르면 종료
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed_on_Dialog();
    }
}

