package com.yl.transaction.patientManager.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yl.transaction.patientManager.dao.PatientManagerDao;
import com.yl.transaction.patientManager.service.PatientManagerService;

@Service("patientManagerService")
public class PatientManagerServiceImpl implements PatientManagerService{

	@Resource
	private PatientManagerDao patientManagerDao;

	@Override
	public List<Map<String, String>> getAllPatientManger(
			Map<String, Object> param) {
		
		return patientManagerDao.getAllPatientManger(param);
	}

	@Override
	public void addPatientManger(Map<String, String> param) {
		patientManagerDao.addPatientManger(param);
		
	}

	@Override
	public void delPatientMangerInIDS(Map<String, String> param) {
		patientManagerDao.delPatientMangerInIDS(param);
		
	}

	@Override
	public void updatePatientMangertByID(Map<String, String> param) {
		patientManagerDao.updatePatientMangertByID(param);
		
	}

	@Override
	public List<Map<String, String>> getPatientMangerByAccount(Map<String, Object> param) {
		
		return patientManagerDao.getPatientMangerByAccount(param);
	}

	@Override
	public String getTotalPersonnel() {
		
		return patientManagerDao.getTotalPatientManger();
	}

	@Override
	public List<Map<String, String>> getPatientByParam(Map<String, Object> param) {
		return patientManagerDao.getPatientByParam(param);
	}

	@Override
	public int updatePatientByParam(Map<String, Object> param) {
		return patientManagerDao.updatePatientByParam(param);
	}

	@Override
	public int insertPatientInfo(Map<String, Object> param) {
		return patientManagerDao.insertPatientInfo(param);
	}


	@Override
	public List<Map<String, String>> getPatientTypeList() {
		return patientManagerDao.getPatientTypeList();
	}
	

	@Override
	public List<Map<String, String>> getPatientCJInfo(Map<String, Object> param) {
		return patientManagerDao.getPatientCJInfo(param);
	}

	@Override
	public int insertBasePatientInfo(Map<String, Object> param) {
		return patientManagerDao.insertBasePatientInfo(param);
	}

	@Override
	public List<Map<String, String>> getBasePatientByParam(Map<String, Object> param) {
		return patientManagerDao.getBasePatientByParam(param);
	}

	@Override
	public int insertCJMapper(Map<String, Object> param) {
		return patientManagerDao.insertCJMapper(param);
	}

	@Override
	public int updateBasePatient(Map<String, Object> param) {
		return patientManagerDao.updateBasePatient(param);
	}

	@Override
	public int patientTempToReal(Map<String, Object> param) {
		return patientManagerDao.patientTempToReal(param);
	}

	@Override
	public List<Map<String, String>> getPatientSexList() {
		
		return patientManagerDao.getPatientSexList();
	}

	@Override
	public String getVersion(Map<String, String> para) {
		
		return patientManagerDao.getVersion(para);
	}

	@Override
	public List<Map<String, Object>> getPatientInfoDet(Map<String, Object> param) {
	
		return patientManagerDao.getPatientInfoDet(param);
	}


}
