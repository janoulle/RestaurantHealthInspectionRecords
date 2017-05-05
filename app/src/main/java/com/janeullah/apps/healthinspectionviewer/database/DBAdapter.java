package com.janeullah.apps.healthinspectionviewer.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Jane Ullah
 * @date 4/22/2017.
 */

public class DBAdapter {
    protected static final String TAG = "DBAdapter";

    private DBHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Context mContext;

    public DBAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
    }


    public DBAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.getMessage() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.getMessage());
            throw mSQLException;
        }
        return this;
    }

    public SQLiteDatabase getReadableDatabase() {
        createDatabase();
        open();
        return mDb;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor getTestData(String sqlQuery) {
        try {
            Cursor mCur = mDb.rawQuery(sqlQuery, null);
            if (mCur != null && mCur.moveToFirst()) {
                    do {
                        Cursor c = mCur;
                        String[] sample = c.getColumnNames();
                        Log.v(TAG,"Column names : " + Arrays.toString(sample));
                    } while (mCur.moveToNext());
                }


            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.getMessage());
            throw mSQLException;
        }
    }
}
