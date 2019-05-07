package com.yl.transaction.custManager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CustDao {

	public List<Map<String, String>> getCustList(Map<String, Object> param);
	
	public void updateBlackStatus(Map<String, String> param);
	
	public void addCustList(Map<String, String> param);
	
	public void updateCustInfo(Map<String, String> param);
	
	public void delCustInIDS(@Param("ids") String ids);
	
	public Map<String, String> getCustInfo(Map<String, String> param);
}
