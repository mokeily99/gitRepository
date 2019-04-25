package com.yl.transaction.custManager.service;

import java.util.List;
import java.util.Map;

public interface CustService {

	public List<Map<String, String>> getCustList(Map<String, Object> param);
	
	public void updateBlackStatus(Map<String, String> param);
	
	public void addCustList(Map<String, String> param);
	
	public void updateCustInfo(Map<String, String> param);
	
	public void delCustInIDS(String param);
}