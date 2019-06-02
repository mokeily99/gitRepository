package com.yl.transaction.order.service;

import java.util.List;
import java.util.Map;


public interface OrderService {

	public int updateOrderInfo(Map<String, Object> param);
	
	public void saveOrderList(Map<String, String> param);
	
	public void saveInitOrder(Map<String, String> param);
	
	public List<Map<String, String>> getOrderList(Map<String, Object> param);
	
	public List<Map<String, Object>> getPageOrder(Map<String, Object> param);
	
	public void sendOrder(Map<String, String> param);
	
	public void insertOrderList(Map<String, String> param);
	
	public List<Map<String, String>> getPendingOrder(Map<String, Object> param);
	
	public void overOrder(Map<String, String> param);
	
	public void overOrderList(Map<String, String> param);
	
	public List<Map<String, String>> getOrderLocusList(Map<String, String> param);
	
	
	public List<Map<String, String>> getOrderByStatusAndAble(Map<String, Object> param);
	
	public int insertOrder(Map<String, Object> param);
	
	public int updateOrderInfoApp(Map<String, Object> param);
	
	public List<Map<String, String>> getOrderByParam(Map<String, Object> param);
	
	public List<Map<String, Object>> getOrderPatientDet(Map<String, Object> param);
	
	public List<Map<String, Object>> getOrderBasePatientDet(Map<String, Object> param);
	
	public List<Map<String, String>> getCodeNum(Map<String, Object> param);
	
	public int updateStartTime(Map<String, Object> param);
	
	public List<Map<String, String>> getOutTimeOrder(Map<String, Object> param);
	
	public int delOrder(Map<String, String> param);

	public List<Map<String, Object>> getStatisticsList(Map<String, Object> param);

	public List<Map<String, Object>> getStatisticsTotal(Map<String, Object> param);
	
	public Map<String, String> getOrderTypeCount(Map<String, String> param);
}
