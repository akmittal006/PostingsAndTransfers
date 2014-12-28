package com.mittal.postingsandtransfers;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	ArrayList<Post> mposts;

	// String date;
	// String order;
	// String subject;
	// String url;

	ProgressBar mProgressBar;
	PostingDataSource mDataSource;
	private PendingIntent pendingIntent;
	AlarmManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDataSource = new PostingDataSource(this);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

		

		if (isNetworkAvailable()) {
			mProgressBar.setVisibility(View.VISIBLE);
			GetPostsTask getPostsTask = new GetPostsTask();
			// mDataSource.deleteAll();
			getPostsTask.execute();
		} else {
			mProgressBar.setVisibility(View.INVISIBLE);
			mDataSource.open();
			if(mDataSource.selectAll() == null) {
				TextView emptyView = (TextView) getListView().getEmptyView();
				emptyView.setText("No items to display!");
			}else {
				mposts = mDataSource.selectAll();
				PostAdapter adapter = new PostAdapter(this, mposts);
				setListAdapter(adapter);
			}
			mDataSource.close();
			
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		mDataSource.open();
	}

	@Override
	public void onPause() {
		super.onPause();
		mDataSource.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			mProgressBar.setVisibility(View.VISIBLE);
			if (isNetworkAvailable()) {
				mProgressBar.setVisibility(View.VISIBLE);
				GetPostsTask getPostsTask = new GetPostsTask();
				// mDataSource.deleteAll();
				getPostsTask.execute();
			} else {
				mProgressBar.setVisibility(View.INVISIBLE);
				mDataSource.open();
				if(mDataSource.selectAll() == null) {
					TextView emptyView = (TextView) getListView().getEmptyView();
					emptyView.setText("No items to display!");
				}else {
					mposts = mDataSource.selectAll();
					PostAdapter adapter = new PostAdapter(this, mposts);
					setListAdapter(adapter);
				}
				mDataSource.close();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String murl = mposts.get(position).getUrl();

		Uri uri = Uri.parse(murl);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(uri);
		startActivity(intent);
	}

	public class GetPostsTask extends AsyncTask<Object, Void, ArrayList<Post>> {

		@Override
		protected ArrayList<Post> doInBackground(Object... params) {
			
			mposts = new ArrayList<Post>();

			try {
				Document doc = Jsoup
						.connect(
								"http://202.164.52.147/serv1/frmshow.aspx?ofc=services1")
						.get();
				Element table = doc.getElementById("GridView1");
				Elements rows = table.getElementsByTag("tr");

				
				// Post testPost = new Post();
				// testPost.setDate("26-dec-2014");
				// testPost.setOrder("testorder2");
				// testPost.setSubject("subject");
				// testPost.setUrl("dcdf");
				// mDataSource.insert(testPost);

				int x;
				for (x = 1; x < 16; x++) {
					Post post = new Post();
					Element row = rows.get(x);
					Elements columns = row.children();
					int i;
					for (i = 0; i < 3; i++) {

						if (i == 0) {
							Element cell = columns.get(i);
							String date = cell.text();
							date = date.trim();
							post.setDate(date);
							// Log.d("ak","Date :" + date);

						} else if (i == 1) {
							Element cell = columns.get(i);
							String order = cell.text();
							order = order.trim();
							post.setOrder(order);
							// Log.d("ak","Order :" + order);
						} else {
							Element cell = columns.get(i);
							Elements links = cell
									.getElementsByAttribute("href");
							for (Element link : links) {

								String subject = link.text();
								subject = subject.trim();
								// Log.d("ak","Subject: " + subject);
								post.setSubject(subject);

								String url = link.attr("abs:href");
								post.setUrl(url);
								// Log.d("ak", "url :" + url);
							}
						}
						// System.out.println("\n");

					}

					// post.Post(date, order, subject, url);
					mposts.add(post);
					mDataSource.insertByMain(post);

				}
			} catch (Exception e) {
				System.out.println("" + e);
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						MainActivity.this);
//				builder.setTitle("Soryy papa!");
//				builder.setMessage("No connection available!");
//				builder.setPositiveButton(android.R.string.ok, null);
//				AlertDialog dialog = builder.create();
//				dialog.show();
//				TextView emptyView = (TextView) getListView().getEmptyView();
//				emptyView.setText("No items to display!");

			}
			Log.d("IIIMMMPPPP", "size :" + mposts.size());
			
			return mposts;
		}

		protected void onPostExecute(ArrayList<Post> gotposts) {
			long trig = 2*60*60*1000 ;
			setAlarm(trig);
			mProgressBar.setVisibility(View.INVISIBLE);
			mposts = gotposts;
			// Log.d("IIIMMMPPPP","Date :" + mposts.get(5).getDate());
			if (mposts.size() == 0) {
				mProgressBar.setVisibility(View.INVISIBLE);
				mDataSource.open();
				if(mDataSource.selectAll() == null) {
					TextView emptyView = (TextView) getListView().getEmptyView();
					emptyView.setText("No items to display!");
				}else {
					mposts = mDataSource.selectAll();
					PostAdapter adapter = new PostAdapter(MainActivity.this, mposts);
					setListAdapter(adapter);
				}
				mDataSource.close();
			} else {
				PostAdapter adapter = new PostAdapter(MainActivity.this, mposts);
				setListAdapter(adapter);
				

			}

		}

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	private void setAlarm(long trig) {
		Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
				alarmIntent, 0);
		manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + trig, trig, pendingIntent);
		// Toast.makeText(MainActivity.this, "Alarm Set",
		// Toast.LENGTH_SHORT).show();
	}
}
