package com.yl.transaction.seat.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yl.transaction.custManager.dao.CustDao;
import com.yl.transaction.custManager.service.CustService;
import com.yl.transaction.seat.dao.SeatDao;
import com.yl.transaction.seat.service.SeatService;
import com.yl.transaction.senwords.dao.SenwordsDao;
import com.yl.transaction.senwords.service.SenwordsService;
import com.yl.transaction.smsManager.dao.SMSDao;
import com.yl.transaction.smsManager.service.SMSService;

@Service("seatService")
public class SeatServiceImpl implements SeatService{

	@Resource
	private SeatDao seatDao;

	@Override
	public List<Map<String, String>> getSeatFreeBusyInfo(Map<String, String> param) {
		return seatDao.getSeatFreeBusyInfo(param);
	}

	@Override
	public void setBusyFree(Map<String, String> param) {
		seatDao.setBusyFree(param);
	}

	@Override
	public void insertSeatInfo(Map<String, String> param) {
		seatDao.insertSeatInfo(param);
	}

	@Override
	public void updateSeatStatus(Map<String, String> param) {
		seatDao.updateSeatStatus(param);
	}
	
}
