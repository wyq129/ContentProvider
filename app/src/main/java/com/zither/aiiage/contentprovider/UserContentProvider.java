package com.zither.aiiage.contentprovider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.jetbrains.annotations.Nullable;


/**
 * @author wangyanqin
 * @date 2018/08/04
 */
@SuppressLint("Registered")
public class UserContentProvider extends ContentProvider {
    private static final String TAG = "UserContentProvider";
    private static UriMatcher matcher;
    private DatebaseHelper mHelper;
    /**
     * The same to the manifest:android:authorities="com.zither.aiiage.contentprovider.user"
     */
    private static final String AUTHORITY = "com.zither.aiiage.contentprovider.user";
    private static final String TABLE_USER_NAME = DatebaseHelper.USERTABLE_NAME;
    private static final int USER_INSERT_CODE = 1000;
    private static final int USER_DELETE_CODE = 1001;
    private static final int USER_UPDATE_CODE = 1002;
    private static final int USER_QUERY_ALL_CODE = 1003;
    private static final int USER_QUERY_ONE_CODE = 1004;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "user/insert", USER_INSERT_CODE);
        matcher.addURI(AUTHORITY, "user/delete", USER_DELETE_CODE);
        matcher.addURI(AUTHORITY, "user/update", USER_UPDATE_CODE);
        matcher.addURI(AUTHORITY, "user", USER_QUERY_ALL_CODE);
        matcher.addURI(AUTHORITY, "user/#", USER_QUERY_ONE_CODE);
    }

    @Override
    public boolean onCreate() {
        mHelper = new DatebaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.e(TAG, "query: uri =" + uri);
        int matchcode = matcher.match(uri);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        switch (matchcode) {
            case USER_QUERY_ALL_CODE:
                return db.query(TABLE_USER_NAME, strings, s, strings1, null, null, s1);
            case USER_QUERY_ONE_CODE:
                long parseId = ContentUris.parseId(uri);
                return db.query(TABLE_USER_NAME, strings, "ID=?", new String[]{parseId + ""}, null, null, s1);
            default:
                throw new IllegalArgumentException("Uri don't match: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, @Nullable ContentValues contentValues) {
        Log.d(TAG, "insert: uri is "+uri);
        SQLiteDatabase db=mHelper.getWritableDatabase();
        long id=db.insert(TABLE_USER_NAME,null, contentValues);
        db.close();
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "delete: uri is"+uri);
        SQLiteDatabase db=mHelper.getWritableDatabase();
        int count=db.delete(TABLE_USER_NAME,s,strings);
        db.close();
        return count;
    }

    @Override
    public int update(Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db=mHelper.getWritableDatabase();
        int count=db.update(TABLE_USER_NAME,contentValues,s,strings);
        db.close();
        return count;
    }
}
