package com.yl.transaction.senwords.service;

import java.util.List;
import java.util.Map;

public interface SenwordsService {

	public List<Map<String, String>> getSenWordsList(Map<String, Object> param);
	
	public void updateSenwordsStatus(Map<String, String> param);
	
	public void addSenwords(Map<String, String> param);
	
	public void updateSenwords(Map<String, String> param);
	
	public void delSenwordsInIDS(String param);
}