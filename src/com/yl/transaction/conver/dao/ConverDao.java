package com.yl.transaction.conver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ConverDao {
	public void insertConver(Map<String, String> param);
	
	public List<Map<String, String>> getConverByCallID(@Param("callID") String callID);
	
	public void updateTalkFlag(@Param("maxaccept") String maxaccept);
	
	public void updateHangupTar(Map<String, String> param);
	
	public void finashConver(Map<String, String> param);
}
