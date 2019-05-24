package com.yl.transaction.conver.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yl.transaction.conver.dao.ConverDao;
import com.yl.transaction.conver.service.ConverService;

@Service("converService")
public class ConverServiceImpl implements ConverService{

	@Resource
	private ConverDao converDao;

	@Override
	public void insertConver(Map<String, String> param) {
		converDao.insertConver(param);
	}

	@Override
	public List<Map<String, String>> getConverByCallID(String callID) {
		return converDao.getConverByCallID(callID);
	}

	@Override
	public void updateTalkFlag(String maxaccept) {
		converDao.updateTalkFlag(maxaccept);
	}

	@Override
	public void updateHangupTar(Map<String, String> param) {
		converDao.updateHangupTar(param);
	}

	@Override
	public void finashConver(Map<String, String> param) {
		converDao.finashConver(param);
	}
}
