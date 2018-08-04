package com.zither.aiiage.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyanqin
 * @date 2018/08/04
 */
public class DatebaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatebaseHelper";
    private static final String DATABASE_NAME = "user.db";
    public static final String USERTABLE_NAME = "user_table";
    private static final int DATABASE_VERSITION = 2;
    /**
     * Columns
     */
    public static final String user_id = "ID";
    public static final String user_name = "NAME";
    public static final String user_intro = "INTRO";
    /**
     * select type
     */
    private final int COL_TYPE_ID = 1;
    private final int Col_TYPE_NAME = 2;

    public DatebaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSITION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createDatebase = "create table " + USERTABLE_NAME
                + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + user_name + " TEXT,"
                + user_intro + " TEXT)";
        sqLiteDatabase.execSQL(createDatebase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /**
        update versition
         */
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERTABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * insert user
     */
    public boolean insertUser(UserBean userBean) {
        if (!isUserAvailabel(userBean)) {
            return false;
        }
        if (userBean.getUid() != null && userBean.getUid().length() > 1) {
            if (queryUserBy(COL_TYPE_ID,userBean.getUid()).size()>0){
                Log.i(TAG, "insertUser: user is existed");
                return false;
            }
            Log.d(TAG, "insertUser: ignore user' id");
        }
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(user_name,userBean.getName());
        contentValues.put(user_intro,userBean.getIntro());
        return db.insert(USERTABLE_NAME,null,contentValues) !=-1;
    }
    /**
     * update user
     */
    public boolean updateUser(UserBean userBean){
        if (!isUserAvailabel(userBean)){
            return false;
        }
        if (userBean.getUid()==null||userBean.getUid().length()<1){
            Log.i(TAG, "updateUser: uid is not existed");
            return false;
        }
        if (queryUserBy(COL_TYPE_ID,userBean.getUid()).size()<1){
            Log.i(TAG, "updateUser: userbean is null ");
            return false;
        }
        SQLiteDatabase db =getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(user_id,userBean.getUid());
        contentValues.put(user_name,userBean.getName());
        contentValues.put(user_intro,userBean.getIntro());
        db.update(USERTABLE_NAME,contentValues,"ID = ?",new String[]{userBean.getUid()});
        return false;
    }
    /**
     * according id to delete user
     */
    public boolean deleteUserById(String id){
        if (id==null||id.length()<1){
            Log.d(TAG, "deleteUserById: id is null");
        }
       return queryUserBy(COL_TYPE_ID,id).size()>1 &&deleteUser(COL_TYPE_ID,id);
    }
    /**
     * according name to delete user
     */
    public boolean deleteUserByName(String user_name){
        if (user_name==null||user_name.length()<1){
            Log.d(TAG, "deleteUserByName: user is not existed");
        }
        return queryUserBy(Col_TYPE_NAME,user_name).size()>1 && deleteUser(Col_TYPE_NAME,user_name);
    }
    /**
     * delete user
     */
    private boolean deleteUser(int col_type_id, String id) {
        SQLiteDatabase db =this.getWritableDatabase();
        if (col_type_id==COL_TYPE_ID){
            return db.delete(USERTABLE_NAME,"ID=?",new String[]{id})>0;
        }
        return col_type_id==Col_TYPE_NAME&&db.delete(USERTABLE_NAME,"NAME",new String[]{id})>0;
    }

    /**
     * get all user
     */
    public List<UserBean> getAllUser() {
        return queryUserBy(0, null);
    }
    /**
     * get user by id
     */
    public List<UserBean> getUserById(String id){
        return queryUserBy(COL_TYPE_ID,id);
    }
    /**
     * get user by name
     */
    public List<UserBean> getUserByName(String user_name){
        return queryUserBy(Col_TYPE_NAME,user_name);
    }
    /**
     * select user by different type
     *
     * @param queryTye
     * @param param
     * @return
     */
    private List<UserBean> queryUserBy(int queryTye, String param) {
        List<UserBean> list = new ArrayList<>();
        if (queryTye == 0 && param == null || param.length() < 1) {
            return list;
        }
        String sql = "select * from" + USERTABLE_NAME;
        if (queryTye == COL_TYPE_ID) {
            sql += "where ID=" + param;
        }
        else if (queryTye==Col_TYPE_NAME){
            sql +="where NAME"+param;
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            UserBean userBean=new UserBean();
            userBean.setUid(cursor.getString(0));
            userBean.setName(cursor.getString(1));
            userBean.setIntro(cursor.getString(2));
            list.add(userBean);
            cursor.close();
        }
        return list;
    }

    /**
     * Notice: checkout without student's id
     */
    private boolean isUserAvailabel(UserBean userBean) {
        if (userBean == null) {
            Log.d(TAG, "isUserAvailabel: userbean is null");
            return false;
        }
        if (userBean.getName() == null && userBean.getName().length() < 1) {
            Log.d(TAG, "isUserAvailabel: username is null");
            return false;
        }
        if (userBean.getIntro() == null && userBean.getIntro().length() < 1) {
            Log.d(TAG, "isUserAvailabel: userintro is null");
            return false;
        }
        return true;
    }
}
