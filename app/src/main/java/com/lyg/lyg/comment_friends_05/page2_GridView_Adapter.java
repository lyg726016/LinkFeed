package com.lyg.lyg.comment_friends_05;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class page2_GridView_Adapter extends ArrayAdapter<page2_Gird> {

    private ViewHolder holder;
    private Context context;
    private int layoutResourceId;
    private ArrayList<page2_Gird> data = new ArrayList<page2_Gird>();
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
    private ImageButton webview_refresh;
    private ImageButton webview_copy;


    //for_contact
    private ListView contact_listView;
    private page999_Contact_Listview_Adapter page999_contact_listview_adapter;
    private ArrayList<page999_Contact_for_Listview> contact_for_listviewArrayList = new ArrayList<page999_Contact_for_Listview>();
    private String contact_name = "";
    private String contact_phone_number = "";
    private ArrayList<String> phone_number_for_send = new ArrayList<String>();
    private View contactDialogView;
    private ImageView imageView_send_for_contact;
    private ImageView imageView_close_for_contact;
    //contacts_dialog
    private AlertDialog contacts_alertDialog;
    AlertDialog.Builder contacts_builder;

    //db
    private page999_Database_Manager database_manager;
    private static final int is_webview_from_gridview = 1;
    private static final int is_webview_from_page_move = 2;

    //for_comment_in_webview
    private ListView comment_listview;
    private page999_Comment_ListView_Adapter comment_listView_adapter;
    private ArrayList<page999_Comment_for_Listview> comment_for_listviewArrayList = new ArrayList<page999_Comment_for_Listview>();
    private ImageView page2_popup_listview_pull_up;
    private ImageView page2_popup_listview_pull_down;
    private EditText page2_popup_edittext_for_write_comment;
    private ImageView page2_popup_imageview_for_send_comment;

    //17.07.12
    //simple_dialog_for_alert
    private Dialog simple_alert_dialog;
    private AlertDialog.Builder builder_for_simple_alert_dialog;


    public page2_GridView_Adapter(Context context, int layoutResourceId, ArrayList<page2_Gird> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.page2_grid_for_unread_comment, null);
            //get listview for contacts
            //17.06.27 - contact dialog view
            //get listview for contacts
            contactDialogView = inflater.inflate(R.layout.page999_dialog_for_contacts, null);
            imageView_close_for_contact = (ImageView) contactDialogView.findViewById(R.id.page999_contact_close);
            contact_listView = (ListView) contactDialogView.findViewById(R.id.page999_contact_listview_for_contacts);
            imageView_send_for_contact = (ImageView) contactDialogView.findViewById(R.id.page999_contact_send);
            //webview_dialog
            mDialogView = inflater.inflate(R.layout.page999_dialog_for_webview, null);
            webView = (WebView)mDialogView.findViewById(R.id.page2_popup_webview);
            webview_copy = (ImageButton)mDialogView.findViewById(R.id.page2_popup_copy);
            webview_refresh = (ImageButton)mDialogView.findViewById(R.id.page2_popup_refresh);
            progressBar = (ProgressBar)mDialogView.findViewById(R.id.page2_popup_progressbar);
            database_manager = new page999_Database_Manager(context);
            //set_listview
            comment_listview = (ListView)mDialogView.findViewById(R.id.page2_popup_listview);
            comment_listView_adapter = new page999_Comment_ListView_Adapter(getContext(), R.layout.page999_dialog_for_webview, comment_for_listviewArrayList);
            comment_listview.setAdapter(comment_listView_adapter);
            page2_popup_listview_pull_up = (ImageView)mDialogView.findViewById(R.id.pgae2_popup_pull_up_listview);
            page2_popup_listview_pull_down= (ImageView)mDialogView.findViewById(R.id.pgae2_popup_pull_down_listview);
            //set_page2_popup_function
            page2_popup_edittext_for_write_comment = (EditText)mDialogView.findViewById(R.id.page2_popup_edittext_for_write_comment);
            page2_popup_imageview_for_send_comment = (ImageView) mDialogView.findViewById(R.id.page2_popup_imageview_for_send);
            //holder
            holder = new ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.page2_TextView_for_Unread_GridView);
            holder.image = (ImageView) row.findViewById(R.id.page2_ImageView_for_Unread_GridView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        //17.07.09 send as enter
        page2_popup_edittext_for_write_comment.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (page2_popup_edittext_for_write_comment.getText().toString() == null) {
                        //do nothing
                    } else {
                        //17.06.27 - conatct dialog call
                        //send_comment();
                        //close_view_alert_dialog();
                        call_send_function();
                        //기존에 선택했던 contact 초기화
                        page999_contact_listview_adapter.clear_Pn_ArrayList();
                    }
                }
                return false;
            }

        });

        //17.07.09 send, send for contact
        //set_send_function_COMMENT_DIALOG_IN_VIEW_FUCNTION
        page2_popup_imageview_for_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //17.06.27 contacts dialog
                //send_comment();
                //close_view_alert_dialog();
                call_send_function();
                //기존에 선택했던 contact 초기화
                page999_contact_listview_adapter.clear_Pn_ArrayList();
            }
        });
        //set_send_function_CONTACT_DIALOG_IN_VIEW_FUNCTION_IN
        imageView_send_for_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //전송할 핸드폰 번호를 page999_contact_listview_adapter에서 각 클릭된 번호를 통해서 받아옴.
                phone_number_for_send = page999_contact_listview_adapter.get_Pn_ArrayList();
                if(phone_number_for_send.size() != 0) {
                    send_comment(phone_number_for_send);
                }else{
                    //삭제-17.07.12 -toast는 dialog보다 아래에 출력됨 -> dialog로 변경.
                    //Toast.makeText(getContext(), "SELECT NAME FOR SEND COMMENT", Toast.LENGTH_SHORT).show();
                    simple_dialog_for_aloer(getContext(), simple_alert_dialog, builder_for_simple_alert_dialog, "받는 사람을 선택해주세요.");
                }
                //text 초기화

            }
        });
        imageView_close_for_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//17.07.12
                //17.06.27 다 닫고 다시 생성.
                close_contact_dialog(0);
                set_View_Dialog();
                //close 시 pn arraylist 초기화 -> 중복되지 않도록.
                page999_contact_listview_adapter.clear_Pn_ArrayList();

                //17.07.12
                // 다시 되돌아 갈 때, keyboard hide -> 안먹힘
                /*
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(page2_popup_edittext_for_write_comment.getWindowToken(), 0);
                */
            }
        });

        final page2_Gird item = data.get(position);
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
                set_listview_in_webview(holder.url, is_webview_from_gridview);

                //set webview dialog and show
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
                //17.07.12
                //set_webview_imagebtn
                webview_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        webView.getUrl();
                        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(webView.getUrl());
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("URL COPY", webView.getUrl());
                            clipboard.setPrimaryClip(clip);
                        }
                        Toast.makeText(getContext(), "복사 완료", Toast.LENGTH_SHORT).show();
                    }
                });
                webview_refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        webView.loadUrl(webView.getUrl());
                    }
                });
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
    }
    private void set_listview_in_webview(String url, int mode)
    {
        //17.06.21
        //page2_function_listview_show_N_hide_초기화.
        comment_listView_adapter.clear();
        //db - gridview를 통해 클릭한 경우에만 db 업데이트 -> 읽음으로 바꿈
            database_manager.update_Comment_as_READ(holder.phone_number, url);
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

    //17.07,09 webview에서 보내기 시도.
    private void sendSMS(String phoneNumber, String message) {

        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0,
                new Intent(getContext(), page999_Sms_Sent.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getContext(), 0,
                new Intent(getContext(), page999_Sms_Sent.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {
            e.printStackTrace();
            simple_dialog_for_aloer(getContext(), simple_alert_dialog, builder_for_simple_alert_dialog, "SMS sending failed...");
            //Toast.makeText(getContext(), "SMS sending failed...", Toast.LENGTH_SHORT).show();
        }
        //sms 정상 전송 시 텍스트창 초기화.

        Toast.makeText(getContext(), "SMS sending completed...", Toast.LENGTH_SHORT).show();
        page2_popup_edittext_for_write_comment.setText("");
        //17.06.27 send 후 모든 dialog close
        close_contact_dialog(0);
    }

    private void send_comment(ArrayList<String> pn_Array_List) {
        //comment_friend_message[0] = comment_friend / 1 = url / 2 = 댓글 내용 / 3 = emotion / 4 = type(text, image, else)
        String message = "comment_friend<!%" + holder.url + "<!%" + page2_popup_edittext_for_write_comment.getText().toString() + "<!%" + 0 + "<!%" + 0;
        //message의 총 길이에 따라 mms / sms 구분해야 함.
        if (message.length() > 80) {
            //17.07.02 -> 구글 url shortner를 이용해서 짧게해서 전달하도록.
            //17.07.12 문구 수정 -> toast를 dialog로 변경
            //Toast.makeText(getContext(), "주소, 댓글의 길이가 80자를 초과했습니다. \n80자를 초과해 전송할 수 없습니다.(업데이트 예정)", Toast.LENGTH_SHORT).show();
            simple_dialog_for_aloer(getContext(), simple_alert_dialog, builder_for_simple_alert_dialog, "주소, 댓글의 길이가 80자를 초과했습니다. \n80자를 초과해 전송할 수 없습니다.(추후 업데이트 예정)");
        } else {
            //17.06.28- pn 배열리스트 완료 -> term을 두고 각각의 String(phone_number)을 다 보내도록
            for(String phone_number: pn_Array_List) {
                sendSMS(phone_number, message);
            }
        }

    }

    //17.06.23 - SEND 클릭 시 주소록이 출력되고, 해당 주소록에서 클릭으로 수신자 선택. - 아직 미구현.
    //17.06.27 구현 -> checkbox 클릭되도록 추가 진행 -> 완료.
    private void call_send_function() {
        contact_for_listviewArrayList.clear();
        contacts_builder = new AlertDialog.Builder(getContext());
        contacts_builder.setView(contactDialogView);
        contacts_alertDialog = contacts_builder.create();
        contacts_alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        contacts_alertDialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        contacts_alertDialog.getWindow().setGravity(Gravity.TOP);

        String name = null;
        String phone_number = null;
        Cursor contact_cursor;

        contact_cursor = database_manager.get_Bookmarks_All();
        if (contact_cursor != null) {
            if (contact_cursor.moveToFirst() && contact_cursor.getCount() != 0) {
                do {
                    name = contact_cursor.getString(contact_cursor.getColumnIndex("NAME"));
                    phone_number = contact_cursor.getString(contact_cursor.getColumnIndex("PHONE_NUMBER"));
                    page999_Contact_for_Listview contact_for_listview = new page999_Contact_for_Listview(name, phone_number);
                    contact_for_listviewArrayList.add(contact_for_listview);
                    Log.d("call_send_function", "add_favority_contact");
                } while (contact_cursor.moveToNext());
                contact_cursor.close();
            } else {
                Log.d("get_Bookmarks_All", "on_top_contact_cursor moveToFirst end");
            }
        } else {
            //17.07.12 필요없는 부분 삭제
            /*
            page999_Contact_for_Listview contact_for_listview = new page999_Contact_for_Listview("No Bookmark founded", "");
            contact_for_listviewArrayList.add(contact_for_listview);
            Log.d("get_Bookmarks_All", "on_top_contact_cursor is null");
            */
        }
        //17.07.12 GROUP 추가 -> 중복없이 하나의 그룹이름만 출력해야 함
        ArrayList<String> group_name = new ArrayList<String>();
        contact_cursor = database_manager.get_Group_All();
        if (contact_cursor != null) {
            if (contact_cursor.moveToFirst() && contact_cursor.getCount() != 0) {
                do {
                    name = contact_cursor.getString(contact_cursor.getColumnIndex("GROUP_NAME"));
                    if(group_name.contains(name)){ //이미 해당 그룹명이 있는 경우
                        //do nothing
                    }else{ //처음 나오는 그룹명인 경우
                        group_name.add(name);
                    }
                    Log.d("call_send_function", "add_group_contact");
                } while (contact_cursor.moveToNext());
                contact_cursor.close();
            } else {
                Log.d("get_Group_All", "on_top_contact_cursor moveToFirst end");
            }
            //위에서 중복없이 그룹명을 모은 group_name의 각각을 출력할 리스트에 추가
            for(String v : group_name)
            {
                page999_Contact_for_Listview contact_for_listview = new page999_Contact_for_Listview(v, "0");
                contact_for_listviewArrayList.add(contact_for_listview);
            }
        } else {
            //17.07.12 필요없는 부분 삭제
            /*
            page999_Contact_for_Listview contact_for_listview = new page999_Contact_for_Listview("No Bookmark founded", "");
            contact_for_listviewArrayList.add(contact_for_listview);
            Log.d("get_Group_All", "on_top_contact_cursor is null");
            */
        }

        contact_cursor = database_manager.get_Contacts_All();
        if (contact_cursor != null) {
            if (contact_cursor.moveToFirst() && contact_cursor.getCount() != 0) {
                do {
                    name = contact_cursor.getString(contact_cursor.getColumnIndex("NAME"));
                    phone_number = contact_cursor.getString(contact_cursor.getColumnIndex("PHONE_NUMBER"));
                    page999_Contact_for_Listview contact_for_listview = new page999_Contact_for_Listview(name, phone_number);
                    contact_for_listviewArrayList.add(contact_for_listview);
                    Log.d("call_send_function", "add_contact");
                } while (contact_cursor.moveToNext());
                contact_cursor.close();
            } else {
                Log.d("call_send_function", "on_top_contact_cursor moveToFirst end");
            }
        } else {
            //17.07.12 필요없는 부분 삭제
            /*
            page999_Contact_for_Listview contact_for_listview = new page999_Contact_for_Listview("No Friend founded", "");
            contact_for_listviewArrayList.add(contact_for_listview);
            Log.d("call_send_function", "on_top_get_contact_cursor is null");
            */
        }
        page999_contact_listview_adapter = new page999_Contact_Listview_Adapter(getContext(), R.layout.page999_contact_for_listview, contact_for_listviewArrayList);
        page999_contact_listview_adapter.set_view_for_dilaog_for_contacts(contactDialogView);
        contact_listView.setAdapter(page999_contact_listview_adapter);
        page999_contact_listview_adapter.notifyDataSetChanged();

        //dialogview.parent 있을 때 실행 시 오류 -> 있으면 remove하고 생성하도록
        if (contactDialogView.getParent() != null) {
            ((ViewGroup) contactDialogView.getParent()).removeView(contactDialogView);
            contacts_alertDialog.show();
        } else {
            contacts_alertDialog.show();
        }

    }


    //17.06.27 - close contact dialog , 0=all , 1 = contact만
    private void close_contact_dialog(int is_all_close)
    {
        //close_imageview click
        if(is_all_close == 1) {
            if (contacts_alertDialog != null) {
                contacts_alertDialog.dismiss();
            } else {
                Log.d("close_contact_dialog", "contacts_alertDialog is null");
            }
        }
        //send_cantact click
        else{
            if (view_alertDialog != null) {
                view_alertDialog.dismiss();
            } else {
                Log.d("close_contact_dialog", "view_alertDialog is null");
            }
            if (contacts_alertDialog != null) {
                contacts_alertDialog.dismiss();
            } else {
                Log.d("close_contact_dialog", "contacts_alertDialog is null");
            }
        }
    }

    //17.07.12
    //simple_dialog_for_aloer
    private void simple_dialog_for_aloer(Context context_or_activity, Dialog dialog, AlertDialog.Builder builder, String message_in_dialog)
    {
        builder = new AlertDialog.Builder(context_or_activity);
        builder.setMessage(message_in_dialog)
                .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        } else {
                            Log.d("simple_dialog_for_aloer", "dialog is null");
                        }
                    }
                });
        dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        dialog.show();
    }

}