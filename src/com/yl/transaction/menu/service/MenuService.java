package com.yl.transaction.menu.service;

import java.util.List;
import java.util.Map;

import com.yl.transaction.menu.pojo.Menu;

public interface MenuService {

	public List<Menu> getParentMenu();
	
	public Menu getMenuByID(String maxaccept);
	
	public List<Menu> getMenuInID(String maxaccept);
	
	public List<Map<String,String>> loadMenu(String roleMenus);
	
	public void updateMenuByID(Map<String, String> param);
	
	public String getMaxIndeByParenID(String parentMenuID);
	
	public String getMaxInde();
	
	public void addMenu(Map<String, String> param);
	
	public List<Menu> getMenusByParenID(String pid);
	
	public void deleteMenuByID(String maxaccept);
	
	public void deleteMenusByPID(String ids);
	
	public List<Map<String, String>> getAllParentMemus();
	
	public List<Map<String, String>> getAllSonMemus();
	
	public List<Menu> getMenuInIDByLevel(Map<String, String> param);
}
