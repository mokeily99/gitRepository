package com.yl.transaction.personnel.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface PersonnelDao {

	public List<Map<String, String>> getAllPersonnel(Map<String, Object> param);
	
	public void addPersonnel(Map<String, String> param);
	
	public void delPersonnelInIDS(@Param("ids") String ids);
	
	public void updatePersonnelByID(Map<String, String> param);
	
	public List<Map<String, String>> getPersonnelByAccount(@Param("account") String account);
	
	public String getTotalPersonnel();
	
	public List<Map<String, String>> getPersonnelByParam(Map<String, String> param);
	
	public void updatePassByMax(Map<String, String> param);
	
	public void updateUserInfoByID(Map<String, String> param);
	
	public List<Map<String, String>> isMySeat(Map<String, String> param);
	
	public List<Map<String, String>> getDeptPerson(Map<String, String> param);
	
	public Map<String, String> getPersonnelByMax(@Param("maxaccept") String maxaccept);
}
