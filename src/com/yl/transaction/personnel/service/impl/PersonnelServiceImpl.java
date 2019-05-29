package com.yl.transaction.personnel.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.yl.transaction.personnel.dao.PersonnelDao;
import com.yl.transaction.personnel.service.PersonnelService;

@Service("personnelService")
public class PersonnelServiceImpl implements PersonnelService{

	@Resource
	private PersonnelDao personnelDao;

	public List<Map<String, String>> getAllPersonnel(Map<String, Object> param) {
		return personnelDao.getAllPersonnel(param);
	}

	public void addPersonnel(Map<String, String> param) {
		personnelDao.addPersonnel(param);
	}

	public void delPersonnelInIDS(String ids) {
		personnelDao.delPersonnelInIDS(ids);
	}

	public void updatePersonnelByID(Map<String, String> param) {
		personnelDao.updatePersonnelByID(param);
	}

	public List<Map<String, String>> getPersonnelByAccount(String account) {
		return personnelDao.getPersonnelByAccount(account);
	}

	public String getTotalPersonnel() {
		return personnelDao.getTotalPersonnel();
	}

	@Override
	public List<Map<String, String>> getPersonnelByParam(Map<String, String> param) {
		return personnelDao.getPersonnelByParam(param);
	}
	
	@Override
	public void updatePassByMax(Map<String, String> param) {
		personnelDao.updatePassByMax(param);
	}

	@Override
	public void updateUserInfoByID(Map<String, String> param) {
		personnelDao.updateUserInfoByID(param);
	}

	@Override
	public List<Map<String, String>> isMySeat(Map<String, String> param) {
		return personnelDao.isMySeat(param);
	}

	@Override
	public List<Map<String, String>> getDeptPerson(Map<String, String> param) {
		return personnelDao.getDeptPerson(param);
	}

	@Override
	public Map<String, String> getPersonnelByMax(String maxaccept) {
		return personnelDao.getPersonnelByMax(maxaccept);
	}
}
