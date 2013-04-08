package com.example.tweetsrus;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.twitterapime.rest.Timeline;
import com.twitterapime.search.Query;
import com.twitterapime.search.SearchDeviceListener;

public class MentionTimelineFragment extends TimelineFragment{
	@Override
	public void tweetPull(Query q, SearchDeviceListener s, Timeline t){
		//t.startGetRetweetsByMe(q, s)
		t.startGetMentions(q, s);
	}
}
