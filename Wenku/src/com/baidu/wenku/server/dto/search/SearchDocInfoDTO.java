package com.baidu.wenku.server.dto.search;

public class SearchDocInfoDTO {
    private String doc_id;	 
    private String title;
    private String view_count;
    private String size;
    private String ext_name;
    private int mydoc;
    private int download_count;
    
 


	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getView_count() {
		return view_count;
	}



	public void setView_count(String view_count) {
		this.view_count = view_count;
	}



	public String getSize() {
		return size;
	}



	public void setSize(String size) {
		this.size = size;
	}



	public String getExt_name() {
		return ext_name;
	}



	public void setExt_name(String ext_name) {
		this.ext_name = ext_name;
	}



	public int getMydoc() {
		return mydoc;
	}



	public void setMydoc(int mydoc) {
		this.mydoc = mydoc;
	}



	public String getDoc_id() {
		return doc_id;
	}



	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}



	public int getDownload_count() {
		return download_count;
	}



	public void setDownload_count(int download_count) {
		this.download_count = download_count;
	}
}
