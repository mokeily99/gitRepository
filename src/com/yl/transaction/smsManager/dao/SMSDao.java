package com.yl.transaction.smsManager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SMSDao {

	public List<Map<String, String>> getBlackList(Map<String, Object> param);
	
	public void updateBlackStatus(Map<String, String> param);
	
	public void addBlackList(Map<String, String> param);
	
	public void updateBlackList(Map<String, String> param);
	
	public void delBlackListInIDS(@Param("ids") String ids);
	
	public List<Map<String, String>> getMouldList(Map<String, Object> param);
	
	public void addMould(Map<String, String> param);
	
	public void editMould(Map<String, String> param);
	
	public void delMouldInIDS(@Param("ids") String ids);
	
	public List<Map<String, String>> getMouldInfo(Map<String, String> param);
	
	public void insertSmsInfo(Map<String, String> param);
	
	public List<Map<String, String>> getSmsList(Map<String, Object> param);
}
