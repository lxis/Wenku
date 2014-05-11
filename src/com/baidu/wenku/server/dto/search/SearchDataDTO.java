package com.baidu.wenku.server.dto.search;

import java.util.ArrayList;



public class SearchDataDTO {
private String total;

public String getTotal() {
	return total;
}

public void setTotal(String total) {
	this.total = total;
}

 
 



public ArrayList<SearchDocInfoDTO> getDoc_info() {
	return doc_info;
}

public void setDoc_info(ArrayList<SearchDocInfoDTO> doc_info) {
	this.doc_info = doc_info;
}






private ArrayList<SearchDocInfoDTO> doc_info;
}
