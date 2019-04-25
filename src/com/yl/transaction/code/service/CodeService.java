package com.yl.transaction.code.service;

import java.util.List;
import java.util.Map;

public interface CodeService {
	public List<Map<String, String>> getCodeList(String codeType);
	
	public List<Map<String, String>> getCommonCode(Map<String, String> param);
	
	public List<Map<String, String>> getCommonCustTypeCode(Map<String, String> param);

	public Map<String, String> getCodename(String code_key, String code_id);
	
	public Map<String, String> getCodeByName(String code_key, String code_name);
}
