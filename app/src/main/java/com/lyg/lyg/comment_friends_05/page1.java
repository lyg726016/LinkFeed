package com.lyg.lyg.comment_friends_05;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Junyoung on 2016-06-23.
 */

public class page1 extends Fragment {
    //root_View
    private View page1_view;

    //list
    private LinkedHashMap<String, page1_Parent> subjects = new LinkedHashMap<String, page1_Parent>();
    private ArrayList<page1_Parent> deptList = new ArrayList<page1_Parent>();
    private page1_Expandable_ListView_Adapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    //db
    private static String db_setting_table_id = "COMMENT_FRIENDS";
    private static page999_Database_Manager database_manager;
    private static String TABLE1_COLUMN2 = "IS_CHECKED_CONTACTS";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        page1_view = inflater.inflate(R.layout.page1, container, false);


        //Database
        database_manager = new page999_Database_Manager(this.getContext());
        int is_checked_contacts = database_manager.get_IS_CHECKED_CONTACTS();

        //is_checked_contacts == 1, 연락처 불러오기 완료
        //is_checked_contacts == 0, 연락처 불러오기 해야 함
        //is_checked_contacts == 2, db 생성 실패 -> 다시 생성
        if(is_checked_contacts == 1)
        {
            Log.d("page1_is_contact", "excute get contacts is already done");
            loadData();
            //do nothing
        }else if(is_checked_contacts == 0){
            Log.d("page1_is_contact", "excute get contacts");
            //연락처 불러오기 실행
            fetchContacts();
            database_manager.update_IS_CHECKED_CONTACTS_in_Setting_Table(1);
            loadData();
        }else{
            Log.d("page1_is_contact", "excute insert_setting");
            database_manager.insert_Setting(db_setting_table_id, 0, 0);
            Log.d("page1_is_contact", "excute get contacts");
            //연락처 불러오기 실행
            fetchContacts();
            database_manager.update_IS_CHECKED_CONTACTS_in_Setting_Table(1);
            loadData();
        }

        //
        // add data for displaying in expandable list view


        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) page1_view.findViewById(R.id.page1_expandable_listview);
        // create the adapter by passing your ArrayList data
        listAdapter = new page1_Expandable_ListView_Adapter(getActivity(), deptList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(listAdapter);

        //expand all the Groups
        expandAll();

        // setOnChildClickListener listener for child row click
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                page1_Parent headerInfo = deptList.get(groupPosition);
                //get the child info
                page1_Child detailInfo =  headerInfo.getProductList().get(childPosition);
                //display it or do something with it
                //Toast.makeText(getActivity(), " Clicked on :: " + headerInfo.getName() + "/" + detailInfo.getName(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                page1_Parent headerInfo = deptList.get(groupPosition);
                //display it or do something with it
                //Toast.makeText(getActivity(), " Header is :: " + headerInfo.getName(), Toast.LENGTH_LONG).show();

                return false;
            }
        });
        //

        return page1_view;
    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(){

        String phoneNumber;
        String name = null;
        String group_name;
        database_manager = new page999_Database_Manager(this.getContext());
        Cursor contacts_cursor = database_manager.get_Contacts_All();
        Cursor group_name_cursor = database_manager.get_Group_All();
        Cursor group_bookmarks_cursor = database_manager.get_Bookmarks_All();

        //get_bookmarks
        if(group_bookmarks_cursor == null)
            addProduct("친한친구 등록", "상단 왼쪽의 + 모양 버튼을 누르면 등록할 수 있습니다");

        if(group_bookmarks_cursor != null && group_bookmarks_cursor.moveToFirst())
        {
            do {
                name = group_bookmarks_cursor.getString(group_bookmarks_cursor.getColumnIndex("NAME"));
                addProduct("친한 친구", name);
            }while (group_bookmarks_cursor.moveToNext());
        }else {
            Log.d("loadData", "group_bookmarks_cursor doesn't work");
        }

        //get_groups
        if(group_name_cursor == null)
            addProduct("그룹 생성", "상단 왼쪽의 + 모양 버튼을 누르면 등록할 수 있습니다");

        if(group_name_cursor != null && group_name_cursor.moveToFirst())
        {
            do {
            group_name = group_name_cursor.getString(group_name_cursor.getColumnIndex("GROUP_NAME"));
            name = group_name_cursor.getString(group_name_cursor.getColumnIndex("NAME"));
            addProduct(group_name, name);
            }while (group_name_cursor.moveToNext());
        }else {
            Log.d("loadData", "group_name_curor doesn't work");
        }

        //get_friends
        if(contacts_cursor != null && contacts_cursor.moveToFirst())
        {
            do {
                name = contacts_cursor.getString(contacts_cursor.getColumnIndex("NAME"));
                addProduct("친구", name);
                Log.d("loadData", "cursor next");
            }while (contacts_cursor.moveToNext());
        }else{
            Log.d("loadData", "contacts_cursor doesn't work");
        }
    }

    //get_contacts
    public void fetchContacts() {

        String phoneNumber = null;
        String email = null;

        //info_type
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phoneNumber = phoneNumber.replaceAll("[\\s\\-()]", "");
                        phoneNumber = phoneNumber.trim();
                        Log.d("phon_nn", phoneNumber);
                    }
                    phoneCursor.close();
                }
                //name, phoneNumber
                //if database == null -> database insert
                database_manager.insert_Contacts(phoneNumber, name);
            }
            //수정소스
        }
    }

    //here we maintain our products in various departments
    private int addProduct(String department, String product){

        int groupPosition = 0;

        //check the hash map if the group already exists
        page1_Parent headerInfo = subjects.get(department);
        //Log.d(department, department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new page1_Parent();
            headerInfo.setName(department);
            subjects.put(department, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<page1_Child> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        page1_Child detailInfo = new page1_Child();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(product);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

}