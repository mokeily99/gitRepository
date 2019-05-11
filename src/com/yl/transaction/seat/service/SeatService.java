package com.yl.transaction.seat.service;

import java.util.List;
import java.util.Map;

public interface SeatService {

	public List<Map<String, String>> getSeatFreeBusyInfo(Map<String, String> param);
	
	public void setBusyFree(Map<String, String> param);
	
	public void insertSeatInfo(Map<String, String> param);
}