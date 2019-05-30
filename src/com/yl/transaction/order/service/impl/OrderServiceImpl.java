package com.yl.transaction.order.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yl.transaction.order.dao.OrderDao;
import com.yl.transaction.order.service.OrderService;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Resource
	private OrderDao orderDao;
	
	@Override
	public List<Map<String, String>> getOrderByStatusAndAble(Map<String, Object> param) {
		return orderDao.getOrderByStatusAndAble(param);
	}

	@Override
	public int insertOrder(Map<String, Object> param) {
		return orderDao.insertOrder(param);
	}

	@Override
	public int updateOrderInfo(Map<String, Object> param) {
		return orderDao.updateOrderInfo(param);
	}

	@Override
	public List<Map<String, String>> getOrderByParam(Map<String, Object> param) {
		return orderDao.getOrderByParam(param);
	}

	@Override
	public List<Map<String, Object>> getOrderPatientDet(Map<String, Object> param) {
		return orderDao.getOrderPatientDet(param);
	}

	@Override
	public List<Map<String, Object>> getOrderBasePatientDet(Map<String, Object> param) {
		return orderDao.getOrderBasePatientDet(param);
	}

	@Override
	public List<Map<String, String>> getCodeNum(Map<String, Object> param) {
		return orderDao.getCodeNum(param);
	}

	@Override
	public int updateStartTime(Map<String, Object> param) {
		return orderDao.updateStartTime(param);
	}

	@Override
	public List<Map<String, String>> getOutTimeOrder(Map<String, Object> param) {
		return orderDao.getOutTimeOrder(param);
	}

	@Override
	public List<Map<String, String>> getOrderList(Map<String, Object> param) {
		return orderDao.getOrderList(param);
	}

	@Override
	public List<Map<String, Object>> getPageOrder(Map<String, Object> param) {
		return orderDao.getPageOrder(param);
	}

	@Override
	public int delOrder(Map<String, String> param) {
		return orderDao.delOrder(param);
	}

	@Override
	public int updateOrderInfoApp(Map<String, Object> param) {
		return orderDao.updateOrderInfoApp(param);
	}

	@Override
	public List<Map<String, Object>> getStatisticsList(Map<String, Object> param) {
		return orderDao.getStatisticsList(param);
	}

	@Override
	public List<Map<String, Object>> getStatisticsTotal(Map<String, Object> param) {

		return orderDao.getStatisticsTotal(param);
	}

	@Override
	public void saveInitOrder(Map<String, String> param) {
		orderDao.saveInitOrder(param);
	}

	@Override
	public void saveOrderList(Map<String, String> param) {
		orderDao.saveOrderList(param);
	}

	@Override
	public void sendOrder(Map<String, String> param) {
		orderDao.sendOrder(param);
	}

	@Override
	public void insertOrderList(Map<String, String> param) {
		orderDao.insertOrderList(param);
	}

	@Override
	public List<Map<String, String>> getPendingOrder(Map<String, Object> param) {
		return orderDao.getPendingOrder(param);
	}

	@Override
	public void overOrder(Map<String, String> param) {
		orderDao.overOrder(param);
	}

	@Override
	public void overOrderList(Map<String, String> param) {
		orderDao.overOrderList(param);
	}

	@Override
	public List<Map<String, String>> getOrderLocusList(Map<String, String> param) {
		return orderDao.getOrderLocusList(param);
	}
}
