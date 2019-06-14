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

	@Override
	public List<Map<String, String>> getConverList(Map<String, Object> param) {
		return converDao.getConverList(param);
	}

	@Override
	public List<Map<String, String>> getCallStatusBySeatID(String seatID) {
		return converDao.getCallStatusBySeatID(seatID);
	}

	@Override
	public void insertCallStatus(Map<String, String> param) {
		converDao.insertCallStatus(param);
	}

	@Override
	public void updateCallStatus(Map<String, String> param) {
		converDao.updateCallStatus(param);
	}

	@Override
	public List<Map<String, String>> getCallStatus(Map<String, String> param) {
		return converDao.getCallStatus(param);
	}

	@Override
	public void updateShowStauts(String seatID) {
		converDao.updateShowStauts(seatID);
	}

	@Override
	public Map<String, String> getConverCount(Map<String, String> param) {
		return converDao.getConverCount(param);
	}

	@Override
	public Map<String, String> getIntoConverCount(Map<String, String> param) {
		return converDao.getIntoConverCount(param);
	}

	@Override
	public Map<String, String> getOutConverCount(Map<String, String> param) {
		return converDao.getOutConverCount(param);
	}

	@Override
	public List<Map<String, String>> getConverSumAn(Map<String, Object> param) {
		return converDao.getConverSumAn(param);
	}

	@Override
	public Map<String, String> getConverIsTalk(Map<String, String> param) {
		return converDao.getConverIsTalk(param);
	}
}
