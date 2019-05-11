package com.yl.transaction.seat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SeatDao {

	public List<Map<String, String>> getSeatFreeBusyInfo(Map<String, String> param);
	
	public void setBusyFree(Map<String, String> param);
	
	public void insertSeatInfo(Map<String, String> param);
}
