package com.yl.transaction.patientManager.dao;

import java.util.List;
import java.util.Map;


public interface PatientManagerDao {
	
	public List<Map<String, String>> getAllPatientManger(Map<String, Object> param);
	
	public void addPatientManger(Map<String, String> param);
	
	public void delPatientMangerInIDS(Map<String, String> param);
	
	public void updatePatientMangertByID(Map<String, String> param);
	
	public List<Map<String, String>> getPatientMangerByAccount(Map<String, Object> param);
	
	public String getTotalPatientManger();
	
	public List<Map<String, String>> getPatientByParam(Map<String, Object> param);
	
	public int updatePatientByParam(Map<String, Object> param);
	
	public int insertPatientInfo(Map<String, Object> param);


	public List<Map<String, String>> getPatientTypeList();

	public List<Map<String, String>> getPatientSexList();
	
	public List<Map<String, String>> getPatientCJInfo(Map<String, Object> param);
	
	public int insertBasePatientInfo(Map<String, Object> param);
	
	public List<Map<String, String>> getBasePatientByParam(Map<String, Object> param);
	
	public int insertCJMapper(Map<String, Object> param);
	
	public int updateBasePatient(Map<String, Object> param);
	
	public int patientTempToReal(Map<String, Object> param);
	
	public String getVersion(Map<String, String> para);
	
	public List<Map<String, Object>> getPatientInfoDet(Map<String, Object> param);
}
