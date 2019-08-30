package com.lyg.lyg.comment_friends_05;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;


public class page0_main extends AppCompatActivity {

    private ViewPager viewPager;
    //tab_Layout
    private TabLayout tabLayout;
    private TabLayout.Tab contacts;
    private TabLayout.Tab comments;
    private TabLayout.Tab browser;

    //onBackPress
    private page999_BackPressCloseHandler backPressCloseHandler;

    //page_Adapter
    private page999_tabpage_Adapter pagerAdapter;

    //permission
    private boolean is_SMS_permission_checked = false;
    private boolean is_CONTACTS_permission_checked = false;

    //clickable_component
    private ImageButton page0_Image_btn_Comment_Friend;

    //set_tab_ position
    private int page1 = 1;
    public static String POSITION = "POSITION";
    private static int position_int = 0;

    //overray_permission
    private static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
    private String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS};

    //imvage_view_for_functrion
    private ImageView page1_add_contact;
    private ImageView page1_search_contact;
    private ImageView page2_refresh;
    private ImageButton page0_start_notification;
    private ImageButton page0_share_to_othre;

    //dialog_for_add_contact
    AlertDialog.Builder view_builder;
    View mDialogView_for_add_contact;
    AlertDialog view_alertDialog;
    EditText edittext_for_add_contact_name;
    EditText edittext_for_add_contact_phone_number;
    EditText edittext_for_add_contact_group_name;
    CheckBox add_to_favority;
    CheckBox add_to_group;

    //db_for_add_contact
    private static page999_Database_Manager database_manager;

    //notification
    private static int NOTIFICATION_ID = 889;
    Notification Notifi;
    NotificationManager Notifi_M;
    boolean is_notification_activated = false;
    ImageButton notification_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page0_main);

        //notification
        Notifi_M = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //onBackPress
        backPressCloseHandler = new page999_BackPressCloseHandler(this);

        // Adding Toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialogView_for_add_contact = mInflater.inflate(R.layout.page999_dialog_for_add_contact, null);
        edittext_for_add_contact_name = (EditText)mDialogView_for_add_contact.findViewById(R.id.add_contact_name);
        edittext_for_add_contact_phone_number = (EditText)mDialogView_for_add_contact.findViewById(R.id.add_contact_phone_number);
        edittext_for_add_contact_group_name = (EditText)mDialogView_for_add_contact.findViewById(R.id.add_contact_group_name);
        //add_contact
        add_to_favority = (CheckBox)mDialogView_for_add_contact.findViewById(R.id.add_contact_favority_checkbox);
        add_to_group = (CheckBox)mDialogView_for_add_contact.findViewById(R.id.add_contact_group_checkbox);

        setTabLayout();

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //imvage_view_for_functrion
        page1_add_contact = (ImageView)findViewById(R.id.page1_add_contact);
        page1_search_contact = (ImageView)findViewById(R.id.page1_search_contact);
        page2_refresh = (ImageView)findViewById(R.id.page2_refresh);
        page0_start_notification = (ImageButton)findViewById(R.id.page0_start_notification);
        page0_share_to_othre = (ImageButton)findViewById(R.id.page0_main_share_to_other);

        //db_for_add_contact
        database_manager = new page999_Database_Manager(getApplicationContext());

        //Permission
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if(!hasPermissions(this, PERMISSIONS)) {
                getPermission();
            }else {
                setViewPager();
            }
        }else {
            setViewPager();
        }

        //imageBtn
        page0_Image_btn_Comment_Friend = (ImageButton)findViewById(R.id.page0_Main_Image_btn_Comment_Friend);
        //image_button_click
        page0_Image_btn_Comment_Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStart();
            }
        });
        page0_start_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_version_lower()){
                    if(is_notification_activated == true){
                        Notifi_M.cancelAll();
                        is_notification_activated = false;
                        Toast.makeText(getApplicationContext(), "       알림창을 제거 했습니다.       \n       오른쪽의 아이콘을 클릭하시면       \n       떠다니는 아이콘이 생성됩니다.       " , Toast.LENGTH_LONG).show();
                    }else{
                        set_Notification(getApplicationContext());
                        is_notification_activated = true;
                        Toast.makeText(getApplicationContext(), "       알림창을 추가 했습니다.       \n       주소를 복사 후 클릭하면       \n       쉽게 댓글을 공유할 수 있습니다.       " , Toast.LENGTH_LONG).show();
                    }
                }else{
                    //VERSION = M 면 위에서 문구 부적합 문제 해결, 알림창이 있는 지 확인 후 있을 때만 제거하도록.
                    close_Notification();
                }
            }
        });

        page0_share_to_othre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareClick(v);
            }
        });

        //add_contact_add_group_checkbox_event
        //17.07.12 해당 체크박스 누를 때만 그룹이름 입력칸 나오도록.
        add_to_group.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(add_to_group.isChecked() == true){
                    edittext_for_add_contact_group_name.setVisibility(View.VISIBLE);
                    edittext_for_add_contact_group_name.setText("");
                }else{
                    edittext_for_add_contact_group_name.setVisibility(View.GONE);
                }
            }
        });


        //function_add_contact_on_page1
        page1_add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //17.07.12 - 페이지에 상관없이 항상 연락처 추가로 고정.
                /*
                if(position_int == 0 ){
                //17.07.12 - 페이지에 상관없이 항상 연락처 추가로 고정.
                */
                    view_builder = new AlertDialog.Builder(getApplicationContext());
                    view_builder.setView(mDialogView_for_add_contact);
                    view_builder.setTitle("LINK feed");
                    view_builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            close_view_alert_dialog();
                        }
                    });
                    view_builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(edittext_for_add_contact_name.getText().length() != 0) {
                                if (edittext_for_add_contact_phone_number.getText().length() != 0) {
                                    String phone_number_for_add_contact = edittext_for_add_contact_phone_number.getText().toString().replaceAll("[\\s\\-()]", "");
                                    phone_number_for_add_contact = phone_number_for_add_contact.trim();
                                    boolean is_clear = true;

                                    if(add_to_favority.isChecked() == true || add_to_group.isChecked() == true) { //친한친구나 그룹에 추가하는 경우
                                    //17.07.09 add to favority 기능 추가
                                    if (add_to_favority.isChecked() == true) { //친한친구 추가에 체크된 경우
                                        database_manager.insert_Bookmarks(phone_number_for_add_contact, edittext_for_add_contact_name.getText().toString());
                                        Toast.makeText(getApplicationContext(), edittext_for_add_contact_name.getText().toString() + "님이 친한친구에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    if(add_to_group.isChecked() == true){ //그룹추가에 체크된 경우
                                        if (edittext_for_add_contact_group_name.getText().length() != 0) {
                                            int return_value_from_insert_group = database_manager.insert_Group(phone_number_for_add_contact, edittext_for_add_contact_name.getText().toString(), "그룹_"+ edittext_for_add_contact_group_name.getText().toString());
                                            //그룹에 추가 중 에러가 발생했는지 확인
                                            if (return_value_from_insert_group == 0) {//정상적으로 추가 된 경우(그룹명 + 폰번호의 key가 겹치지 않은 경우)
                                                Toast.makeText(getApplicationContext(), edittext_for_add_contact_name.getText().toString() + "님이 " + edittext_for_add_contact_group_name.getText().toString() + " 그룹에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                            }else {//정상 추가 안된경우 (key에러, db에러 등)
                                                Toast.makeText(getApplicationContext(), edittext_for_add_contact_name.getText().toString() + "님의 번호가 이미 해당 그룹에 있습니다.", Toast.LENGTH_SHORT).show();
                                                is_clear = false;
                                            } //정상 추가 안된경우 (key에러, db에러 등)
                                        } else {//그룹 이름을 입력 안 한 경우
                                            Toast.makeText(getApplicationContext(), "그룹 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                            is_clear = false;
                                        }//그룹 이름을 입력 안 한 경우
                                        }//그룹추가에 체크된 경우
                                        if(is_clear == true){//문제 없이 정상 추가완료 => 초기화 진행, 아닌 경우에는 초기화 안하고 놔둠.
                                            initializing_component_in_add_contact();
                                            database_manager.update_IS_CHECKED_CONTACTS_in_Setting_Table(0);
                                        }
                                    }else {//친한친구, 그룹 추가가 아닌 경우, 일반 연락처에 추가
                                        database_manager.insert_Contacts(phone_number_for_add_contact, edittext_for_add_contact_name.getText().toString());
                                        database_manager.update_IS_CHECKED_CONTACTS_in_Setting_Table(0);
                                        Toast.makeText(getApplicationContext(), edittext_for_add_contact_name.getText().toString() + "님이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                        initializing_component_in_add_contact();
                                    }
                                }else{ //핸드폰 번호란이 비어있는 경우
                                    Toast.makeText(getApplicationContext(), "친구의 핸드폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }else { //이름란이 비어있는 경우
                                Toast.makeText(getApplicationContext(), "친구의 이름을 입력해주세요", Toast.LENGTH_SHORT).show();;
                            }
                        }
                    });
                    //set_dialog -> 나중에 따로 함수로 변환
                    view_alertDialog = view_builder.create();
                    view_alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    view_alertDialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                            | android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    view_alertDialog.getWindow().setGravity(Gravity.TOP);

                    if (mDialogView_for_add_contact.getParent() != null) {
                        ((ViewGroup) mDialogView_for_add_contact.getParent()).removeView(mDialogView_for_add_contact);
                        view_alertDialog.show();
                    } else {
                        view_alertDialog.show();
                    }
                //17.07.12 - 페이지에 상관없이 항상 연락처 추가로 고정.
                /*
                }

                else if(position_int == 1){


                }else{

                }
                //17.07.12 - 페이지에 상관없이 항상 연락처 추가로 고정.
                */
            }
        });
        page1_search_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position_int == 0)
                {

                }else if(position_int == 1){

                }else{

                }
            }
        });
        page2_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position_int == 0 || position_int == 1)
                    pagerAdapter.notifyDataSetChanged();
            }
        });

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void close_view_alert_dialog() {
        {
            if (view_alertDialog != null) {
                view_alertDialog.dismiss();
            } else {
                Log.d("page2_grid_adapter", "view_alertDialog is null");
            }
        }
    }

    void setViewPager()
    {
        // Creating TabPagerAdapter adapter
        pagerAdapter = new page999_tabpage_Adapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        contacts.setIcon(R.drawable.tablayout_contacts_black);
                        comments.setIcon(R.drawable.tablayout_comments_white);
                        browser.setIcon(R.drawable.tablayout_browser_white);
                        position_int = 0;
                        //17.07.15
                        //share 추가
                        page0_share_to_othre.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        contacts.setIcon(R.drawable.tablayout_contacts_white);
                        comments.setIcon(R.drawable.tablayout_comments_black);
                        browser.setIcon(R.drawable.tablayout_browser_white);
                        position_int = 1;
                        //17.07.15
                        //share 추가
                        page0_share_to_othre.setVisibility(View.INVISIBLE);
                        //refresh_viewpager -17.06.18 / 화면 전환이 비효율적임
                        //pagerAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        contacts.setIcon(R.drawable.tablayout_contacts_white);
                        comments.setIcon(R.drawable.tablayout_comments_white);
                        browser.setIcon(R.drawable.tablayout_browser_black);
                        position_int = 2;
                        //17.07.15
                        //share 추가
                        page0_share_to_othre.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(page1);
    }

    void setTabLayout()
    {
        contacts = tabLayout.newTab();
        comments = tabLayout.newTab();
        browser = tabLayout.newTab();

        contacts.setIcon(R.drawable.tablayout_contacts_white);
        comments.setIcon(R.drawable.tablayout_comments_white);
        browser.setIcon(R.drawable.tablayout_browser_white);
        tabLayout.addTab(contacts);
        tabLayout.addTab(comments);
        tabLayout.addTab(browser);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
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
    //permission_Check
    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            if (!Settings.canDrawOverlays(this)) {
                return false;
            }
        }
        return true;
    }

    //other_permission
    public void getPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS},
                1);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                2);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    is_SMS_permission_checked = true;
                    if(is_CONTACTS_permission_checked == true)
                    {
                        Toast.makeText(this, "다른 앱 위에 그리기를 허용해주세요.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    is_SMS_permission_checked = false;
                    Toast.makeText(this, "SMS 권한 거부\n" +
                            "재실행 후 권한을 다시 설정해주세요.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    is_CONTACTS_permission_checked = true;
                    if(is_SMS_permission_checked == true){
                        Toast.makeText(this, "다른 앱 위에 그리기를 허용해주세요.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    is_CONTACTS_permission_checked = false;
                    Toast.makeText(this, "CONTACTS 권한 거부\n" +
                                    "재실행 후 권한을 다시 설정해주세요.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //overray_permission
    @RequiresApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "다른 앱 위에 그리기가 허용되지 않았습니다.\n" +
                        "재실행 후 허용해주시면 정상 실행됩니다.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //허용
                Toast.makeText(this, "다른 앱 위에 그리기가 허용됐습니다." , Toast.LENGTH_SHORT).show();
                if(is_CONTACTS_permission_checked == true && is_SMS_permission_checked == true)
                {
                    setViewPager();
                }
            }
        }
    }

    //17.07.12 add_contact 후 초기화
    private void initializing_component_in_add_contact()
    {
        pagerAdapter.notifyDataSetChanged();
        edittext_for_add_contact_phone_number.setText("");
        edittext_for_add_contact_name.setText("");
        edittext_for_add_contact_group_name.setText("");
        add_to_favority.setChecked(false);
        add_to_group.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void showToast(String toastContents, int time)
    {
        Toast.makeText(this, toastContents, time).show();
    }

    private void set_Vilsibility_Function_ImageView(int page_number)
    {
        switch (page_number)
        {
            case 0 :
                break;
            case 1 :
                break;
            case 2 :
                break;
            default:
                break;
        }
    }
    //17.07.12
    //notification
    private void set_Notification(Context context){
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.page999_nitification_custom);
        //custon_notification으로 변경 -> 삭제
        /*
        Intent intent = new Intent(context, page999_Service_Always_Top.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        */

        //17.08.04 -> custom notification btn click
        //17.09.02
        //문제점발생 -> 이미 존재할 경우 제거를 해야함 -> ?
        Intent intent_for_icon = new Intent(context, page999_Service_Always_Top.class);
        PendingIntent pendingIntent_for_icon = PendingIntent.getService(context, 0, intent_for_icon, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_icon, pendingIntent_for_icon);
        //17.09.02 -> custion_nitification에서 search btn 클릭 -> search dialog 생성
        Intent intent_for_search = new Intent(context, page999_Dialog_for_Search.class);
        PendingIntent pendingIntent_for_search = PendingIntent.getActivity(context, 0, intent_for_search, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_search, pendingIntent_for_search);

        Intent intent_for_start_app = new Intent(context, page0_main.class);
        PendingIntent pendingIntent_for_start_app = PendingIntent.getActivity(context, 0, intent_for_start_app, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_start_app, pendingIntent_for_start_app);

        Intent intent_for_comment = new Intent(context, page999_Comment.class);
        PendingIntent pendingIntent_for_comment = PendingIntent.getService(context, 0, intent_for_comment, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_comment, pendingIntent_for_comment);

        //17.08.05 클릭 시 알림창 숨기기 -> comment 서비스 실행 시 알림창 닫는 방식으로 진행 -> 정상 작동 확인
        /*
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
        */

        //17.08.07 -> notification에서 x 버튼 클릭 시 notification 제거 -> 작동 안함
        //notification id를 브로드캐스트 리시버로 전달 -> 리시버에서 해당 notifiaction 제거
        Intent intent_for_close = new Intent(context, page999_Notification_Manager.class);
        intent_for_close.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent pendingIntent_for_close = PendingIntent.getBroadcast(context, 0, intent_for_close,0);
        remoteViews.setOnClickPendingIntent(R.id.notification_close, pendingIntent_for_close);

        Notifi = new Notification.Builder(context)
                .setContent(remoteViews)
                .setSmallIcon(R.drawable.tablayout_comments_white)
                .setOngoing(true)
                .build();
        Notifi_M.notify( NOTIFICATION_ID , Notifi);
    }

    //17.07.13
    //main_activity_life_cycle
    @Override
    public void onPause() {
        super.onPause();
    }
    //이전 실행 시의 page로 다시 시작
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //17.06.17
        //Activity 종료 시 서비스 종료? -> 종료 안하는게 낫다, 계속 켜져있게.
        //stopService(new Intent(this, page999_Service_Always_Top.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasPermissions(this, PERMISSIONS))
        {
            try{
                pagerAdapter.notifyDataSetChanged();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "일시적인 데이터베이스 초기화 오류",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //17.07.15 추가
    //share from LINK feed to other app
    public void onShareClick(View v) {
        Resources resources = getResources();

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_native)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));
        emailIntent.setType("message/rfc822");

        PackageManager pm = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");


        Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text));

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if(packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
            } else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm") || packageName.contains("kakao.talk")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if(packageName.contains("twitter")) {
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_twitter));
                } else if(packageName.contains("facebook")) {
                    // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                    // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                    // will show the <meta content ="..."> text from that page with our link in Facebook.
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_facebook));
                } else if(packageName.contains("mms")) {
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_sms));
                } else if(packageName.contains("android.gm")) { // If Gmail shows up twice, try removing this else-if clause and the reference to "android.gm" above
                    intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_gmail)));
                    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));
                    intent.setType("message/rfc822");
                } else if(packageName.contains("kakao.talk")) {
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_kakaotalk));
                }

                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }

    //check_version < M
    private boolean is_version_lower() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return false;
        }
        return true;
    }

    //17.08.07
    //API Mashmallow 에서는 notification의 id를 통해서 동작하도록 가능 -> 정상 작동 및 문구 부적합 해결
    @TargetApi(Build.VERSION_CODES.M)
    private void close_Notification()
    {
        Notifi_M = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = Notifi_M.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == NOTIFICATION_ID) {
                Notifi_M.cancelAll();
                Toast.makeText(getApplicationContext(), "       알림창을 제거 했습니다.       \n" +
                        "       오른쪽의 아이콘을 클릭하시면       \n" +
                        "       떠다니는 아이콘이 생성됩니다.       " , Toast.LENGTH_LONG).show();
                return;
            }
        }
        set_Notification(getApplicationContext());
        Toast.makeText(getApplicationContext(), "       알림창을 추가 했습니다.       \n" +
                "       주소를 복사 후 클릭하면       \n" +
                "       쉽게 댓글을 공유할 수 있습니다.       " , Toast.LENGTH_LONG).show();
    }
}