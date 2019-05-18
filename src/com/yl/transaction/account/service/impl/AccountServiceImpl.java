package com.yl.transaction.account.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yl.transaction.account.dao.AccountDao;
import com.yl.transaction.account.service.AccountService;

@Service("accountService")
public class AccountServiceImpl implements AccountService{

	@Resource
	private AccountDao accountDao;

	public List<Map<String, String>> getAccountList(Map<String, Object> param) {
		return accountDao.getAccountList(param);
	}

	public void addAccount(Map<String, String> param) {
		accountDao.addAccount(param);
	}
	
	public void updateAccountByIds(Map<String, String> param){
		accountDao.updateAccountByIds(param);
	}

	public void delDeptInIDS(String ids) {
		accountDao.delDeptInIDS(ids);
	}

	public void updateDeptByID(Map<String, String> param) {
		accountDao.updateDeptByID(param);
	}

	public List<Map<String, String>> getPersonnelByAccount(String account) {
		return accountDao.getPersonnelByAccount(account);
	}

	public String getTotalDept() {
		return accountDao.getTotalDept();
	}

	public List<Map<String, String>> getAllDept(Map<String, String> param) {
		return accountDao.getAllDept(param);
	}

	public List<Map<String, String>> getDeptByType(Map<String, String> param) {
		return accountDao.getDeptByType(param);
	}

	public List<Map<String, String>> getOnlineUserByDeptCode(Map<String, String> param) {
		return accountDao.getOnlineUserByDeptCode(param);
	}

	public List<Map<String, String>> isAdminAccept(Map<String, String> param) {
		return accountDao.isAdminAccept(param);
	}

	public List<Map<String, Object>> getDeptListByPID(Map<String, Object> param) {
		return accountDao.getDeptListByPID(param);
	}

	public List<Map<String, Object>> getDeptTreeByPID(Map<String, Object> param) {
		return accountDao.getDeptTreeByPID(param);
	}

	public List<Map<String, String>> getjzList(Map<String, String> param) {
		return accountDao.getjzList(param);
	}

	public String getDept(Map<String, String> param) {
		return accountDao.getDept(param);
	}

	public List<Map<String, String>> getSonList(Map<String, String> param) {
		return accountDao.deptDaogetSonList(param);
	}

	public List<Map<String, String>> getSonDept(Map<String, String> param) {
		return accountDao.getSonDept(param);
	}

	@Override
	public void delDeptInPIDS(String ids) {
		accountDao.delDeptInPIDS(ids);
	}
}
