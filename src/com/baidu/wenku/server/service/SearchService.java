package com.baidu.wenku.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.accounts.NetworkErrorException;
import android.text.Editable;

import com.baidu.wenku.domain.Article;
import com.baidu.wenku.domain.ArticleInfo;
import com.baidu.wenku.server.common.RestClient;
import com.baidu.wenku.server.common.RestRequest;
import com.baidu.wenku.server.dto.search.SearchJsonResponseDTO;
import com.baidu.wenku.server.dto.search.SearchDataDTO;
import com.baidu.wenku.server.dto.search.SearchDocInfoDTO;
import com.google.gson.Gson;

public class SearchService {
	
	RestClient restClient = new RestClient();
	
	//对外的方法
	public ArrayList<Article> GetSearchResult(String searchKeywork,int pageIndex)
	{		
		RestRequest request = GenerateRequest(searchKeywork,pageIndex);
		SearchJsonResponseDTO jsonObject = restClient.GetDtoFromServer(request, SearchJsonResponseDTO.class);
		return ConvertDtoToDomain(jsonObject);
	}
	private RestRequest GenerateRequest(String searchKeywork,int pageIndex) {
		RestRequest request = new RestRequest();
		request.Url = "http://appwk.baidu.com/naapi/doc/search";
		request.GetParamDic.add(new BasicNameValuePair("query", searchKeywork));
		request.GetParamDic.add(new BasicNameValuePair("bid", "1"));
		request.GetParamDic.add(new BasicNameValuePair("pn", String.valueOf(pageIndex)));
		request.GetParamDic.add(new BasicNameValuePair("fr", "8"));
		request.GetParamDic.add(new BasicNameValuePair("pid", "1"));
		request.GetParamDic.add(new BasicNameValuePair("na_uncheck", "1"));
		return request;
	}
	
	//业务模型转换
	private ArrayList<Article> ConvertDtoToDomain(SearchJsonResponseDTO jsonObject			) {
		ArrayList<Article> resultList = new ArrayList<Article>();	
		ArrayList<SearchDocInfoDTO> docs =jsonObject.getData().getDoc_info();
		for(SearchDocInfoDTO doc : docs)
		{
			Article articleInfo1 = new Article();	
			articleInfo1.Info = new ArticleInfo();
			articleInfo1.Info.Title = doc.getTitle();
			articleInfo1.Info.DocId = doc.getDoc_id();
			articleInfo1.Info.Size = Long.valueOf(doc.getSize());
			articleInfo1.Info.DownloadCount = doc.getDownload_count();
			resultList.add(articleInfo1);
		}
		return resultList;
	}
}
