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
}
