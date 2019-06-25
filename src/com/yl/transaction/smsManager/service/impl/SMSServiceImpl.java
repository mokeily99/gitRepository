package com.yl.transaction.smsManager.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yl.transaction.senwords.dao.SenwordsDao;
import com.yl.transaction.senwords.service.SenwordsService;
import com.yl.transaction.smsManager.dao.SMSDao;
import com.yl.transaction.smsManager.service.SMSService;

@Service("smsService")
public class SMSServiceImpl implements SMSService{

	@Resource
	private SMSDao smsDao;
	
	@Override
	public List<Map<String, String>> getBlackList(Map<String, Object> param) {
		return smsDao.getBlackList(param);
	}

	@Override
	public void updateBlackStatus(Map<String, String> param) {
		smsDao.updateBlackStatus(param);
	}

	@Override
	public void addBlackList(Map<String, String> param) {
		smsDao.addBlackList(param);
	}

	@Override
	public void updateBlackList(Map<String, String> param) {
		smsDao.updateBlackList(param);
	}

	@Override
	public void delBlackListInIDS(String param) {
		smsDao.delBlackListInIDS(param);
	}

	@Override
	public List<Map<String, String>> getMouldList(Map<String, Object> param) {
		return smsDao.getMouldList(param);
	}

	@Override
	public void addMould(Map<String, String> param) {
		smsDao.addMould(param);
	}

	@Override
	public void editMould(Map<String, String> param) {
		smsDao.editMould(param);
	}

	@Override
	public void delMouldInIDS(String param) {
		smsDao.delMouldInIDS(param);
	}

	@Override
	public List<Map<String, String>> getMouldInfo(Map<String, String> param) {
		return smsDao.getMouldInfo(param);
	}

	@Override
	public void insertSmsInfo(Map<String, String> param) {
		smsDao.insertSmsInfo(param);
	}

	@Override
	public List<Map<String, String>> getSmsList(Map<String, Object> param) {
		return smsDao.getSmsList(param);
	}

	@Override
	public List<Map<String, String>> getSmsHisList(Map<String, Object> param) {
		return smsDao.getSmsHisList(param);
	}

	@Override
	public Map<String, String> getSMSCount(Map<String, String> param) {
		return smsDao.getSMSCount(param);
	}

	@Override
	public Map<String, String> getUnSend(Map<String, String> param) {
		return smsDao.getUnSend(param);
	}

	@Override
	public Map<String, String> getSMSSend(Map<String, String> param) {
		return smsDao.getSMSSend(param);
	}

	@Override
	public List<Map<String, String>> getSmsListAnalyse(Map<String, Object> param) {
		return smsDao.getSmsListAnalyse(param);
	}

	@Override
	public Map<String, String> getSendNum(Map<String, String> param) {
		return smsDao.getSendNum(param);
	}

	@Override
	public Integer getSMSNum(Map<String, String> param) {
		return smsDao.getSMSNum(param);
	}

	
}
