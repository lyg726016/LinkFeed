package com.lyg.lyg.comment_friends_05;

/**
 * Created by L on 2017-05-21.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class page999_Database_Manager extends SQLiteOpenHelper {

    //DB INFO
    private static String DB_NAME = "LINK_feed";
    private static int DB_VERSION = 1;
    //TABLE 1 SETTINGS
    private static String TABLE1_NAME = "SETTINGS";
    private static String TABLE1_COLUMN1_PRIMARY_KEY = "COMMENT_FRIENDS";
    private static String TABLE1_COLUMN2 = "IS_CHECKED_CONTACTS";
    private static String TABLE1_COLUMN3 = "IS_CHECKED_COMMENTS";
    //TABLE 2 CONTACTS
    private static String TABLE2_NAME = "CONTACTS";
    private static String TABLE2_COLUMN1_PRIMARY_KEY = "PHONE_NUMBER";
    private static String TABLE2_COLUMN2 = "NAME";
    //TABLE 3 COMMENTS
    private static String TABLE3_NAME = "COMMENTS";
    private static String TABLE3_COLUMN1_PRIMARY_KEY = "PHONE_NUMBER";
    private static String TABLE3_COLUMN2 = "NAME";
    private static String TABLE3_COLUMN3 = "URL";
    private static String TABLE3_COLUMN4 = "COMMENT_CONTENTS";
    private static String TABLE3_COLUMN5 = "DATETIME";
    //0=info, 1~4
    private static String TABLE3_COLUMN6 = "EMOTION";
    //0=text, 1=image, 2 = else
    private static String TABLE3_COLUMN7 = "TYPE_OF_COMMENT";
    private static String TABLE3_COLUMN8 = "IS_READ";
    //TABLE 4 GROUP
    private static String TABLE4_NAME = "GROUPS";
    private static String TABLE4_COLUMN1 = "PHONE_NUMBER";
    private static String TABLE4_COLUMN2 = "NAME";
    private static String TABLE4_COLUMN3 = "GROUP_NAME";
    //TABLE 5 BOOKMARK
    private static String TABLE5_NAME = "BOOKMARKS";
    private static String TABLE5_COLUMN1_PRIMARY_KEY = "PHONE_NUMBER";
    private static String TABLE5_COLUMN2 = "NAME";




//SETTINGS
    private static String CREATE_TABLE_1 =
            "CREATE TABLE IF NOT EXISTS " + TABLE1_NAME +
                " (" + TABLE1_COLUMN1_PRIMARY_KEY + " text primary key,"
                    + TABLE1_COLUMN2 + " integer default 0, "
                    + TABLE1_COLUMN3 + " integer default 0 );";
//CONTACTS
    private static String CREATE_TABLE_2 =
            "create table if not exists " +TABLE2_NAME +
            " (" + TABLE2_COLUMN1_PRIMARY_KEY + " text primary key,"
                    + TABLE2_COLUMN2 + " text );";
//COMMENTS
    private static String CREATE_TABLE_3 =
            "create table if not exists " +TABLE3_NAME +
            " (" + TABLE3_COLUMN1_PRIMARY_KEY + " text,"
                    + TABLE3_COLUMN2 + " text,"
                    + TABLE3_COLUMN3 + " text,"
                    + TABLE3_COLUMN4 + " text,"
                    + TABLE3_COLUMN5 + " text,"
                    + TABLE3_COLUMN6 + " integer default 0,"
                    + TABLE3_COLUMN7 + " integer default 0,"
                    + TABLE3_COLUMN8 + " integer default 0, " +
                    "PRIMARY KEY ( " + TABLE3_COLUMN1_PRIMARY_KEY + ", " + TABLE3_COLUMN5 + ") );";
//17.05.28 - TABLE의 이름으로 GROUP는 불가능 !
//GROUPS
    private static String CREATE_TABLE_4 =
            "create table if not exists " +TABLE4_NAME +
                    " (" + TABLE4_COLUMN1 + " text,"
                    + TABLE4_COLUMN2 + " text,"
                    + TABLE4_COLUMN3 + " text, " +
                    "PRIMARY KEY ( " + TABLE4_COLUMN1 + ", " + TABLE4_COLUMN3 + ") );";

//CONTACTS
    private static String CREATE_TABLE_5 =
            "create table if not exists " +TABLE5_NAME +
                    " (" + TABLE5_COLUMN1_PRIMARY_KEY + " text primary key,"
                    + TABLE5_COLUMN2 + " text );";

    public page999_Database_Manager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try{
            db.execSQL(CREATE_TABLE_1);
            db.execSQL(CREATE_TABLE_2);
            db.execSQL(CREATE_TABLE_3);
            db.execSQL(CREATE_TABLE_4);
            db.execSQL(CREATE_TABLE_5);
            Log.v("INFO1","creating db");
        }catch( Exception e){
            Log.e("db_manager_onCreate", e.getMessage().toString());
            Log.v("INFO1","creating db error");
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3_NAME);
        onCreate(db);
    }

    //update
    public void update_IS_CHECKED_COMMENTS_in_Setting_Table(int IS_CHECKED_COMMENTS)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COLUMN3, IS_CHECKED_COMMENTS);
        try {
            db.update(TABLE1_NAME, contentValues, null, null);
            Log.d("update_comments", "success");
        }catch (Exception e)
        {
            Log.e("update_comments", e.getMessage().toString());
        }
        db.close();
    }
    public void update_IS_CHECKED_CONTACTS_in_Setting_Table(int IS_CHECKED_CONTACTS)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COLUMN2, IS_CHECKED_CONTACTS);
        try {
            db.update(TABLE1_NAME, contentValues, null, null);
            Log.d("update_contacts", "success");
        }catch (Exception e)
        {
            Log.e("update_contacts", e.getMessage().toString());
        }
        db.close();
    }

    public void update_Contacts(String PHONE_NUMBER, String NAME)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE2_COLUMN2, NAME);
        try {
            db.update(TABLE2_NAME, contentValues, TABLE2_COLUMN1_PRIMARY_KEY + " = " + PHONE_NUMBER, null);
        }catch (Exception e)
        {
            Log.e("update_Contacts", e.getMessage().toString());
        }
        db.close();
    }

    public void update_Comment_as_READ(String PHONE_NUMBER, String URL)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE3_COLUMN8, 1);
        try {
            db.update(TABLE3_NAME,
                    contentValues,
                    TABLE3_COLUMN1_PRIMARY_KEY + " = ? AND " + TABLE3_COLUMN3 + " = ?",
                    new String[]{PHONE_NUMBER, URL});
            Log.e("update_COMMENT_AS_READ", "update try");
        }catch (Exception e)
        {
            Log.e("update_COMMENT_AS_READ", e.getMessage().toString());
        }
        db.close();
    }

    //insert
    public void insert_Setting(String ID, int IS_CHECKED_CONTACTS, int IS_CHECKED_COMMENTS)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COLUMN1_PRIMARY_KEY, ID);
        contentValues.put(TABLE1_COLUMN2, IS_CHECKED_CONTACTS);
        contentValues.put(TABLE1_COLUMN3, IS_CHECKED_COMMENTS);

        try {
            db.insert(TABLE1_NAME, null, contentValues);
        }catch (Exception e)
        {
            Log.e("insert_Setting", e.getMessage().toString());
        }
    }

    public void insert_Bookmarks(String PHONE_NUMBER, String NAME)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE5_COLUMN1_PRIMARY_KEY, PHONE_NUMBER);
        contentValues.put(TABLE5_COLUMN2, NAME);

        try {
            db.insert(TABLE5_NAME, null, contentValues);
        }catch (Exception e)
        {
            Log.e("insert_Setting", e.getMessage().toString());
        }
    }

    public void insert_Contacts(String PHONE_NUMBER, String NAME)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE2_COLUMN1_PRIMARY_KEY, PHONE_NUMBER);
        contentValues.put(TABLE2_COLUMN2, NAME);

        try {
            db.insert(TABLE2_NAME, null, contentValues);
        }catch (Exception e)
        {
            Log.e("insert_Contacts", e.getMessage().toString());
        }
    }

    public void insert_Comments(String PHONE_NUMBER, String NAME, String URL, String COMMENT_CONTENTS, String DATE, int EMOTION, int TYPE, int IS_READ)
    {
        //SimpleDateFormat -> String 형으로 변환 필요.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE3_COLUMN1_PRIMARY_KEY , PHONE_NUMBER);
        contentValues.put(TABLE3_COLUMN2, NAME);
        contentValues.put(TABLE3_COLUMN3, URL);
        contentValues.put(TABLE3_COLUMN4, COMMENT_CONTENTS);
        contentValues.put(TABLE3_COLUMN5, DATE);
        contentValues.put(TABLE3_COLUMN6, EMOTION);
        contentValues.put(TABLE3_COLUMN7, TYPE);
        contentValues.put(TABLE3_COLUMN8, IS_READ);

        try {
            db.insert(TABLE3_NAME, null, contentValues);
            Log.d("insert_Comments", "insert completed");
        }catch (SQLiteConstraintException  e)
        {
            Log.d("insert_Comments", e.getMessage().toString());
        }
    }

    //insert
    //key = 그룹명 + 폰번호, 이미 있을 경우 1을 리턴함으로 에러 알려줌.
    public int insert_Group(String PHONE_NUMBER, String NAME, String GROUP_NAME)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE4_COLUMN1, PHONE_NUMBER);
        contentValues.put(TABLE4_COLUMN2, NAME);
        contentValues.put(TABLE4_COLUMN3, GROUP_NAME);

        try {
            db.insert(TABLE4_NAME, null, contentValues);
        }catch (SQLiteConstraintException e)
        {
            Log.e("insert_Setting", e.getMessage().toString());
            return 1;
        }
        return 0;
    }

    //select
    public int get_IS_CHECKED_CONTACTS()
    {
        //결과 있을 경우 cursor을 넘겨서 IS_CHECKED_CONTACTS, IS_CHECKED_COMMENTS의 값을 통해 확인
        //값이 없을 경우(처음 실행했을 경우) null 리턴함으로 insert 하도록 진행
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE1_NAME, null, null, null, null, null, null);
            if( cursor != null && cursor.moveToFirst() ){
                int is_checked_contacts = cursor.getInt(cursor.getColumnIndex(TABLE1_COLUMN2));
                cursor.close();
                return is_checked_contacts;
            }
        }catch (Exception e)
        {
            Log.e("select_Setting", e.getMessage().toString());
        }
        //error 발생
        return 2;
    }

    public Cursor get_Phone_Number_as_Group_Name(String GROUP_NAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE4_NAME + " where +" + TABLE4_COLUMN3 + "=" + GROUP_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        }else{
            return null;
        }
    }

    public Cursor get_Not_Read_Comment() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE3_NAME + " where +" + TABLE3_COLUMN8 + "=0", null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("get_Not_Read_Comment", "Not_Read_Comment is passing");
            return cursor;
        }else {
            Log.d("get_Not_Read_Comment", "Not_Read_Comment is not passing");
            return null;
        }
    }

    //17.07.01 - 이미 읽은 comment를 시간순으로 내림차순으로 가져옴 -> 15개 까지만 화면에 출력
    public Cursor get_Read_Comment() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE3_NAME + " where +" + TABLE3_COLUMN8 + "=1 order by datetime(datetime) DESC", null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("get_Read_Comment", "get_Read_Comment is passing");
            return cursor;
        }else {
            Log.d("get_Read_Comment", "get_Read_Comment is not passing");
            return null;
        }
    }

    public Cursor get_Comment_as_Phone_Number(String phone_number){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE3_NAME + " where +" + TABLE3_COLUMN1_PRIMARY_KEY + "=" + phone_number, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        }else{
            return null;
        }
    }

    public String get_Phone_Number_as_Name(String Name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE2_NAME + " where +" + TABLE2_COLUMN2 + "=" + Name, null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("get_Phone_Number", "phone_number is passing");
            return cursor.getString(cursor.getColumnIndex(TABLE2_COLUMN1_PRIMARY_KEY));
        }else{
            Log.d("get_Phone_Number", "phone_number is not passing");
            return null;
        }
    }

    public String get_Name_as_Phone_Number(String Phone_Number){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("get_Name_Number", Phone_Number);
        Cursor cursor = db.rawQuery("select * from " + TABLE2_NAME + " where +" + TABLE2_COLUMN1_PRIMARY_KEY + "= ?; ", new String[] {Phone_Number});
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("get_Name_as_PN", "NAME is passing");
            return cursor.getString(cursor.getColumnIndex(TABLE2_COLUMN2));
        }else{
            Log.d("get_Name_as_PN", "NAME is NOT passing");
            return null;
        }
    }

    public Cursor get_Comment_as_Url(String url){
        if(url == null){
            return null;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE3_NAME + " where +" + TABLE3_COLUMN3 + "= ?; ", new String[] {url});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        }else{
            return null;
        }
    }

    public Cursor get_Contacts_All(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE2_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("get_Contacts_All", "return cursor");
            return cursor;
        }else{
            Log.d("get_Contacts_All", "cursor doesn't work");
            return null;
        }
    }

    public Cursor get_Group_All(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE4_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("get_Contacts_All", "return cursor");
            return cursor;
        }else{
            Log.d("get_Contacts_All", "cursor doesn't work");
            return null;
        }
    }

    public Cursor get_Bookmarks_All(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE5_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("get_Bookmarks_All", "return cursor");
            return cursor;
        }else{
            Log.d("get_Bookmarks_All", "cursor doesn't work");
            return null;
        }
    }

    //delete
    public void delete_Contacts_as_Phone_Number(String phone_number)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE2_NAME, TABLE2_COLUMN1_PRIMARY_KEY + "=" + phone_number, null);
        db.close();
    }
}
