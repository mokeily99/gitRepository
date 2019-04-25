package com.yl.transaction.smsManager.service;

import java.util.List;
import java.util.Map;

public interface SMSService {

	public List<Map<String, String>> getBlackList(Map<String, Object> param);
	
	public void updateBlackStatus(Map<String, String> param);
	
	public void addBlackList(Map<String, String> param);
	
	public void updateBlackList(Map<String, String> param);
	
	public void delBlackListInIDS(String param);
}