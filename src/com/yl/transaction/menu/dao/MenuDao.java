package com.yl.transaction.menu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yl.transaction.menu.pojo.Menu;

public interface MenuDao {

	public List<Map<String,String>> loadMenu(@Param("roleMenus") String roleMenus);
	
	public Menu getMenuByID(@Param("maxaccept") String maxaccept);
	
	public List<Menu> getMenuInID(@Param("maxaccept")String maxaccept);
	
	public List<Menu> getParentMenu();
	
	public void updateMenuByID(Map<String, String> param);
	
	public String getMaxIndeByParenID(@Param("parentMenuID")String parentMenuID);
	
	public String getMaxInde();
	
	public void addMenu(Map<String, String> param);
	
	public List<Menu> getMenusByParenID(@Param("pid") String pid);
	
	public void deleteMenuByID(@Param("maxaccept") String maxaccept);
	
	public void deleteMenusByPID(@Param("ids") String ids);
	
	public List<Map<String,String>> getAllParentMemus();
	
	public List<Map<String,String>> getAllSonMemus();
	
	public List<Menu> getMenuInIDByLevel(Map<String, String> param);
}
