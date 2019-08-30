package com.lyg.lyg.comment_friends_05;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by L on 2017-08-31.
 */

public class page999_Dialog_for_Webview extends Activity {

    //onBackPress
    private page999_BackPressCloseHandler backPressCloseHandler;

    //webview_dialog
    private AlertDialog view_alertDialog;
    AlertDialog.Builder view_builder;
    private View mDialogView;
    private ProgressBar progressBar;

    //for_comment_in_webview
    private ListView comment_listview;
    private page999_Comment_ListView_Adapter comment_listView_adapter;
    private ArrayList<page999_Comment_for_Listview> comment_for_listviewArrayList = new ArrayList<page999_Comment_for_Listview>();
    private ImageView page2_popup_listview_pull_up;
    private ImageView page2_popup_listview_pull_down;
    private EditText page2_popup_edittext_for_write_comment;
    private ImageView page2_popup_imageview_for_send_comment;

    //coponent_in_webview_dialog
    private WebView webView;
    private ImageButton webview_refresh;
    private ImageButton webview_copy;

    //db
    private page999_Database_Manager database_manager;
    private static final int is_webview_from_gridview = 1;
    private static final int is_webview_from_page_move = 2;


    //for_get_data_from_sms_incoming
    private String url_from_sms_incoming;
    private String phone_number_from_sms_incoming;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //17.09.02, sms_incoming에서 url정보를 받아와 webview에 전달
        Intent intent = getIntent();
        url_from_sms_incoming = intent.getStringExtra("sms_url");
        //onBackPress
        backPressCloseHandler = new page999_BackPressCloseHandler(this);
        //webview_dialog
        mDialogView = inflater.inflate(R.layout.page999_dialog_for_webview, null);
        webView = (WebView)mDialogView.findViewById(R.id.page2_popup_webview);
        webview_copy = (ImageButton)mDialogView.findViewById(R.id.page2_popup_copy);
        webview_refresh = (ImageButton)mDialogView.findViewById(R.id.page2_popup_refresh);
        progressBar = (ProgressBar)mDialogView.findViewById(R.id.page2_popup_progressbar);
        database_manager = new page999_Database_Manager(getApplicationContext());


        //set_listview
        comment_listview = (ListView)mDialogView.findViewById(R.id.page2_popup_listview);
        comment_listView_adapter = new page999_Comment_ListView_Adapter(getApplicationContext(), R.layout.page999_dialog_for_webview, comment_for_listviewArrayList);
        comment_listview.setAdapter(comment_listView_adapter);
        page2_popup_listview_pull_up = (ImageView)mDialogView.findViewById(R.id.pgae2_popup_pull_up_listview);
        page2_popup_listview_pull_down= (ImageView)mDialogView.findViewById(R.id.pgae2_popup_pull_down_listview);
        //set_page2_popup_function
        page2_popup_edittext_for_write_comment = (EditText)mDialogView.findViewById(R.id.page2_popup_edittext_for_write_comment);
        page2_popup_imageview_for_send_comment = (ImageView) mDialogView.findViewById(R.id.page2_popup_imageview_for_send);

        page2_popup_edittext_for_write_comment.setVisibility(View.GONE);
        page2_popup_imageview_for_send_comment.setVisibility(View.INVISIBLE);
        page2_popup_listview_pull_up.setVisibility(View.VISIBLE);
        page2_popup_listview_pull_down.setVisibility(View.INVISIBLE);
        page2_popup_listview_pull_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_listview.setVisibility(View.VISIBLE);
                page2_popup_edittext_for_write_comment.setVisibility(View.VISIBLE);
                page2_popup_imageview_for_send_comment.setVisibility(View.VISIBLE);
                page2_popup_listview_pull_up.setVisibility(View.INVISIBLE);
                page2_popup_listview_pull_down.setVisibility(View.VISIBLE);
            }
        });
        page2_popup_listview_pull_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_listview.setVisibility(View.INVISIBLE);
                page2_popup_edittext_for_write_comment.setVisibility(View.GONE);
                page2_popup_imageview_for_send_comment.setVisibility(View.INVISIBLE);
                page2_popup_listview_pull_up.setVisibility(View.VISIBLE);
                page2_popup_listview_pull_down.setVisibility(View.INVISIBLE);
            }
        });
        //set_listview
        set_listview_in_webview(url_from_sms_incoming, is_webview_from_gridview);

        //set webview dialog and show
        webView.loadUrl(url_from_sms_incoming);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (progressBar.isShown() == false) {
                    progressBar.setVisibility(View.VISIBLE);
                    //17.06.23 - 다른 페이지로 이동 시 해당 페이지의 코멘트 리스트의 변화가 있어야 함 -> 페이지 이동을 하는 지 체크하고 진행
                    //set_listview_in_webview(webView.getUrl(), is_webview_from_page_move);
                }
            }
            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShown() == true) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        set_View_Dialog();
        //17.07.12
        //set_webview_imagebtn
        webview_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.getUrl();
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(webView.getUrl());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("URL COPY", webView.getUrl());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getApplicationContext(), "복사 완료", Toast.LENGTH_SHORT).show();
            }
        });
        webview_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(webView.getUrl());
            }
        });


        super.onCreate(savedInstanceState);
    }

    private void set_listview_in_webview(String url, int mode)
    {
        //17.06.21
        //page2_function_listview_show_N_hide_초기화.
        comment_listView_adapter.clear();
        //db - gridview를 통해 클릭한 경우에만 db 업데이트 -> 읽음으로 바꿈
        database_manager.update_Comment_as_READ(phone_number_from_sms_incoming, url);
        Cursor page2_comment_cursor = database_manager.get_Comment_as_Url(url);
        if(page2_comment_cursor != null) {
            if (page2_comment_cursor.moveToFirst()&& page2_comment_cursor.getCount() != 0) {
                do {
                    String page2_name = page2_comment_cursor.getString(page2_comment_cursor.getColumnIndex("NAME"));
                    String page2_comment = page2_comment_cursor.getString(page2_comment_cursor.getColumnIndex("COMMENT_CONTENTS"));
                    if(page2_name == null)
                        page2_name = "Unknown Friend";
                    //add to listview
                    comment_listView_adapter.add(new page999_Comment_for_Listview(page2_name, page2_comment));
                } while (page2_comment_cursor.moveToNext());
                page2_comment_cursor.close();
            } else {
                Log.d("page2_emotion_click", "page2_comment_cursor moveToFirst end");
            }
        }else{
            comment_listView_adapter.add(new page999_Comment_for_Listview("LINK FEED", "친구에게 받은 댓글이 아직 없습니다."));
            Log.d("page2_emotion_click", "page2_comment_cursor is null");
        }
        comment_listView_adapter.notifyDataSetChanged();
    }

    private void set_View_Dialog() {
        //dialog
        view_builder = new AlertDialog.Builder(getApplicationContext());
        view_builder.setView(mDialogView);
        view_builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                close_view_alert_dialog();
                comment_listview.setVisibility(View.INVISIBLE);
            }
        });
        view_alertDialog = view_builder.create();
        view_alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        view_alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view_alertDialog.getWindow().setGravity(Gravity.TOP);

        //추가 -> XX
        //view_alertDialog.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //view_alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //

        if (mDialogView.getParent() != null) {
            ((ViewGroup) mDialogView.getParent()).removeView(mDialogView);
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
            Log.d("page2_grid_adapter", "view_alertDialog is null");
        }
        //종료
        backPressCloseHandler.onBackPressed_on_Dialog();
    }

    //뒤로가기 누르면 종료
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed_on_Dialog();
    }
}
