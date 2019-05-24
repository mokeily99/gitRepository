package com.yl.transaction.conver.service;

import java.util.List;
import java.util.Map;

public interface ConverService {

	public void insertConver(Map<String, String> param);
	
	public List<Map<String, String>> getConverByCallID(String callID);
	
	public void updateTalkFlag(String maxaccept);
	
	public void updateHangupTar(Map<String, String> param);
	
	public void finashConver(Map<String, String> param);
}
