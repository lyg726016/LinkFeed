package com.lyg.lyg.comment_friends_05;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class page2_GridView_Adapter_for_read_comment extends ArrayAdapter<page2_Gird_for_read> {

    private View row;
    private ViewHolder holder;
    private Context context;
    private int layoutResourceId;
    private ArrayList<page2_Gird_for_read> data = new ArrayList<page2_Gird_for_read>();
    //emotion_bitmap
    private Bitmap bitmap;

    //AlertDialog
    //webview_dialog
    private AlertDialog view_alertDialog;
    AlertDialog.Builder view_builder;
    private View mDialogView;
    private ProgressBar progressBar;

    //coponent_in_webview_dialog
    private WebView webView;

    //db
    private page999_Database_Manager database_manager;
    private static final int is_webview_from_gridview = 1;
    private static final int is_webview_from_page_move = 2;

    //listview
    private ListView comment_listview;
    private page999_Comment_ListView_Adapter comment_listView_adapter;
    private ArrayList<page999_Comment_for_Listview> comment_for_listviewArrayList = new ArrayList<page999_Comment_for_Listview>();
    private ImageView page2_popup_listview_pull_up;
    private ImageView page2_popup_listview_pull_down;


    public page2_GridView_Adapter_for_read_comment(Context context, int layoutResourceId, ArrayList<page2_Gird_for_read> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (row == null) {
            row = convertView;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.page2_grid_for_read_comment, null);
            //webview_dialog
            mDialogView = inflater.inflate(R.layout.page999_dialog_for_webview, null);
            webView = (WebView)mDialogView.findViewById(R.id.page2_popup_webview);
            progressBar = (ProgressBar)mDialogView.findViewById(R.id.page2_popup_progressbar);
            database_manager = new page999_Database_Manager(context);
            //set_listview
            comment_listview = (ListView)mDialogView.findViewById(R.id.page2_popup_listview);
            comment_listView_adapter = new page999_Comment_ListView_Adapter(getContext(), R.layout.page999_dialog_for_webview, comment_for_listviewArrayList);
            comment_listview.setAdapter(comment_listView_adapter);
            page2_popup_listview_pull_up = (ImageView)mDialogView.findViewById(R.id.pgae2_popup_pull_up_listview);
            page2_popup_listview_pull_down= (ImageView)mDialogView.findViewById(R.id.pgae2_popup_pull_down_listview);
            //set_page2_popup_function

            //holder
            holder = new ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.page2_TextView_for_Read_GridView);
            holder.image = (ImageView) row.findViewById(R.id.page2_ImageView_for_Read_GridView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }




        final page2_Gird_for_read item = data.get(position);
        holder.name.setText(item.get_name());
        holder.url = item.get_url();
        holder.emotion = item.get_emotion();
        holder.phone_number = item.get_Phone_number();
        //info = 0 / angry = 1 / fear = 2 / happy = 3 / sad = 4
        switch (holder.emotion)
        {
            case 0 :
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.emotion_info);
                holder.image.setImageBitmap(bitmap);
                break;
            case 1 :
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.emotion_anger);
                holder.image.setImageBitmap(bitmap);
                break;
            case 2 :
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.emotion_fear);
                holder.image.setImageBitmap(bitmap);
                break;
            case 3 :
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.emotion_happy);
                holder.image.setImageBitmap(bitmap);
                break;
            case 4 :
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.emotion_sad);
                holder.image.setImageBitmap(bitmap);
                break;
        }
        item.set_Image(bitmap);

        //emotion_imageview_clcik
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_listview.setVisibility(View.INVISIBLE);
                page2_popup_listview_pull_up.setVisibility(View.VISIBLE);
                page2_popup_listview_pull_down.setVisibility(View.INVISIBLE);
                page2_popup_listview_pull_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comment_listview.setVisibility(View.VISIBLE);
                        page2_popup_listview_pull_up.setVisibility(View.INVISIBLE);
                        page2_popup_listview_pull_down.setVisibility(View.VISIBLE);
                    }
                });
                page2_popup_listview_pull_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comment_listview.setVisibility(View.INVISIBLE);
                        page2_popup_listview_pull_up.setVisibility(View.VISIBLE);
                        page2_popup_listview_pull_down.setVisibility(View.INVISIBLE);
                    }
                });
                //set_listview
                set_listview_in_webview(holder.url, is_webview_from_gridview);
                //dialog
                view_builder = new AlertDialog.Builder(getContext());
                view_builder.setView(mDialogView);
                view_builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        close_view_alert_dialog();
                        comment_listview.setVisibility(View.INVISIBLE);
                    }
                });


                //set dialog and show
                webView.loadUrl(holder.url);
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
            }
        });
        return row;
    }

    static class ViewHolder {
        TextView name;
        ImageView image;
        String url;
        String phone_number;
        int emotion;
    }

    private void set_View_Dialog() {
        view_alertDialog = view_builder.create();
        view_alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        view_alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view_alertDialog.getWindow().setGravity(Gravity.TOP);

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
    }
    private void set_listview_in_webview(String url, int mode)
    {
        //17.06.21
        //page2_function_listview_show_N_hide_초기화.
        comment_listView_adapter.clear();
        //db - gridview를 통해 클릭한 경우에만 db 업데이트 -> 읽음으로 바꿈
        if(mode == is_webview_from_gridview) {
            database_manager.update_Comment_as_READ(holder.phone_number, url);
        }else{
            //do nothing
        }
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
            comment_listView_adapter.add(new page999_Comment_for_Listview("LINK FEED", "There is no comment exist"));
            Log.d("page2_emotion_click", "page2_comment_cursor is null");
        }
        comment_listView_adapter.notifyDataSetChanged();
    }
}