package com.yl.common.dao;

import java.util.Map;

public interface PublicDao {

	public String getMaxaccept();
	
	public String getToken();
	
	public int insertSMS(Map<String, Object> param);
	
	public int insertCodeNum(Map<String, Object> param);
}
