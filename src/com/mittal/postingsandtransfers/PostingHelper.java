package com.mittal.postingsandtransfers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostingHelper extends SQLiteOpenHelper {
	
	/*
     * Table and column information
     */
    public static final String TABLE_POSTINGS = "POSTINGANDTRANSFER";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_ORDER = "ORDER_N0";
    public static final String COLUMN_SUBJECT = "SUBJECT";
    public static final String COLUMN_URL = "URL";

    /*
     * Database information
     */
    private static final String DB_NAME = "new3.db";
    private static final int DB_VERSION = 1; // Must increment to trigger an upgrade
    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_POSTINGS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DATE + " TEXT,"+
                    COLUMN_ORDER+ " TEXT,"+
                    COLUMN_SUBJECT + " TEXT,"+
                    COLUMN_URL + " TEXT)";
    public PostingHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
