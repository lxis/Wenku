package com.baidu.wenku.ui.reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.baidu.wenku.domain.Article;
import com.baidu.wenku.domain.ArticlePage;
import com.baidu.wenku.domain.PicParag;
import com.baidu.wenku.domain.TextParag;
import com.baidu.wenku.server.service.ArticleService;
import com.baidu.wenku.server.service.SearchService;
import com.baidu.wenku.ui.search.SearchActivity;
import com.baidu.wenku.ui.search.SearchResultAdapter;
import com.example.wenku.R;
import com.google.gson.Gson;

import android.R.array;
import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ReaderActivity extends Activity {

	Article article;
	
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	requestWindowFeature(Window.FEATURE_NO_TITLE);

    String articleJson =getIntent().getStringExtra("article");
    article = new Gson().fromJson(articleJson, Article.class);


	setContentView(R.layout.activity_reader);
	
	final ProgressDialog dialog = new ProgressDialog(this);
	
	AsyncTask<ArticleParam, Integer, String> at = new AsyncTask<ArticleParam, Integer, String>(){
		@Override
		protected String doInBackground(ArticleParam... params) 
		{
				LoadArticlePage(params);
				return "";
		}
		
		protected void onPostExecute(String result) 
		{
			ShowArticlePage(dialog);
		}		
	};
	
	
	SetControlEvent();
	
	dialog.setMessage("«Î…‘∫Û...");
	dialog.show();
	
	

	ArticleParam param = new ArticleParam();
	param.id = article.Info.DocId;
	param.needInfo = true;
	param.pageNumber = 1;
	at.execute(param);	
	
	Thread thread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			while(true)
			{
				
			}
			
			
		}
	});
	thread.start();
	
	final ScrollView scrollView = (ScrollView)findViewById(R.id.readerScroll);
	scrollView.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent arg1) {
			
			if(arg1.getAction()== MotionEvent.ACTION_MOVE)
			{
                if (scrollView.getChildAt(0).getMeasuredHeight() <= scrollView.getHeight() + scrollView.getScrollY() + 2000)
                {
                		LoadMore();	
                }
			}
			return false;
		}
	
	});
}
	
	private void SetControlEvent() {

		
	Button button =	(Button)findViewById(R.id.buttonLoadMore);
	button.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			LoadMore();	
		}

		
	});
		
	}
	
	private void LoadArticlePage(ArticleParam... params) {
		Article returnArticle = new ArticleService().GetArticleContent(params[0].id ,params[0].needInfo,params[0].pageNumber,1);
		if(returnArticle.Info != null)				
			article.Info = returnArticle.Info;		
		article.Pages.add(returnArticle.Pages.get(0));
	}
	
	private void ShowArticlePage(final ProgressDialog dialog) {
		ArticlePage page =	article.Pages.get(article.Pages.size()-1);
			for(Object parag: page.Parags)
			{
				LinearLayout mainLayout = (LinearLayout)findViewById(R.id.rootGrid);
				if(parag instanceof TextParag)					
					AddText((TextParag)parag, mainLayout);
				else						
					AddPic((PicParag)parag, mainLayout);											
			}
		SetTitle();

		dialog.cancel();
		
	}

	private void SetTitle() {
		TextView textTitle = (TextView)findViewById(R.id.textViewArticleTitle);
		String title = article.Info.Title;
		if(title.length()>13)
			title = title.substring(0,12)+"...";
		
		textTitle.setText(title+"("+article.Pages.size()+"/"+article.Info.PageCount+")");
	}
	
	private int loadedPageCount = 1;
	
	private void LoadMore() 
	{
		if(article.Pages.size() == article.Info.PageCount)
			return;
		if(loadedPageCount != article.Pages.size())
			return;
		loadedPageCount = article.Pages.size()+1;
		
		final ProgressDialog dialog = new ProgressDialog(ReaderActivity.this);
		//dialog.setMessage("«Î…‘∫Û...");
		//dialog.show();
		final AsyncTask<ArticleParam, Integer, String> at = new AsyncTask<ArticleParam, Integer, String>(){
			@Override
			protected String doInBackground(ArticleParam... params) 
			{
					LoadArticlePage(params);
					return "";
			}
			
			protected void onPostExecute(String result) 
			{
				ShowArticlePage(dialog);
			}		
		};
		
		ArticleParam param = new ArticleParam();
		param.id = article.Info.DocId;
		param.needInfo = true;
		param.pageNumber = article.Pages.size() + 1;
		at.execute(param);
	}
	
	private void AddPic(PicParag parag,LinearLayout mainLayout) {
		try
		{
			final ImageView imageView = (ImageView)LayoutInflater.from(ReaderActivity.this).inflate(R.layout.fragment_reader_pic_parag, null);
			imageView.setImageResource(R.drawable.img_waiting);
			mainLayout.addView(imageView,mainLayout.getChildCount()-1);
			ArticlePic picParam = new ArticlePic();
			picParam.pic = parag;
			picParam.id = parag.Page.Article.Info.DocId;
			picParam.pn = parag.Page.PageIndex;
			
			new AsyncTask<ArticlePic, Integer, Bitmap>(){@Override
			protected Bitmap doInBackground(ArticlePic... params) {
				// TODO Auto-generated method stub
				try
				{
				return new ArticleService().GetArticlePic(params[0].id,params[0].pic,params[0].pn);
				}
				catch(Exception e)
				{
					int i = 0;
					int ii = i;
				}
				return null;
			}
			@Override
				protected void onPostExecute(Bitmap result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					imageView.setImageBitmap(result);		
				}
			}.execute(picParam);
		}
		catch(Exception e)
		{
			int i = 0;
			int ii = i;
		}
	}

	private void AddText(TextParag parag, LinearLayout mainLayout) {
		String text = parag.Content;	
		TextView searchResult = (TextView)LayoutInflater.from(ReaderActivity.this).inflate(R.layout.fragment_reader_text_parag, null);
		searchResult.setText(text);
		
		mainLayout.addView(searchResult,mainLayout.getChildCount()-1);
	}

	private class ArticleParam
	{
		public String id;
		public Boolean needInfo;
		public int pageNumber;
	}
	
	private class ArticlePic
	{
		public String id;
		public int pn;
		public PicParag pic = new PicParag();
	}
	

}
