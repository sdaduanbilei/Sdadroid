package com.scorpio.frame.request;

public interface DataResponse {
	
	public void onSucc(Object response);
	
	public void onFail(String error);
}
