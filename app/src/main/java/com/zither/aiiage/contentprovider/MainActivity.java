package com.zither.aiiage.contentprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * @author wangyanqin
 * @date 2018/08/04
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG="MainActivity";
    private Uri mUri=Uri.parse("content://com.zither.aiiage.contentprovider.user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_insert).setOnClickListener(this);
        findViewById(R.id.button_query).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_insert:
                ContentValues contentValues=new ContentValues();
                contentValues.put(DatebaseHelper.user_name,"寄生不忧伤");
                contentValues.put(DatebaseHelper.user_intro,"爱笑的女生运气都不会太差");
                Log.d(TAG, "onClick: "+contentValues);
                getContentResolver().insert(mUri,contentValues);
                break;
            case R.id.button_query:
                Cursor cursor=managedQuery(Uri.parse("content://com.zither.aiiage.contentprovider.user/user"),null,null,null,null);
                while (cursor.moveToNext()){
                    Log.i(TAG, "onClick: " + cursor.getString(0) + ", "
                            + cursor.getString(1) + ", "
                            + cursor.getString(2));
                }
                break;
                default:
                    break;
        }
    }
}
