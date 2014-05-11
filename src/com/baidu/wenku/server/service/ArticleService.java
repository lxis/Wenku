package com.baidu.wenku.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextPaint;

import com.baidu.wenku.common.MD5Helper;
import com.baidu.wenku.domain.Article;
import com.baidu.wenku.domain.ArticleInfo;
import com.baidu.wenku.domain.ArticlePage;
import com.baidu.wenku.domain.PicParag;
import com.baidu.wenku.domain.TextParag;
import com.baidu.wenku.server.common.RestClient;
import com.baidu.wenku.server.common.RestRequest;
import com.baidu.wenku.server.dto.article.ArticleJsonResponseDTO;
import com.baidu.wenku.server.dto.article.DocInfoDTO;
import com.baidu.wenku.server.dto.article.ParagDTO;
import com.baidu.wenku.server.dto.article.PhysicalPageDTO;
import com.baidu.wenku.server.dto.article.PicDTO;
import com.baidu.wenku.server.dto.search.SearchJsonResponseDTO;
import com.google.gson.Gson;

public class ArticleService {

	RestClient restClient = new RestClient();
	public Article GetArticleContent(String docId, Boolean needInfo, int pn, int rn)
	{	

		RestRequest request = GenerateRequest(docId,needInfo,pn,rn);
		ArticleJsonResponseDTO jsonObject = restClient.GetDtoFromServer(request, ArticleJsonResponseDTO.class);
		if(jsonObject.data.content!=null)
		{
		for(PhysicalPageDTO physicalPage : jsonObject.data.content)
		{
			for(ParagDTO parag : physicalPage.parags)
			{
				if(parag.t.equals("txt"))
				{
					parag.c = parag.c.toString();
					int i =0;
					int ii = i;
				}
				else if(parag.t.equals("pic"))
				{
					parag.c = new Gson().fromJson(parag.c.toString(),PicDTO.class);
					int i =0;
					int ii = i;
				}				
				else
				{
					//wrong
				}
				
			}
		}
		}
		
		return ConvertDtoToDomain(jsonObject);
	}
	
	public Bitmap GetArticlePic(String id,PicParag pic,int pn)
	{
		int rn = 1;
		String md5Sing = MD5Encode(id,pn,rn);
		String picUrl = "http://appwk.baidu.com/naapi/doc/view?type=1&rt=Retype&doc_id="+id+"&pn="+pn+"&rn=1&na_uncheck=1&ix="+pic.IX+"&iy="+pic.IY+"&iw="+pic.IW+"&ih="+pic.IH+"&aimw="+pic.IW+"&o="+pic.o+"&sign="+md5Sing;
		RestRequest request = new RestRequest();
		request.Url = picUrl;
		InputStream is = restClient.GetStreamFromServer(request);
		Bitmap bitmap = BitmapFactory.decodeStream(is); 
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;		
	}
	
    private Article ConvertDtoToDomain(ArticleJsonResponseDTO jsonObject) {
    	Article article = new Article();
    	DocInfoDTO docInfo = jsonObject.data.doc_info;
    	if(docInfo!=null)
    	{
    	article.Info = new ArticleInfo();
    	article.Info.DocId = docInfo.doc_id;
    	article.Info.ExtName = docInfo.ext_name;
    	article.Info.Size = Long.valueOf(docInfo.size);
    	article.Info.Title = docInfo.title;
    	article.Info.PageCount = Integer.parseInt(docInfo.page);
    	}
    	for(PhysicalPageDTO pageDTO : jsonObject.data.content)
    	{
    		ArticlePage articlePage = new ArticlePage();
    		articlePage.PageIndex = pageDTO.page;
    		for(ParagDTO parag : pageDTO.parags)
    		{
    			if(parag.t.equals("pic"))
    			{
    				PicParag pic = new PicParag();
    				pic.o = parag.o;
    				PicDTO picDTO = (PicDTO)parag.c;
    				pic.IH = picDTO.ih;
    				pic.IW = picDTO.iw;
    				pic.IX = picDTO.ix;
    				pic.IY = picDTO.iy;    				
    				pic.Page = articlePage;
    				articlePage.Parags.add(pic);
    			}
    			else
    			{
    				TextParag text = new TextParag();
    				text.Content = parag.c.toString();
    				text.Page = articlePage;
    				articlePage.Parags.add(text);
    			}
    		}
    		articlePage.Article = article;
    		article.Pages.add(articlePage);
    	}
		return article;
	}

	private RestRequest GenerateRequest(String docId, Boolean needInfo, int pn, int rn) {

		String url = "http://appwk.baidu.com/naapi/doc/view";	
    	RestRequest request = new RestRequest();
    	request.Url = url;
    	request.GetParamDic.add(new BasicNameValuePair("type","0"));
    	request.GetParamDic.add(new BasicNameValuePair("rt","Retype"));
    	request.GetParamDic.add(new BasicNameValuePair("doc_id",docId));
    	request.GetParamDic.add(new BasicNameValuePair("pn",String.valueOf(pn)));
    	request.GetParamDic.add(new BasicNameValuePair("rn",String.valueOf(rn)));
    	request.GetParamDic.add(new BasicNameValuePair("na_uncheck","1"));
    	request.GetParamDic.add(new BasicNameValuePair("needInfo",needInfo?"1":"0"));
		String md5Sing = MD5Encode(docId,pn,rn);
    	request.GetParamDic.add(new BasicNameValuePair("sign",md5Sing));
    	request.GetParamDic.add(new BasicNameValuePair("fr","8"));
    	request.GetParamDic.add(new BasicNameValuePair("pid","1"));
    	request.GetParamDic.add(new BasicNameValuePair("bid","1"));    	
    	return request;
	}
    
    private String MD5Encode(String docId, int pn, int rn)
    {
        String str = ReverseString(docId) + "_" + pn + "_" + rn + "_rwdk70aqPu";
        return MD5Helper.MD5(str);
    }

    private String ReverseString(String docId)
    {
        String newStr = "";
        for(int i = docId.length()-1;i>=0;i--)        
        	newStr += docId.charAt(i);        
        return newStr;
    }
    

}
