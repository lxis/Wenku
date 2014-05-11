

package com.baidu.wenku.ui.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.wenku.domain.Article;
import com.baidu.wenku.ui.reader.ReaderActivity;
import com.example.wenku.R;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class SearchResultAdapter extends BaseAdapter {

	ArrayList<Article> data;
	Context context;
	
	public SearchResultAdapter(ArrayList<Article>  data,Context context)
	{
		this.data = data;
		this.context =context; 
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private Drawable GridItemBackgroud;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listitem_search_result, null);
                //convertView.setTag(data);
            } else {
                //holder = ()convertView.getTag();
            }
            final TextView textViewTitle = (TextView)convertView.findViewById(R.id.textViewTitle);
            TextView textViewSize = (TextView)convertView.findViewById(R.id.textViewSize);
            textViewTitle.setText(Html.fromHtml(data.get(position).Info.Title.replace("<em>", "<font color='red'>").replace("</em>", "</font>")));
            textViewSize.setText(String.valueOf(data.get(position).Info.DownloadCount)+"ÈËÏÂÔØ");
            final int positionFinal = position; 
            
            final LinearLayout linearLayoutSearchItem = (LinearLayout)convertView.findViewById(R.id.linearLayoutSearchItem);
            GridItemBackgroud = linearLayoutSearchItem.getBackground();
            linearLayoutSearchItem.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					int event =	arg1.getAction();
					if(event == MotionEvent.ACTION_DOWN)
					{
						linearLayoutSearchItem.setBackgroundColor(Color.rgb(220, 220, 220));						
					}
					else if(event == MotionEvent.ACTION_CANCEL)
					{
						linearLayoutSearchItem.setBackground(GridItemBackgroud);						
					}
					return false;
				}
			});
            linearLayoutSearchItem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					linearLayoutSearchItem.setBackground(GridItemBackgroud);	
					Intent intent = new Intent(context,ReaderActivity.class);
					String articleJson = new Gson().toJson(data.get(positionFinal));
					intent.putExtra("article", articleJson);
					context.startActivity(intent);
					
				}
			});
            return convertView;
        }

	private String GetSizeText(long size) {
		double sizeDouble = (double)size;
		double mb = sizeDouble/1024/1024;
		if(mb>=1)
			return GetDiget( mb,2)+"MB";
		double kb = sizeDouble/1024;
		if(kb>=1)
			return GetDiget(kb,2)+"KB";
		return String.valueOf(size);
	}
	
	private double GetDiget(double original,int size)
	{
	BigDecimal b = new BigDecimal(original);
	double c = b.setScale(size , BigDecimal.ROUND_HALF_UP).doubleValue();
	return c;
	}



}
