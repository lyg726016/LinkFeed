package com.lyg.lyg.comment_friends_05;

/**
 * Created by L on 2017-05-01.
 */
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import java.util.ArrayList;

public class page999_Service_Always_Top extends Service {
    private View mView;
    private View mDialogView;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;
    private View contactDialogView;

    //motion_move
    private float mTouchX, mTouchY;
    private int mViewX, mViewY;
    private boolean isMove = false;
    private int x;
    private int y;

    //imageview for share function
    private int is_emotion_visible = 0;
    private ImageView imageView_stop;
    private ImageView imageView_view;
    private ImageView imageView_search;
    private ImageView comment_Friend;
    private ImageView imageView_start_activity;

    //imageview for emotion
    private ImageView imageView_emotion_sad;
    private ImageView imageView_emotion_angry;
    private ImageView imageView_emotion_happy;
    private ImageView imageView_emotion_info;
    private ImageView imageView_emotion_fear;

    //imageView_for_View_Function
    private ImageView imageView_close;
    private ImageView imageView_send_sms;
    private EditText editText_contents_comments;
    private String contents_of_comment = "";
    private ListView comment_listview;
    private page999_Comment_ListView_Adapter comment_listView_adapter;
    private ArrayList<page999_Comment_for_Listview> comment_for_listviewArrayList = new ArrayList<page999_Comment_for_Listview>();

    //contact_listview
    private ImageView imageView_close_for_contact;
    private ImageView imageView_send_for_contact;
    private ListView contact_listView;
    private page999_Contact_Listview_Adapter page999_contact_listview_adapter;
    private ArrayList<page999_Contact_for_Listview> contact_for_listviewArrayList = new ArrayList<page999_Contact_for_Listview>();
    private String contact_name = "";
    private String contact_phone_number = "";
    private ArrayList<String> phone_number_for_send = new ArrayList<String>();


    //clipboard
    private ClipboardManager clipboardManager;
    private String clipboard_url;

    //AlertDialog for comment
    //view_dialog
    private AlertDialog view_alertDialog;
    AlertDialog.Builder view_builder;
    //contacts_dialog
    private AlertDialog contacts_alertDialog;
    AlertDialog.Builder contacts_builder;

    //database_for_comment_listview
    private page999_Database_Manager database_manager;

    //sms_parameter
    private int type_of_message = 0;
    private int type_of_emotion = 0;

    //17.07.12
    //simple_dialog_for_alert
    private Dialog simple_alert_dialog;
    private AlertDialog.Builder builder_for_simple_alert_dialog;

    //17.08.28
    //view_for_search_dialog
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

    @Override
    public void onCreate() {
        super.onCreate();

        //17.08.07 서비스 실행 시 알림창 숨기기
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        getApplicationContext().sendBroadcast(it);

        //get view
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.page999_service_for_comment, null);
        mDialogView = mInflater.inflate(R.layout.page999_dialog_for_comments, null);
        //17.06.27 - contact dialog view
        contactDialogView = mInflater.inflate(R.layout.page999_dialog_for_contacts, null);
        //17.06.19 - database for comment listview
        database_manager = new page999_Database_Manager(getApplicationContext());


        //get clipboard
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        //get imageview for View function
        imageView_send_sms = (ImageView) mDialogView.findViewById(R.id.service_Imageview_for_send);
        editText_contents_comments = (EditText) mDialogView.findViewById(R.id.service_Edittext_for_send);

        //get imageview for share function
        imageView_stop = (ImageView) mView.findViewById(R.id.service_imageview_stop);
        imageView_view = (ImageView) mView.findViewById(R.id.service_imageview_view);
        imageView_search = (ImageView) mView.findViewById(R.id.service_imageview_search);
        comment_Friend = (ImageView) mView.findViewById(R.id.service_comment_friend);
        imageView_start_activity = (ImageView) mView.findViewById(R.id.service_start_activity);
        set_Comment_Friend_ImageView_Visibility(View.GONE, View.VISIBLE);

        //get imageview for emotion
        imageView_emotion_info = (ImageView) mDialogView.findViewById(R.id.service_emotion_info);
        imageView_emotion_sad = (ImageView) mDialogView.findViewById(R.id.service_emotion_sad);
        imageView_emotion_angry = (ImageView) mDialogView.findViewById(R.id.service_emotion_angry);
        imageView_emotion_happy = (ImageView) mDialogView.findViewById(R.id.service_emotion_happy);
        imageView_emotion_fear = (ImageView) mDialogView.findViewById(R.id.service_emotion_fear);
        imageView_close = (ImageView) mDialogView.findViewById(R.id.service_imageview_for_close);
        set_Emotion_ImageView_Visibility(View.VISIBLE);


        //17.06.19 - comment list view
        //get listview for comment
        comment_listview = (ListView) mDialogView.findViewById(R.id.service_Listview_for_read);
        comment_listView_adapter = new page999_Comment_ListView_Adapter(getApplicationContext(), R.layout.page999_comment_for_listview, comment_for_listviewArrayList);
        comment_listview.setAdapter(comment_listView_adapter);

        //17.06.27 - contact list view
        //get listview for contacts
        imageView_close_for_contact = (ImageView) contactDialogView.findViewById(R.id.page999_contact_close);
        imageView_send_for_contact = (ImageView) contactDialogView.findViewById(R.id.page999_contact_send);
        contact_listView = (ListView) contactDialogView.findViewById(R.id.page999_contact_listview_for_contacts);

        //17.06.29 - listview clcik event 처리 -> 하나 클릭하면 같은 position의 check도 true로 변경됨 -> holder에서 처리해봄.
        //17.07.02 -> holder에서 처리하도록 변경.
        /*
        contact_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox)view.findViewById(R.id.page999_contact_checkbox);
                page999_Contact_for_Listview contact_for_listview = (page999_Contact_for_Listview)parent.getAdapter().getItem(position);
                if(checkBox.isChecked() == true) {
                    checkBox.setChecked(false);
                    phone_number_for_send.remove(contact_for_listview.get_Phone_number());
                    //Log.d("contact_setonitem", contact_for_listview.get_Phone_number());
                }else{
                    checkBox.setChecked(true);
                    phone_number_for_send.add(contact_for_listview.get_Phone_number());
                    //Log.d("contact_setonitem", contact_for_listview.get_Phone_number());
                }
            }
        });
        */

        //set_emotion_function
        //info = 0 / angry = 1 / fear = 2 / happy = 3 / sad = 4
        imageView_emotion_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_emotion_visible == 0) {
                    set_Emotion_ImageView_Visibility(View.VISIBLE);
                } else {
                    set_Emotion_ImageView_Visibility(View.GONE);
                }
                type_of_emotion = 0;
                imageView_emotion_info.setVisibility(View.VISIBLE);
            }
        });

        imageView_emotion_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_emotion_visible == 0) {
                    set_Emotion_ImageView_Visibility(View.VISIBLE);
                } else {
                    set_Emotion_ImageView_Visibility(View.GONE);
                }
                type_of_emotion = 1;
                imageView_emotion_angry.setVisibility(View.VISIBLE);
            }
        });

        imageView_emotion_fear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_emotion_visible == 0) {
                    set_Emotion_ImageView_Visibility(View.VISIBLE);
                } else {
                    set_Emotion_ImageView_Visibility(View.GONE);
                }
                type_of_emotion = 2;
                imageView_emotion_fear.setVisibility(View.VISIBLE);
            }
        });

        imageView_emotion_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_emotion_visible == 0) {
                    set_Emotion_ImageView_Visibility(View.VISIBLE);
                } else {
                    set_Emotion_ImageView_Visibility(View.GONE);
                }
                type_of_emotion = 3;
                imageView_emotion_happy.setVisibility(View.VISIBLE);
            }
        });

        imageView_emotion_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_emotion_visible == 0) {
                    set_Emotion_ImageView_Visibility(View.VISIBLE);
                } else {
                    set_Emotion_ImageView_Visibility(View.GONE);
                }
                type_of_emotion = 4;
                imageView_emotion_sad.setVisibility(View.VISIBLE);
            }
        });

        //SET SERVICE FUNCTION - STOP, VIEW, CAPTURE, START_ACTIVITY
        //VIEW_FUCNTION
        imageView_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_Comment_Friend_ImageView_Visibility(View.GONE, View.VISIBLE);
                //url 복사 -> OS 버전에 따라서 자동으로 or 클립보드로.
                if (is_version_lower()) {
                    //get url from content provider
                    //clipboard_url = get_history();
                    if (check_Clipboard(clipboardManager)) {
                        clipboard_url = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                    }
                } else {
                    if (check_Clipboard(clipboardManager)) {
                        clipboard_url = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                    }
                }
                //set dialog and show
                set_View_Dialog();
                //set_comment_edittext
                editText_contents_comments.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (editText_contents_comments.getText().toString() == null) {
                                //do nothing
                            } else {
                                //17.06.27 - conatct dialog call
                                //send_comment();
                                //close_view_alert_dialog();
                                call_send_function();
                            }
                        }
                        return false;
                    }

                });
            }
        });
        //set_send_function_COMMENT_DIALOG_IN_VIEW_FUCNTION
        imageView_send_sms.setOnClickListener(new View.OnClickListener() {
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

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_view_alert_dialog();
            }
        });
        //17.06.27 contacts dialog
        //set_send_function_CONTACT_DIALOG_IN_VIEW_FUNCTION_IN
        imageView_send_for_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list_view_click에 따라서 받을 사람의 핸드폰 번호 목록 받아옴
                phone_number_for_send = page999_contact_listview_adapter.get_Pn_ArrayList();
                if(phone_number_for_send.size() != 0) {
                    send_comment(phone_number_for_send);
                    //17.06.27 send 후 모든 dialog close
                    close_contact_dialog(0);
                }else{
                    //Toast.makeText(getApplicationContext(), "SELECT NAME FOR SEND COMMENT", Toast.LENGTH_SHORT).show();
                    simple_dialog_for_aloer(getApplicationContext(), simple_alert_dialog, builder_for_simple_alert_dialog, "받는 사람을 선택해주세요.");
                }
                //text 초기화

            }
        });

        imageView_close_for_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //17.06.27 다 닫고 다시 생성.
                close_contact_dialog(0);
                set_View_Dialog();
                //close 시 pn arraylist 초기화 -> 중복되지 않도록.
                page999_contact_listview_adapter.clear_Pn_ArrayList();
            }
        });

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        mParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION;

        mParams.gravity = Gravity.LEFT;
        mParams.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM; //키보드 나타나면 위치 변경

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mView, mParams);
        comment_Friend.setOnTouchListener(mViewTouchListener);

        //STOP_FUCNTION
        imageView_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        //17.08.28 추가
        //SEARCH_FUCNTION
        //get view
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

        //17.08.28 추가
        imageView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_search_dialog();
            }
        });

        //START_ACTIVITY_FUNCTION
        imageView_start_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialogIntent = new Intent(getApplicationContext(), page0_main.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        });

    }

    //최상단 버튼
    public void mStart() {
        if (isMyServiceRunning(page999_Service_Always_Top.class) == false) {
            startService(new Intent(this, page999_Service_Always_Top.class));
        } else {
            stopService(new Intent(this, page999_Service_Always_Top.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null) {
            mManager.removeView(mView);
            mView = null;
        }
        if (mDialogView != null) {
            if (mDialogView.getParent() != null)
                ((ViewGroup) mDialogView.getParent()).removeView(mDialogView);
        }
    }

    //set service ontouch - deleted 2017.05.10
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    isMove = false;
                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mViewX = mParams.x;
                    mViewY = mParams.y;

                    break;

                case MotionEvent.ACTION_UP:

                    if (!isMove) {
                        if (imageView_stop.getVisibility() == View.VISIBLE) {
                            set_Comment_Friend_ImageView_Visibility(View.GONE, View.VISIBLE);
                        } else {
                            set_Comment_Friend_ImageView_Visibility(View.VISIBLE, View.VISIBLE);
                        }

                        //2017-05-15
                        //버그 : 화면 맨 밑에서 누를 시 위로 이동하는 현상
                        //해결 : 마지막으로 눌렀던 위치로 다시 이동
                        x = (int) (event.getRawX() - mTouchX);
                        y = (int) (event.getRawY() - mTouchY);
                        mParams.x = mViewX + x;
                        mParams.y = mViewY + y;
                        mManager.updateViewLayout(mView, mParams);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    isMove = true;
                    x = (int) (event.getRawX() - mTouchX);
                    y = (int) (event.getRawY() - mTouchY);

                    final int num = 5;
                    if ((x > -num && x < num) && (y > -num && y < num)) {
                        isMove = false;
                        break;
                    }

                    mParams.x = mViewX + x;
                    mParams.y = mViewY + y;
                    mManager.updateViewLayout(mView, mParams);
                    break;
            }
            return true;
        }
    };

    public void set_Comment_Friend_ImageView_Visibility(int function_view_value, int comment_friend_view_value) {
        imageView_stop.setVisibility(function_view_value);
        imageView_view.setVisibility(function_view_value);
        imageView_search.setVisibility(function_view_value);
        imageView_start_activity.setVisibility(function_view_value);
        comment_Friend.setVisibility(comment_friend_view_value);
    }

    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
                new Intent(getApplicationContext(), page999_Sms_Sent.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
                new Intent(getApplicationContext(), page999_Sms_Sent.class), 0);
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
            //Toast.makeText(getBaseContext(), "SMS sending failed...", Toast.LENGTH_SHORT).show();
            simple_dialog_for_aloer(getApplicationContext(), simple_alert_dialog, builder_for_simple_alert_dialog, "SMS sending failed...");
        }
        //sms 정상 전송 시 텍스트창 초기화.
        Toast.makeText(getBaseContext(), "SMS sending completed...", Toast.LENGTH_SHORT).show();
        editText_contents_comments.setText("");
    }


    public boolean check_Clipboard(ClipboardManager c) {
        if (!(c.hasPrimaryClip())) {
            return false;
        } else if (!(c.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
            return false;
        } else {
            return true;
        }
    }


    //check_version < M
    private boolean is_version_lower() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return false;
        }
        return true;
    }

    //get history from contents provider - 2017-05-16 -> 나중에 추가
    private String get_history() {
        String url = "";

        return url;
    }

    //17.08.28 추가
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

    private void set_View_Dialog() {
        //listview_initialize - 17.06.20
        comment_listView_adapter.clear();
        view_builder = new AlertDialog.Builder(getApplicationContext());
        view_builder.setView(mDialogView);
        view_alertDialog = view_builder.create();
        view_alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        view_alertDialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view_alertDialog.getWindow().setGravity(Gravity.TOP);

        //17.06.19 comment listview, db에서 comment_listview에 add ==>> db에 업데이트는 다 됨, 리스트뷰에 출력만 하도록 수정.
        String name = null;
        String comment = null;
        Cursor comment_as_url_cursor;
        if (clipboard_url != null) {
            comment_as_url_cursor = database_manager.get_Comment_as_Url(clipboard_url);
        } else {
            comment_as_url_cursor = null;
        }
        if (comment_as_url_cursor != null) {
            if (comment_as_url_cursor.moveToFirst() && comment_as_url_cursor.getCount() != 0) {
                do {
                    name = comment_as_url_cursor.getString(comment_as_url_cursor.getColumnIndex("NAME"));
                    comment = comment_as_url_cursor.getString(comment_as_url_cursor.getColumnIndex("COMMENT_CONTENTS"));
                    if (name == null)
                        name = "Unknown Friend";
                    //add to listview
                    comment_listView_adapter.add(new page999_Comment_for_Listview(name, comment));
                    Log.d("check1111", comment);
                } while (comment_as_url_cursor.moveToNext());
                comment_as_url_cursor.close();
            } else {
                Log.d("on_top_get_comment", "on_top_get_comment_cursor moveToFirst end");
            }
        } else {
            comment_listView_adapter.add(new page999_Comment_for_Listview("LINK feed", "친구에게 받은 댓글이 아직 없습니다."));
            Log.d("on_top_get_comment", "on_top_get_comment_cursor is null");
        }
        comment_listView_adapter.notifyDataSetChanged();
        //17.06.19 comment listview, db에서 comment_listview에 add ==>> db에 업데이트는 다 됨, 리스트뷰에 출력만 하도록 수정.----------------여기까지.

        if (mDialogView.getParent() != null) {
            ((ViewGroup) mDialogView.getParent()).removeView(mDialogView);
            view_alertDialog.show();
        } else {
            view_alertDialog.show();
        }
        editText_contents_comments.requestFocus();
    }


    private void set_Emotion_ImageView_Visibility(int value) {
        imageView_emotion_info.setVisibility(value);
        imageView_emotion_sad.setVisibility(value);
        imageView_emotion_angry.setVisibility(value);
        imageView_emotion_happy.setVisibility(value);
        imageView_emotion_fear.setVisibility(value);

        if (value == View.GONE) {
            is_emotion_visible = 0;
        } else {
            is_emotion_visible = 1;
        }
    }

    private void send_comment(ArrayList<String> pn_Array_List) {
        contents_of_comment = editText_contents_comments.getText().toString();
        //comment_friend_message[0] = comment_friend / 1 = url / 2 = 댓글 내용 / 3 = emotion / 4 = type(text, image, else)
        String message = "comment_friend<!%" + clipboard_url + "<!%" + contents_of_comment + "<!%" + type_of_emotion + "<!%" + type_of_message;
        //message의 총 길이에 따라 mms / sms 구분해야 함.
        if (message.length() > 80) {
            //17.07.02 -> 구글 url shortner를 이용해서 짧게해서 전달하도록.
            //17.07.12 문구 수정 -> toast에서 dialog로 변경
            //Toast.makeText(getApplicationContext(), "주소, 댓글의 길이가 80자를 초과했습니다.\n80자를 초과해 전송할 수 없습니다.(업데이트 예정)", Toast.LENGTH_SHORT).show();
            simple_dialog_for_aloer(getApplicationContext(), simple_alert_dialog, builder_for_simple_alert_dialog, "주소, 댓글의 길이가 80자를 초과했습니다.\\n80자를 초과해 전송할 수 없습니다.(업데이트 예정)");
        } else
            //17.06.28- pn 배열리스트 완료 -> term을 두고 각각의 String(phone_number)을 다 보내도록
            for(String phone_number: pn_Array_List) {
                sendSMS(phone_number, message);
            }
            set_Emotion_ImageView_Visibility(View.VISIBLE);


    }

    //17.06.23 - SEND 클릭 시 주소록이 출력되고, 해당 주소록에서 클릭으로 수신자 선택. - 아직 미구현.
    //17.06.27 구현 -> checkbox 클릭되도록 추가 진행 -> 완료.
    private void call_send_function() {
        contact_for_listviewArrayList.clear();
        contacts_builder = new AlertDialog.Builder(getApplicationContext());
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
                    Log.d("call_send_function", "add_contact");
                } while (contact_cursor.moveToNext());
                contact_cursor.close();
            } else {
                Log.d("get_Bookmarks_All", "on_top_get_comment_cursor moveToFirst end");
            }
        } else {
            //17.07.12 필요없는 부분 삭제
            /*
            page999_Contact_for_Listview contact_for_listview = new page999_Contact_for_Listview("No Bookmark founded", "");
            contact_for_listviewArrayList.add(contact_for_listview);
            Log.d("get_Bookmarks_All", "on_top_get_comment_cursor is null");
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
                Log.d("call_send_function", "on_top_get_comment_cursor moveToFirst end");
            }
        } else {
            //17.07.12 필요없는 부분 삭제
            /*
            page999_Contact_for_Listview contact_for_listview = new page999_Contact_for_Listview("No Friend founded", "");
            contact_for_listviewArrayList.add(contact_for_listview);
            Log.d("call_send_function", "on_top_get_comment_cursor is null");
            */
        }
        page999_contact_listview_adapter = new page999_Contact_Listview_Adapter(getApplicationContext(), R.layout.page999_contact_for_listview, contact_for_listviewArrayList);
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

    private void close_view_alert_dialog()
    {
        if (view_alertDialog != null) {
            view_alertDialog.dismiss();
        } else {
            Log.d("close_view_alert_dialog", "view_alertDialog is null");
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
