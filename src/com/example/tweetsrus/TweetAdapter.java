package com.example.tweetsrus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<Tweet> {
	List<Tweet> tweets;
	final LayoutInflater inflate;
	
	public TweetAdapter(Context context, int resourceId, List<Tweet> _tweets, LayoutInflater infl){
		super(context, resourceId, _tweets);
		tweets = _tweets;
		inflate = infl;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Tweet tweet = tweets.get(position);
		
		// Get contents from the tweet to display.
		String timestamp = tweet.getString("TWEET_PUBLISH_DATE");
		Date date = new Date(Long.parseLong(timestamp));
		SimpleDateFormat displayDate = new SimpleDateFormat("EEE, d, MMM");
		UserAccount acc = tweet.getUserAccount();
		String username = acc.getString("USERACCOUNT_NAME");
		//String username = tweet.getString("USERACCOUNT_USER_NAME");
		String content = tweet.getString("TWEET_CONTENT");
		String formattedDate = displayDate.format(date);
		
		View row = inflate.inflate(R.layout.tweet_row, parent, false);
		TextView userText = (TextView)row.findViewById(R.id.username);
		TextView dateText = (TextView)row.findViewById(R.id.date);
		TextView contentText = (TextView)row.findViewById(R.id.content);
		
		userText.setText(username);
		dateText.setText(formattedDate);
		contentText.setText(content);
		
		return row;
	}
	
}
