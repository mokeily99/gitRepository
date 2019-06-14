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
	
	public List<Map<String, String>> getConverList(Map<String, Object> param);
	
	public List<Map<String, String>> getCallStatusBySeatID(@Param("seatID") String seatID);
	
	public void insertCallStatus(Map<String, String> param);
	
	public void updateCallStatus(Map<String, String> param);
	
	public List<Map<String, String>> getCallStatus(Map<String, String> param);
	
	public void updateShowStauts(@Param("seatID") String seatID);
	
	public Map<String, String> getConverCount(Map<String, String> param);
	
	public Map<String, String> getIntoConverCount(Map<String, String> param);
	
	public Map<String, String> getOutConverCount(Map<String, String> param);
	
	public List<Map<String, String>> getConverSumAn(Map<String, Object> param);
	
	public Map<String, String> getConverIsTalk(Map<String, String> param);
	
	public List<Map<String, String>> getConverIsTalkList(Map<String, String> param);
}
