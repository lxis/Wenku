package com.baidu.wenku.ui.main;

import com.baidu.wenku.ui.search.SearchActivity;
import com.example.wenku.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends Activity {	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);
        GridLayout mainLayout = (GridLayout)findViewById(R.id.mainLayout);
        setTitle("百度文库");

        AddOnlineWenkuLayout(mainLayout);              
        AddSwitchButtonLayout(mainLayout);                           
    }

	private void AddSwitchButtonLayout(GridLayout mainLayout) {
		View switchButtonView = LayoutInflater.from(this).inflate(R.layout.fragment_switch_button, null);
        GridLayout.Spec row2 = GridLayout.spec(0);
        GridLayout.Spec column2 = GridLayout.spec(0);
        GridLayout.LayoutParams params2 = new GridLayout.LayoutParams(row2, column2);
        mainLayout.addView(switchButtonView,params2);
        
        TextView searchTextView = (TextView)switchButtonView.findViewById(R.id.onlineWenkuButton);
        searchTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,SearchActivity.class); 
				startActivity(intent);
			}
		});
	}

	private void AddOnlineWenkuLayout(GridLayout mainLayout) {
		View onlineWenkuView = LayoutInflater.from(this).inflate(R.layout.fragment_onlinewenku,null);
        GridLayout.Spec row = GridLayout.spec(1);
        GridLayout.Spec column = GridLayout.spec(0);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, column);
        params.setGravity(80);
        mainLayout.addView(onlineWenkuView,params);
	}
  
}
