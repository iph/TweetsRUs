package com.example.tweetsrus;

import java.io.IOException;
import java.util.Hashtable;

import com.twitterapime.rest.Credential;
import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Tweet;
import com.twitterapime.xauth.Token;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PostTweet extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_layout);
		final TextView count = (TextView) this.findViewById(R.id.count);
		final EditText edit = (EditText) this.findViewById(R.id.tweet_text);
		final Button postTweet = (Button) this.findViewById(R.id.post_tweet);
		postTweet.setEnabled(false);
		postTweet.setText("Write something...");

		postTweet.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Token token = new Token(TweetHome.TOKEN_ACCESS, TweetHome.TOKEN_SECRET);
				Credential c = new Credential("StylerMyers", TweetHome.CONSUMER_KEY, TweetHome.CONSUMER_SECRET, token);
				UserAccountManager manager = UserAccountManager.getInstance(c);
				 
				try {
					if (manager.verifyCredential()) {
						String text = edit.getText().toString();
						final Tweet t = new Tweet(text);
						final TweetER ter = TweetER.getInstance(manager);
						Runnable run = new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									ter.post(t);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (LimitExceededException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						};
						Thread herp = new Thread(run);
						herp.start();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LimitExceededException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			
		});
		
		edit.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
					count.setText("" + (140 - arg0.length()));
					if(arg0.length() > 0 && arg0.length() <= 140){
						postTweet.setText("Post Tweet");
						postTweet.setEnabled(true);
					}
					else if(arg0.length() == 0){
						postTweet.setEnabled(false);
						postTweet.setText("Write something...");
					}
					else{
						postTweet.setText("Too many characters..");
						postTweet.setEnabled(false);
					}
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_post_tweet, menu);
		return true;
	}

}
