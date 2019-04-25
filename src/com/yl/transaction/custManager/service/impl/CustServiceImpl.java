package com.yl.transaction.custManager.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yl.transaction.custManager.dao.CustDao;
import com.yl.transaction.custManager.service.CustService;
import com.yl.transaction.senwords.dao.SenwordsDao;
import com.yl.transaction.senwords.service.SenwordsService;
import com.yl.transaction.smsManager.dao.SMSDao;
import com.yl.transaction.smsManager.service.SMSService;

@Service("custService")
public class CustServiceImpl implements CustService{

	@Resource
	private CustDao custDao;
	
	@Override
	public List<Map<String, String>> getCustList(Map<String, Object> param) {
		return custDao.getCustList(param);
	}

	@Override
	public void updateBlackStatus(Map<String, String> param) {
		custDao.updateBlackStatus(param);
	}

	@Override
	public void addCustList(Map<String, String> param) {
		custDao.addCustList(param);
	}

	@Override
	public void updateCustInfo(Map<String, String> param) {
		custDao.updateCustInfo(param);
	}

	@Override
	public void delCustInIDS(String param) {
		custDao.delCustInIDS(param);
	}

	
}
