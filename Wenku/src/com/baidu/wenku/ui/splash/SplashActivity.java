package com.baidu.wenku.ui.splash;

import com.baidu.wenku.ui.reader.ReaderActivity;
import com.baidu.wenku.ui.search.SearchActivity;
import com.example.wenku.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(new Intent(SplashActivity.this,SearchActivity.class));				
				
			}
		});
		thread.start();
		
		
		
	}
}
