package com.yl.transaction.code.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CodeDao {

	public List<Map<String, String>> getCodeList(String codeType);
	
	public List<Map<String, String>> getCommonCode(Map<String, String> param);

	public Map<String, String> getCodename(@Param("code_key") String code_key,@Param("code_id") String code_id);
	
	public Map<String, String> getCodeByName(@Param("code_key") String code_key,@Param("code_name") String code_name);
	
	public List<Map<String, String>> getCommonCustTypeCode(Map<String, String> param);
}
