package com.mittal.postingsandtransfers;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.util.Log;



public class PostingDataSource {

	private SQLiteDatabase mDatabase; // The actual DB!
	private PostingHelper mPostingHelper; // Helper class for creating and
											// opening the DB
	private Context mContext;

	public PostingDataSource(Context context) {
		mContext = context;
		mPostingHelper = new PostingHelper(mContext);
	}

	/*
	 * Open the db. Will create if it doesn't exist
	 */
	public void open() throws SQLException {
		mDatabase = mPostingHelper.getWritableDatabase();
	}

	/*
	 * We always need to close our db connections
	 */
	public void close() {
		mDatabase.close();
	}

	// INSERT
	public void insert(Post post) {
		
		Cursor cursor = isPostNew(post);
		if (cursor.getCount() == 0) {
			Log.d("DATA SOURCE", "INSERTING NEW ROW...");

			sendNotification(post);
			
			mDatabase.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				values.put(PostingHelper.COLUMN_DATE, post.getDate());
				values.put(PostingHelper.COLUMN_ORDER, post.getOrder());
				values.put(PostingHelper.COLUMN_SUBJECT, post.getSubject());
				values.put(PostingHelper.COLUMN_URL, post.getUrl());
				post.setViewed(false);
				mDatabase.insert(PostingHelper.TABLE_POSTINGS, null, values);
				mDatabase.setTransactionSuccessful();
				Log.d("INSERTED" , "ROW ADDED");
			} finally {
				mDatabase.endTransaction();
			}
		}
		else{
			
			Log.d(" NOT INSERTED" , "ROW NOT ADDED");
		}

	}
	
public void insertByMain(Post post) {
		
		Cursor cursor = isPostNew(post);
		if (cursor.getCount() == 0) {
			Log.d("DATA SOURCE", "INSERTING NEW ROW...");

			sendNotification(post);
			
			mDatabase.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				values.put(PostingHelper.COLUMN_DATE, post.getDate());
				values.put(PostingHelper.COLUMN_ORDER, post.getOrder());
				values.put(PostingHelper.COLUMN_SUBJECT, post.getSubject());
				values.put(PostingHelper.COLUMN_URL, post.getUrl());
				post.setViewed(false);
				mDatabase.insert(PostingHelper.TABLE_POSTINGS, null, values);
				mDatabase.setTransactionSuccessful();
				Log.d("INSERTED" , "ROW ADDED");
			} finally {
				mDatabase.endTransaction();
			}
		}
		else{
			
			Log.d(" NOT INSERTED" , "ROW NOT ADDED");
		}

	}

	private void sendNotification(Post post) {
		Intent resultIntent = new Intent(mContext, MainActivity.class);
		// Because clicking the notification opens a new ("special") activity,
		// there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
		.setSmallIcon(R.drawable.pspcl_logo)
		.setContentTitle("New Update")
		.setContentText(post.getDate());


		mBuilder.setContentIntent(resultPendingIntent);
		// Sets an ID for the notification
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = 
		        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	}

	private Cursor isPostNew(Post post) {
		String whereClause = PostingHelper.COLUMN_ORDER + " = ?";

		Cursor cursor = mDatabase.query(PostingHelper.TABLE_POSTINGS, // table
				new String[] { PostingHelper.COLUMN_ORDER }, // column names
				whereClause, // where clause
				new String[] { post.getOrder() }, // where params
				null, // groupby
				null, // having
				null // orderby
				);
		return cursor;
	}
	
	 public void deleteAll() {
	        mDatabase.delete(
	            PostingHelper.TABLE_POSTINGS, // table
	            null, // where clause
	            null  // where params
	        );
	    }
	 
	 public ArrayList<Post> selectAll() {
		 ArrayList<Post> mPosts = new ArrayList<Post>();
		 
		 Cursor cursor = mDatabase.query(
	                PostingHelper.TABLE_POSTINGS, // table
	                new String[] { PostingHelper.COLUMN_DATE, PostingHelper.COLUMN_ORDER, PostingHelper.COLUMN_SUBJECT, PostingHelper.COLUMN_URL }, // column names
	                null, // where clause
	                null, // where params
	                null, // groupby
	                null, // having
	                null  // orderby
	        );
		 mPosts.clear();
		 
		 if (cursor.getCount() == 0) {
			 return null;
		 }
		 else {
			 cursor.moveToFirst();
		        while( !cursor.isAfterLast() ) {
		            // do stuff
		        	Post post = new Post();
		            int i = cursor.getColumnIndex(PostingHelper.COLUMN_DATE);
		            post.setDate( cursor.getString(i));
		            i = cursor.getColumnIndex(PostingHelper.COLUMN_ORDER);
		            post.setOrder( cursor.getString(i));
		            i = cursor.getColumnIndex(PostingHelper.COLUMN_SUBJECT);
		            post.setSubject( cursor.getString(i));
		            i = cursor.getColumnIndex(PostingHelper.COLUMN_URL);
		            post.setUrl( cursor.getString(i));
		            mPosts.add(post);
		            cursor.moveToNext();
		        }
		        return mPosts;
		 }

	        

	        
	 }

}
