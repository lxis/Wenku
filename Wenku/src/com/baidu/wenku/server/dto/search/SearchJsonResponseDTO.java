package com.baidu.wenku.server.dto.search;




public class SearchJsonResponseDTO {
	private StatusDTO status;
	private SearchDataDTO data;
	
	public StatusDTO getStatus() {
		return status;
	}

	public void setStatus(StatusDTO status) {
		this.status = status;
	}
	
	public SearchDataDTO getData() {
		return data;
	}

	public void setData(SearchDataDTO data) {
		this.data = data;
	}
}
