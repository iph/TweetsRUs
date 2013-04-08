package com.example.tweetsrus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.twitterapime.rest.Credential;
import com.twitterapime.rest.Timeline;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;
import com.twitterapime.search.TweetEntity;
import com.twitterapime.xauth.Token;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineFragment extends Fragment{
	UserAccountManager manager;
	List<Tweet> tweets;
	private Handler handle;
	@Override
	public void onCreate(Bundle savedState){
		super.onCreate(savedState);
		tweets = new ArrayList<Tweet>();
		Token token = new Token(TweetHome.TOKEN_ACCESS, TweetHome.TOKEN_SECRET);
		Credential c = new Credential("StylerMyers", TweetHome.CONSUMER_KEY, TweetHome.CONSUMER_SECRET, token);
		manager = UserAccountManager.getInstance(c);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		View v = inflater.inflate(R.layout.woah, container, false);
		ListView lv = new ListView(getActivity());
		final ArrayAdapter<Tweet> tweetAdapter = new TweetAdapter(getActivity(), R.layout.tweet_row, tweets, inflater);
		
		lv.setAdapter(tweetAdapter);

	
		// Handler that allows for tweets to update on command.
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				Bundle bundle = msg.getData();
				Tweet herp = new Tweet();
				if(bundle.getBoolean("new_tweet", false)){
					tweetAdapter.notifyDataSetChanged();
				}
			}
		};
		refresh();
	
		return lv;
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
					}

					@Override
					public void searchFailed(Throwable arg0) {
						Log.e("Fucked", arg0.toString());
					}

					@Override
					public void tweetFound(Tweet tweet) {
						tweets.add(tweet);
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
	
	public void tweetPull(Query q, SearchDeviceListener s, Timeline t){
		t.startGetHomeTweets(q, s);
	}
}
