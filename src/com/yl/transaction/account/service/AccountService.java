package com.yl.transaction.account.service;

import java.util.List;
import java.util.Map;

public interface AccountService {

	public List<Map<String, String>> getAccountList(Map<String, Object>param);
	
	public void addAccount(Map<String, String> param);
	
	public void updateAccountByIds(Map<String, String> param);
	
	public void updateAccountBalance(Map<String, String> param);
	
	public void delDeptInIDS(String ids);
	
	public void delDeptInPIDS(String ids);
	
	public void updateDeptByID(Map<String, String> param);
	
	public List<Map<String, String>> getPersonnelByAccount(String account);
	
	public String getTotalDept();
	
	public List<Map<String, String>> getAllDept(Map<String, String> param);
	
	public List<Map<String, String>> getDeptByType(Map<String, String> param);
	
	public List<Map<String, String>> getOnlineUserByDeptCode(Map<String, String> param);
	
	public List<Map<String, String>> isAdminAccept(Map<String, String> param);
	
	public List<Map<String, Object>> getDeptListByPID(Map<String, Object> param);
	
	public List<Map<String, Object>> getDeptTreeByPID(Map<String, Object> param);

	public List<Map<String, String>> getjzList(Map<String, String> param);

	public String getDept(Map<String, String> param);
	
	public List<Map<String, String>> getSonList(Map<String, String> param);
	
	public List<Map<String, String>> getSonDept(Map<String, String> param);
}
