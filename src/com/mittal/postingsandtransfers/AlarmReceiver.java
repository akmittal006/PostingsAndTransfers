package com.mittal.postingsandtransfers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
    	mContext = context;

        // For our recurring task, we'll just display a message
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
      // Log.d("it ","works");
    	
        	 Intent myIntent = new Intent(context, PullPostService.class);
             context.startService(myIntent);
         
       
    }
   
}