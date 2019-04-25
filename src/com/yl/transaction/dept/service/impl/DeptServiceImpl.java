package com.yl.transaction.dept.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yl.transaction.dept.dao.DeptDao;
import com.yl.transaction.dept.service.DeptService;

@Service("deptService")
public class DeptServiceImpl implements DeptService{

	@Resource
	private DeptDao deptDao;

	public List<Map<String, Object>> getPageDept(Map<String, Object> param) {
		return deptDao.getPageDept(param);
	}

	public void addDept(Map<String, String> param) {
		deptDao.addDept(param);
	}

	public void delDeptInIDS(String ids) {
		deptDao.delDeptInIDS(ids);
	}

	public void updateDeptByID(Map<String, String> param) {
		deptDao.updateDeptByID(param);
	}

	public List<Map<String, String>> getPersonnelByAccount(String account) {
		return deptDao.getPersonnelByAccount(account);
	}

	public String getTotalDept() {
		return deptDao.getTotalDept();
	}

	public List<Map<String, String>> getAllDept(Map<String, String> param) {
		return deptDao.getAllDept(param);
	}

	public List<Map<String, String>> getDeptByType(Map<String, String> param) {
		return deptDao.getDeptByType(param);
	}

	public List<Map<String, String>> getOnlineUserByDeptCode(Map<String, String> param) {
		return deptDao.getOnlineUserByDeptCode(param);
	}

	public List<Map<String, String>> isAdminAccept(Map<String, String> param) {
		return deptDao.isAdminAccept(param);
	}

	public List<Map<String, Object>> getDeptListByPID(Map<String, Object> param) {
		return deptDao.getDeptListByPID(param);
	}

	public List<Map<String, Object>> getDeptTreeByPID(Map<String, Object> param) {
		return deptDao.getDeptTreeByPID(param);
	}

	public List<Map<String, String>> getjzList(Map<String, String> param) {
		return deptDao.getjzList(param);
	}

	public String getDept(Map<String, String> param) {
		return deptDao.getDept(param);
	}

	public List<Map<String, String>> getSonList(Map<String, String> param) {
		return deptDao.deptDaogetSonList(param);
	}

	public List<Map<String, String>> getSonDept(Map<String, String> param) {
		return deptDao.getSonDept(param);
	}

	@Override
	public void delDeptInPIDS(String ids) {
		deptDao.delDeptInPIDS(ids);
	}
}
