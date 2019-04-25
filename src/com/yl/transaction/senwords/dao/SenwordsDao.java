package com.yl.transaction.senwords.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SenwordsDao {

	public List<Map<String, String>> getSenWordsList(Map<String, Object> param);
	
	public void updateSenwordsStatus(Map<String, String> param);
	
	public void addSenwords(Map<String, String> param);
	
	public void updateSenwords(Map<String, String> param);
	
	public void delSenwordsInIDS(@Param("ids") String ids);
}
