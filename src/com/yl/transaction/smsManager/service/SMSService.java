package com.yl.transaction.smsManager.service;

import java.util.List;
import java.util.Map;

public interface SMSService {

	public List<Map<String, String>> getBlackList(Map<String, Object> param);
	
	public void updateBlackStatus(Map<String, String> param);
	
	public void addBlackList(Map<String, String> param);
	
	public void updateBlackList(Map<String, String> param);
	
	public void delBlackListInIDS(String param);
	
	public List<Map<String, String>> getMouldList(Map<String, Object> param);
	
	public void addMould(Map<String, String> param);
	
	public void editMould(Map<String, String> param);
	
	public void delMouldInIDS(String param);
	
	public List<Map<String, String>> getMouldInfo(Map<String, String> param);
	
	public void insertSmsInfo(Map<String, String> param);
	
	public List<Map<String, String>> getSmsList(Map<String, Object> param);
}