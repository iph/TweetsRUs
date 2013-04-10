package com.example.tweetsrus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.util.Linkify;
import android.view.Menu;
import android.widget.TextView;

public class DetailedTweet extends Activity {

	@Override
	protected void onCreate(Bundle saveState) {
		super.onCreate(saveState);
		setContentView(R.layout.detailed_tweet);
		Intent b = getIntent();
		String username = b.getStringExtra("username");
		int tweetCount = Integer.parseInt(b.getStringExtra("tweet_count"));
		int followerCount = Integer.parseInt(b.getStringExtra("follower_count"));
		int friendsCount = Integer.parseInt(b.getStringExtra("friends_count"));
		String content = b.getStringExtra("content");
		String time = b.getStringExtra("timestamp");
		String name = b.getStringExtra("name");
		
		TextView userText = (TextView)this.findViewById(R.id.username2);
		TextView nameText = (TextView)this.findViewById(R.id.textView2);
		TextView tweetText = (TextView)this.findViewById(R.id.tweet_count);
		TextView followerText = (TextView)this.findViewById(R.id.follower_count);
		TextView friendsText = (TextView)this.findViewById(R.id.following_count);
		TextView contentText = (TextView)this.findViewById(R.id.textView1);
		TextView timeText = (TextView)this.findViewById(R.id.datetime01);
		
		userText.setText(username);
		tweetText.setText("" + tweetCount + " Tweets");
		followerText.setText("" + followerCount + " Followers");
		friendsText.setText("" + friendsCount + " Following");
		contentText.setText(content);
		timeText.setText(time);
		nameText.setText("(@" + name + ")");
		
		Linkify.addLinks(contentText, Linkify.ALL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.herpaderp, menu);
		return true;
	}

}
