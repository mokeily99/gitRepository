package com.yl.transaction.smsManager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

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
	
	public List<Map<String, String>> getSmsHisList(Map<String, Object> param);
	
	public List<Map<String, String>> getSmsSyncList(Map<String, Object> param, RowBounds rowBounds);
	
	public int updateMakeStatus(Map<String, Object> param);
	
	public void deleteSmsSync(@Param("maxaccept") String maxaccept);
	
	public void insertSmsSynchis(Map<String, String> param);
	
	public void updateTryTimes(Map<String, String> param);
	
	public Map<String, String> getSMSCount(Map<String, String> param);
	
	public Map<String, String> getUnSend(Map<String, String> param);
	
	public Map<String, String> getSMSSend(Map<String, String> param);
	
	public List<Map<String, String>> getSmsListAnalyse(Map<String, Object> param);
	
	public Map<String, String> getSendNum(Map<String, String> param);
	
	public Integer getSMSNum(Map<String, String> param);
}
