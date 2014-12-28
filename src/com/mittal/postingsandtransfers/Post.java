package com.mittal.postingsandtransfers;

public class Post {
	private String mdate;
	private String morder;
	private String msubject;
	private String murl;
	private boolean mviewed = true;

	

	public void Post() {
		

	}
	
	public  void setDate(String date) {
		mdate = date;
	}
	public void setOrder(String order) {
		 morder = order;
	}
	public  void setSubject(String subject) {
		msubject =subject;
	}
	public  void setUrl(String url) {
		 murl =url;
	}
	
	public String getDate() {
		return mdate;
	}
	public String getOrder() {
		return morder;
	}
	public String getSubject() {
		return msubject;
	}
	public String getUrl() {
		return murl;
	}
	
	public boolean isViewed() {
		return mviewed;
	}

	public void setViewed(boolean mviewed) {
		this.mviewed = mviewed;
	}

}
