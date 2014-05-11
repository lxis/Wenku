package com.baidu.wenku.ui.search;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.Inflater;

import com.baidu.wenku.domain.Article;
import com.baidu.wenku.server.service.SearchService;
import com.example.wenku.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.GridLayout.Spec;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SearchActivity extends Activity {

	LinearLayout mainLayout;
	SearchResultAdapter adapter ;
	
	ArrayList<Article> articles = new ArrayList<Article>();
	
	int pageIndex = 0;

	@Override
	public void onBackPressed() {


		CharSequence cs = "aa";
		new AlertDialog.Builder(this)
		.setPositiveButton("退出", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {				
	            Exit();  				
			}

			private void Exit() {
				Intent intent = new Intent(Intent.ACTION_MAIN);  
	            intent.addCategory(Intent.CATEGORY_HOME);  
	            startActivity(intent);  
	            System.exit(0);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();								
			}
		}).setMessage("退出百度文库?").create().show();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("百度文库");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.wenk_me_bar_search);
		setContentView(R.layout.activity_search);
		mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
		AddResultFragment(mainLayout);
		SetControlEvents();
	}
	
	private void SetSearchDefault()
	{
		final EditText searchEdit = (EditText)findViewById(R.id.searchEditText);
		searchEdit.setText("在百度文库中搜索");
		searchEdit.setTextColor(Color.GRAY);
	}
	
	private void SetSearchInput()
	{
		final EditText searchEdit = (EditText)findViewById(R.id.searchEditText);

		if(searchEdit.getText().toString().equals("在百度文库中搜索"))
		{
			searchEdit.setText("");
			searchEdit.setTextColor(Color.BLACK);
		}
	}

	private void SetControlEvents() {
		final EditText searchEdit = (EditText)findViewById(R.id.searchEditText);
		SetSearchDefault();		
		searchEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SetSearchInput();				
			}
		});
		Button buttonNext = (Button)findViewById(R.id.nextButton);
		buttonNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				LoadNext();
			}


		});
		
		Button buttonClear = (Button)findViewById(R.id.clearButton);
		buttonClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchEdit.setText("");		
				showSoftInputMethod();
			}
		});
		searchEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

				View searchResult = (View)findViewById(R.id.searchResultLayout);
				searchResult.setVisibility(8);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		searchEdit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				View searchResult = (View)findViewById(R.id.searchResultLayout);
				searchResult.setVisibility(8);
				if(keyCode == 66)
				{
					if(event.getAction() == KeyEvent.ACTION_UP)
					{
					pageIndex = 0;												
					collapseSoftInputMethod();										
					Search();
					}
					
					return true;
				}
/*				else if(((EditText)v).getText().toString() == "")
				{
					SetSearchDefault();
				}*/
				return false;
			}


		});
		
		View searchResult = (View)findViewById(R.id.searchResultLayout);
		final ListView listView = (ListView)searchResult.findViewById(R.id.searchResultlistView);
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int totalCount) {
				int lastPosition = listView.getLastVisiblePosition();
				if(totalCount <= lastPosition+1+20&&totalCount > pageIndex)		//现在最下的Item是第几个，		
					LoadNext();								
			}
		});
		
		
	}
	
	Lock lock = new ReentrantLock();
	
	private void LoadNext() {
		pageIndex+=20;	
		Search();
	}
	
	public void collapseSoftInputMethod(){
		 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		 EditText text = (EditText)findViewById(R.id.searchEditText);
		 imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
		}
	
	 public void showSoftInputMethod() {
	            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            EditText text = (EditText)findViewById(R.id.searchEditText);
	            imm.showSoftInput(text, 0);	        
	    }

	private void AddResultFragment(LinearLayout mainLayout) {
		// TODO Auto-generated method stub
		View searchResult = LayoutInflater.from(this).inflate(R.layout.fragment_search_result, null);
		searchResult.setVisibility(8);
		mainLayout.addView(searchResult);		
	}
	
	private void Search() {
		if(pageIndex == 0)
			articles.clear();
		View searchResult = (View)findViewById(R.id.searchResultLayout);
		final ListView listView = (ListView)searchResult.findViewById(R.id.searchResultlistView);
		EditText searchWorkEditText = (EditText)findViewById(R.id.searchEditText);
		
		searchResult.setVisibility(0);
		String searchKeyword = searchWorkEditText.getText().toString();
		
		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("请稍后...");
		
		
		AsyncTask<String, Integer, ArrayList<Article>> at = new AsyncTask<String, Integer, ArrayList<Article>>(){
			@Override
			protected ArrayList<Article> doInBackground(
					String... params) {

				ArrayList<Article> articles = new ArrayList<Article>();
				articles.addAll(new SearchService().GetSearchResult(params[0],pageIndex));
				articles.addAll(new SearchService().GetSearchResult(params[0],pageIndex+10));
				return articles;
			}
			
			protected void onPostExecute(java.util.ArrayList<Article> result) 
			{
				articles.addAll(result);
				if(pageIndex == 0)
					dialog.cancel();
				if(adapter == null)
				{
				adapter = new SearchResultAdapter(articles, SearchActivity.this);
				listView.setAdapter(adapter);
				}
				else
				{
					adapter.notifyDataSetChanged();
				}
				//((Button)findViewById(R.id.nextButton)).setVisibility(0);
			};
		};
		if(pageIndex == 0)
			dialog.show();
		 at.execute(searchKeyword);

	}
}
