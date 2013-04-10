package com.example.tweetsrus;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.twitterapime.rest.Credential;
import com.twitterapime.rest.Timeline;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;
import com.twitterapime.search.TweetEntity;
import com.twitterapime.xauth.Token;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineFragment extends Fragment{
	UserAccountManager manager;
	List<Tweet> tweets;
	List<Tweet> newTweets;
	public final String name = "FUCKED";
	private Handler handle;
	@Override
	public void onCreate(Bundle savedState){
		super.onCreate(savedState);
		tweets = new ArrayList<Tweet>();
		newTweets = new ArrayList<Tweet>();
		Token token = new Token(TweetHome.TOKEN_ACCESS, TweetHome.TOKEN_SECRET);
		Credential c = new Credential("StylerMyers", TweetHome.CONSUMER_KEY, TweetHome.CONSUMER_SECRET, token);
		manager = UserAccountManager.getInstance(c);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		View v = inflater.inflate(R.layout.tweets_layout, container, false);
		ListView lv = (ListView)v.findViewById(R.id.listView1);
		final ArrayAdapter<Tweet> tweetAdapter = new TweetAdapter(getActivity(), R.layout.tweet_row, tweets, inflater);
		
		lv.setAdapter(tweetAdapter);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Tweet tweet = tweets.get(position);
				
				String timestamp = tweet.getString("TWEET_PUBLISH_DATE");
				Date date = new Date(Long.parseLong(timestamp));
				SimpleDateFormat displayDate = new SimpleDateFormat("EEE, d, MMM");
				UserAccount acc = tweet.getUserAccount();
				String formattedDate = displayDate.format(date);
				String username = acc.getString("USERACCOUNT_NAME");
				String tweetCount = acc.getString("USERACCOUNT_TWEETS_COUNT");
				String followerCount = acc.getString("USERACCOUNT_FOLLOWERS_COUNT");
				String friendsCount = acc.getString("USERACCOUNT_FRIENDS_COUNT");
				String content = tweet.getString("TWEET_CONTENT");
				String name = acc.getString("USERACCOUNT_USER_NAME");
				
				//Log.e("WHAT", acc.toString());
				Intent detailedView = new Intent(getActivity(), DetailedTweet.class);

				detailedView.putExtra("username", username);
				detailedView.putExtra("tweet_count", tweetCount);
				detailedView.putExtra("follower_count", followerCount);
				detailedView.putExtra("friends_count", friendsCount);
				detailedView.putExtra("content", content);
				detailedView.putExtra("timestamp", formattedDate);
				detailedView.putExtra("name", name);
				
				getActivity().startActivity(detailedView);
				//getActivity().startActiv
			}
			
		});
	
		// Handler that allows for tweets to update on command.
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				Bundle bundle = msg.getData();
				if(bundle.getBoolean("new_tweet", false)){
					tweets.addAll(newTweets);
					newTweets.clear();
					tweetAdapter.notifyDataSetChanged();
				}
			}
		};
		refresh();
	
		
		// Setup for refresh button.
		ImageButton refresh = (ImageButton)v.findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View _) {
				refresh();
			}
		});
		
		ImageButton settings = (ImageButton)v.findViewById(R.id.settings);
		settings.setOnClickListener(new OnClickListener(){
			
			public void onClick(View _){
				Intent caller = new Intent(getActivity(), SettingsActivity.class);
				getActivity().startActivity(caller);
			}
		});
		
		ImageButton post = (ImageButton)v.findViewById(R.id.post);
		post.setOnClickListener(new OnClickListener(){
			public void onClick(View _){
				Intent caller = new Intent(getActivity(), PostTweet.class);
				getActivity().startActivity(caller);
			}
		});
		return v;
	}
	public void refresh(){
		// Pull the tweets here.
		tweets.clear();
		try {
			if(manager.verifyCredential()){
				Timeline timeline = Timeline.getInstance(manager);
				Query q = QueryComposer.count(20);
				SearchDeviceListener s = new SearchDeviceListener(){

					@Override
					public void searchCompleted() {
						Bundle b = new Bundle();
						b.putBoolean("new_tweet", true);
						Message m = new Message();
						m.setData(b);
						handle.sendMessage(m);
						Log.e(name, "DONE!");
					}

					@Override
					public void searchFailed(Throwable arg0) {
						Log.e(name, arg0.toString());
						logz();
					}

					@Override
					public void tweetFound(Tweet tweet) {
						newTweets.add(tweet);
					}
					
				};
				tweetPull(q, s, timeline);
				//timeline.startGetHomeTweets(q, s);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	public void logz(){
		Log.e("AHOY", "THERE");
	}
	
	public void tweetPull(Query q, SearchDeviceListener s, Timeline t){
		t.startGetHomeTweets(q, s);
	}
	
	
	
}
