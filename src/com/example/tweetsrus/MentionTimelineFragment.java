package com.example.tweetsrus;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.twitterapime.rest.Timeline;
import com.twitterapime.search.Query;
import com.twitterapime.search.SearchDeviceListener;

public class MentionTimelineFragment extends TimelineFragment{
	public final String name = "PEW";
	
	@Override
	public void tweetPull(Query q, SearchDeviceListener s, Timeline t){
		//t.startGetRetweetsByMe(q, s)
		//t.startGetDirectMessages(q, false, s);
		t.startGetMentions(q, s);
	}
	
	public void logz(){
		Log.e("HOLY", "FUCK");
	}
}
