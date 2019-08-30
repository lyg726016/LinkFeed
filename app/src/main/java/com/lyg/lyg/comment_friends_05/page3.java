package com.lyg.lyg.comment_friends_05;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Junyoung on 2016-06-23.
 */

public class page3 extends Fragment {

    //page1_search target
    private ImageButton searchtarget_google;
    private ImageButton searchtarget_naver;
    private ImageButton searchtarget_youtube;
    private ImageButton searchtarget_zum;
    private String google;
    private String naver;
    private String youtube;
    private String zum;
    private String search;

    //page1_search
    private ImageButton page3_search_btn;
    private EditText page3_search_edittext;

    //webview
    private WebView page3_webview;
    private ProgressBar page3_progressbar;

    //get view
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get view
        rootView = inflater.inflate(R.layout.page3, container, false);
        //page1_search target
        searchtarget_google = (ImageButton)rootView.findViewById(R.id.page3_searchtarget_google);
        searchtarget_naver = (ImageButton)rootView.findViewById(R.id.page3_searchtarget_naver);
        searchtarget_youtube = (ImageButton)rootView.findViewById(R.id.page3_searchtarget_youtube);
        searchtarget_zum = (ImageButton)rootView.findViewById(R.id.page3_searchtarget_zum);
        google = "https://www.google.co.kr/search?q=";
        naver ="https://search.naver.com/search.naver?ie=utf8&where=nexearch&query=";
        youtube = "https://www.youtube.com/results?search_query=";
        zum = "http://search.zum.com/search.zum?method=uni&option=accu&qm=f_typing&rd=1&query=";
        //webview
        page3_webview = (WebView)rootView.findViewById(R.id.page3_webview);
        page3_progressbar = (ProgressBar)rootView.findViewById(R.id.page3_progressbar);
        set_webview();
        //page1_search
        page3_search_btn = (ImageButton)rootView.findViewById(R.id.page3_search_btn);
        page3_search_edittext = (EditText)rootView.findViewById(R.id.page3_search_edittext);


        //page1_search
        search = naver;
        searchtarget_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = zum;
                show_toast_short("검색 브라우저 줌");
                searchtarget_google.setVisibility(View.INVISIBLE);
                searchtarget_youtube.setVisibility(View.INVISIBLE);
                searchtarget_zum.setVisibility(View.VISIBLE);
                searchtarget_naver.setVisibility(View.INVISIBLE);
            }
        });
        searchtarget_zum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = google;
                show_toast_short("검색 브라우저 구글");
                searchtarget_google.setVisibility(View.VISIBLE);
                searchtarget_youtube.setVisibility(View.INVISIBLE);
                searchtarget_zum.setVisibility(View.INVISIBLE);
                searchtarget_naver.setVisibility(View.INVISIBLE);

            }
        });
        searchtarget_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = naver;
                show_toast_short("검색 브라우저 네이버");
                searchtarget_google.setVisibility(View.INVISIBLE);
                searchtarget_youtube.setVisibility(View.INVISIBLE);
                searchtarget_zum.setVisibility(View.INVISIBLE);
                searchtarget_naver.setVisibility(View.VISIBLE);

            }
        });
        searchtarget_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = youtube;
                show_toast_short("검색 브라우저 유투브");
                searchtarget_google.setVisibility(View.INVISIBLE);
                searchtarget_youtube.setVisibility(View.VISIBLE);
                searchtarget_zum.setVisibility(View.INVISIBLE);
                searchtarget_naver.setVisibility(View.INVISIBLE);
            }
        });

        //search_btn
        page3_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (page3_search_edittext.getText().toString().contains("www")) {
                    if (page3_search_edittext.getText().toString().contains("http")) {
                        page3_webview.loadUrl(page3_search_edittext.getText().toString());
                    } else {
                        //webview load
                        page3_webview.loadUrl("http://" + page3_search_edittext.getText().toString());
                    }

                } else {
                    if (page3_search_edittext.getText().toString().contains("http")) {
                        //webview load
                        page3_webview.loadUrl(page3_search_edittext.getText().toString());
                    } else {
                        if (page3_search_edittext.getText().toString().contains(".com")
                                || page3_search_edittext.getText().toString().contains(".net")
                                || page3_search_edittext.getText().toString().contains(".kr")) {
                            page3_webview.loadUrl("http://" + page3_search_edittext.getText().toString());
                        } else {
                            //위의 모든 조건이 아닌 경우 - 그냥 검색인 경우.
                            page3_webview.loadUrl(search + page3_search_edittext.getText().toString());
                        }

                    }
                }
                //초기화
                page3_search_edittext.setText("");
                //page1_search start -> softkeyboard hide
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(page3_webview.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(page3_webview.getWindowToken(), 0);
            }
        });

        return rootView;
    }

    private void set_webview()
    {
        page3_webview.getSettings().setJavaScriptEnabled(true);
        page3_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (page3_progressbar.isShown() == false) {
                    page3_progressbar.setVisibility(View.VISIBLE);
                }
            }
            public void onPageFinished(WebView view, String url) {
                if(page3_progressbar.isShown() == true) {
                page3_progressbar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //toast_short
    private void show_toast_short(String text) {
        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    //toast_long
    private void show_toast_long(String text) {
        Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}