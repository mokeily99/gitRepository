package com.yl.transaction.senwords.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yl.transaction.senwords.dao.SenwordsDao;
import com.yl.transaction.senwords.service.SenwordsService;

@Service("senwordsService")
public class SenwordsServiceImpl implements SenwordsService{

	@Resource
	private SenwordsDao senwordsDao;
	
	@Override
	public List<Map<String, String>> getSenWordsList(Map<String, Object> param) {
		return senwordsDao.getSenWordsList(param);
	}

	@Override
	public void updateSenwordsStatus(Map<String, String> param) {
		senwordsDao.updateSenwordsStatus(param);
	}

	@Override
	public void addSenwords(Map<String, String> param) {
		senwordsDao.addSenwords(param);
	}

	@Override
	public void updateSenwords(Map<String, String> param) {
		senwordsDao.updateSenwords(param);
	}

	@Override
	public void delSenwordsInIDS(String param) {
		senwordsDao.delSenwordsInIDS(param);
	}

	
}
