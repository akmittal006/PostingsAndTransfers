package com.mittal.postingsandtransfers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PostAdapter extends ArrayAdapter<Post> {
	ArrayList<Post> mposts;
	String date;
	String order;
	String subject;
	String url;
	Context mContext;

	public PostAdapter(Context context, List<Post> posts) {
		super(context, R.layout.post_item, posts);
		mContext = context;
		mposts = new ArrayList<Post>(posts);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.post_item, null);
			holder = new ViewHolder();
			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.orderTextView = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.subjectTextView = (TextView) convertView
					.findViewById(R.id.textView3);
			holder.newTextView = (TextView) convertView
					.findViewById(R.id.newTextView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Post post = mposts.get(position);

		holder.dateTextView.setText(post.getDate());
		//Log.d("ak","Date :" + post.getDate());

		holder.orderTextView.setText(post.getOrder());
		//Log.d("ak","Date :" + post.getOrder());
		holder.subjectTextView.setText(post.getSubject());
		//Log.d("ak","Date :" + post.getSubject());
		
		if (post.isViewed() ) {
			holder.newTextView.setVisibility(View.INVISIBLE);
			
		}else {
			holder.newTextView.setVisibility(View.VISIBLE);
			
		}

		return convertView;
	}

	private static class ViewHolder {
		TextView dateTextView;
		TextView orderTextView;
		TextView subjectTextView;
		TextView newTextView;
	}

	public void refill(List<Post> posts) {
		mposts.clear();
		mposts.addAll(posts);
		notifyDataSetChanged();
	}

}
