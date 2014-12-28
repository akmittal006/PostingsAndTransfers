package com.mittal.postingsandtransfers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class PullPostService extends IntentService {
	PostingDataSource mDataSource;

	public PullPostService() {

		super("PullPostService");
		// TODO Auto-generated constructor stub
		mDataSource = new PostingDataSource(this);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		mDataSource.open();
		Log.d("SERVICE TRIGGERD", "Alaram working");

		try {
			Document doc = Jsoup.connect(
					"http://202.164.52.147/serv1/frmshow.aspx?ofc=services1")
					.get();
			Element table = doc.getElementById("GridView1");
			Elements rows = table.getElementsByTag("tr");

//			 Post testPost = new Post();
//			 testPost.setDate("26-dec-2014");
//			 testPost.setOrder("testo");
//			 testPost.setSubject("subject");
//			 testPost.setUrl("dcdf");
//			 mDataSource.insert(testPost);

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
						 Log.v("SERVICE","Date :" + date);

					} else if (i == 1) {
						Element cell = columns.get(i);
						String order = cell.text();
						order = order.trim();
						post.setOrder(order);
						 Log.v("SERVICE","Order :" + order);
					} else {
						Element cell = columns.get(i);
						Elements links = cell.getElementsByAttribute("href");
						for (Element link : links) {

							String subject = link.text();
							subject = subject.trim();
							 Log.v("SERVICE","Subject: " + subject);
							post.setSubject(subject);

							String url = link.attr("abs:href");
							post.setUrl(url);
							Log.v("SERVICE", "url :" + url);
						}
					}
					// System.out.println("\n");

				}
				//Log.v("Testing", "START");
				mDataSource.insert(post); 
				Log.d("round", "" + x);
				//Log.v("Testing", "STOP");

			}
		} catch (Exception e) {
			System.out.println("" + e);
			retry();

		}
		mDataSource.close();

	}
	
	public void retry() {
		try {
			Document doc = Jsoup.connect(
					"http://202.164.52.147/serv1/frmshow.aspx?ofc=services1")
					.get();
			Element table = doc.getElementById("GridView1");
			Elements rows = table.getElementsByTag("tr");

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
						 Log.v("SERVICE","Date :" + date);

					} else if (i == 1) {
						Element cell = columns.get(i);
						String order = cell.text();
						order = order.trim();
						post.setOrder(order);
						 Log.v("SERVICE","Order :" + order);
					} else {
						Element cell = columns.get(i);
						Elements links = cell.getElementsByAttribute("href");
						for (Element link : links) {

							String subject = link.text();
							subject = subject.trim();
							 Log.v("SERVICE","Subject: " + subject);
							post.setSubject(subject);

							String url = link.attr("abs:href");
							post.setUrl(url);
							Log.v("SERVICE", "url :" + url);
						}
					}
					// System.out.println("\n");

				}
				// mposts.add(post);
				Log.v("Testing", "START");
				mDataSource.insert(post); 
				Log.d("round", "" + x);
				Log.v("Testing", "STOP");

			}
		} catch (Exception e) {
			System.out.println("" + e);

		}
	}

}
