package com.lyg.lyg.comment_friends_05;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by L on 2017-06-27.
 */

public class page999_Contact_Listview_Adapter extends ArrayAdapter<page999_Contact_for_Listview> {

    private page999_Contact_Listview_Adapter.ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private Context mContext = null;
    private ArrayList<String> pn_ArrayList = new ArrayList<String>();
    private ArrayList<String> name_ArrayList = new ArrayList<String>();
    private TextView selected_name;
    private View view_for_dilaog_for_contacts;
    private page999_Database_Manager database_manager;


    public page999_Contact_Listview_Adapter(Context c, int textViewResourceId,
                                            ArrayList<page999_Contact_for_Listview> arrays) {
        super(c, textViewResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        if ( convertview == null ) {
            viewHolder = new page999_Contact_Listview_Adapter.ViewHolder();
            convertview = inflater.inflate(R.layout.page999_contact_for_listview, null);
            viewHolder.name_textview = (TextView) convertview.findViewById(R.id.page999_contact_name);
            //17.07.02 -> checkbox에서 텍스뷰로 변경
            selected_name = (TextView)view_for_dilaog_for_contacts.findViewById(R.id.page999_contact_textview_for_selected);
            selected_name.setText("받는사람 목록");
            viewHolder.name_textview.setTag(viewHolder);
            database_manager =  new page999_Database_Manager(getContext());
            convertview.setTag(viewHolder);
        }else{
        viewHolder = (ViewHolder) convertview.getTag ();
    }
        viewHolder.name_textview.setText(getItem(position).get_name());
        //17.07.02
        // 누르면 다른게 눌림 -> 기존 방식으로 진행 -> textview에 추가하는 방식으로 변경
        final page999_Contact_for_Listview contact_for_listview = getItem(position);
        viewHolder.name_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //17.07.12
                //받는 사람 목록에 추가됐는지 확인 (1 = 추가됐음 -> 다시 누르면 삭제, 0 -> 추가 안됐음 -> 눌렀을 때 추가)
                if (contact_for_listview.get_is_selected() == 1) {
                    contact_for_listview.set_is_selected(0);
                    pn_ArrayList.remove(contact_for_listview.get_Phone_number());
                    name_ArrayList.remove(contact_for_listview.get_name());
                    if(name_ArrayList.size() ==0){
                        selected_name.setText("받는사람 목록");
                    }else {
                        for (int i = 0; i < name_ArrayList.size(); i++) {
                            String selected_name_string = selected_name.getText().toString();
                            if (i > 0) {
                                selected_name.setText(selected_name_string + ", " + (String) name_ArrayList.get(i));
                            } else {
                                selected_name.setText((String) name_ArrayList.get(i));
                            }
                        }
                    }
                } else {
                    contact_for_listview.set_is_selected(1);
                    //17.07.12
                    //그룹인지 아닌 지 구분해서 전달, 그룹이면 해당 그룹에 포함된 폰번호 받아서 하나씩 추가
                    if(contact_for_listview.get_name().contains("그룹.")) //그룹 친구
                    {
                        //선택한 이름에서 "(그룹)"을 제거하고, db에 select문 실행 -> curor을 이용해 각각의 폰번호를 선택한 배열리스트(pn_ArrayList)에 추가. -> 수정
                        //추가할 때부터 그냥 (그룹)을 추가해서 db에 입력 -> (그룹)에서 그룹. 으로 변경 db에러
                        //String group_name = contact_for_listview.get_name().replace("(그룹)","");
                        Cursor group_phone_number_cursor = database_manager.get_Phone_Number_as_Group_Name(contact_for_listview.get_name().toString());
                        if (group_phone_number_cursor != null) {
                            if (group_phone_number_cursor.moveToFirst() && group_phone_number_cursor.getCount() != 0) {
                                do {
                                    String phone_number = group_phone_number_cursor.getString(group_phone_number_cursor.getColumnIndex("PHONE_NUMBER"));
                                    pn_ArrayList.add(phone_number);
                                    name_ArrayList.add(contact_for_listview.get_name());
                                    Log.d("contact_lv_adapter", "add_contact");
                                } while (group_phone_number_cursor.moveToNext());
                                group_phone_number_cursor.close();
                            } else {
                                Log.d("contact_lv_adapter", "group_phone_number_cursor moveToFirst end");
                            }
                        } else {
                            Log.d("contact_lv_adapter", "group_phone_number_cursor is null");
                        }
                    }else { //그룹이 아닌 일반 친구
                        pn_ArrayList.add(contact_for_listview.get_Phone_number());
                        name_ArrayList.add(contact_for_listview.get_name());
                    }
                    if(name_ArrayList.size() ==0){
                        selected_name.setText("받는사람 목록");
                    }else {
                        for (int i = 0; i < name_ArrayList.size(); i++) {
                            String selected_name_string = selected_name.getText().toString();
                            if (i > 0) {
                                selected_name.setText(selected_name_string + ", " + (String) name_ArrayList.get(i));
                            } else {
                                selected_name.setText((String) name_ArrayList.get(i));
                            }
                        }
                    }
                }
            }
        });
        return convertview;
    }

    class ViewHolder{
        TextView name_textview = null;
    }
    //17.02.02 -> 취소 -> 다시 진행 -> pn배열을 통해서 받는 사람 전화번호 전달 -> send 누르면 배열의 전화번호 각각에 전달.
    public ArrayList<String> get_Pn_ArrayList()
    {
        return this.pn_ArrayList;
    }
    public void clear_Pn_ArrayList()
    {
        this.pn_ArrayList.clear();
    }
    public void set_view_for_dilaog_for_contacts(View v){
        this.view_for_dilaog_for_contacts = v;
    }
}
