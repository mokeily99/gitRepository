package com.yl.transaction.conver.service;

import java.util.List;
import java.util.Map;

public interface ConverService {

	public void insertConver(Map<String, String> param);
	
	public List<Map<String, String>> getConverByCallID(String callID);
	
	public void updateTalkFlag(String maxaccept);
	
	public void updateHangupTar(Map<String, String> param);
	
	public void finashConver(Map<String, String> param);
	
	public List<Map<String, String>> getConverList(Map<String, Object> param);
	
	public List<Map<String, String>> getCallStatusBySeatID(String seatID);
	
	public void insertCallStatus(Map<String, String> param);
	
	public void updateCallStatus(Map<String, String> param);
	
	public List<Map<String, String>> getCallStatus(Map<String, String> param);
	
	public void updateShowStauts(String seatID);
	
	public Map<String, String> getConverCount(Map<String, String> param);
	
	public Map<String, String> getIntoConverCount(Map<String, String> param);
	
	public Map<String, String> getOutConverCount(Map<String, String> param);
}
