package com.mittal.postingsandtransfers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context ctxt, Intent intent) {
		 if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
             // Set the alarm here.
    		 Log.d("boot","set");
    		Toast.makeText(ctxt, "Posting and Transfer will update after every two hours", Toast.LENGTH_SHORT).show();
    		 setAlarm(60*1000,ctxt);
         }
		
	}
	
	 private void setAlarm(long trig,Context ctxt) {
	    	
			Intent alarmIntent = new Intent(ctxt, AlarmReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(ctxt, 0,
					alarmIntent, 0);
			AlarmManager manager = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
			manager.setRepeating(AlarmManager.ELAPSED_REALTIME,
					SystemClock.elapsedRealtime() + trig, 2*60*60*1000, pendingIntent);
			// Toast.makeText(MainActivity.this, "Alarm Set",
			// Toast.LENGTH_SHORT).show();
		}

}
