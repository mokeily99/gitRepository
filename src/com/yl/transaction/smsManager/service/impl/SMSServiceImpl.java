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

	
}
