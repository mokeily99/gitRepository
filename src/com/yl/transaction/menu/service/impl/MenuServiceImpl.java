package com.yl.transaction.menu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yl.transaction.menu.dao.MenuDao;
import com.yl.transaction.menu.pojo.Menu;
import com.yl.transaction.menu.service.MenuService;

@Service("menuService")
public class MenuServiceImpl implements MenuService{

	@Resource  
	private MenuDao menuDao; 
	
	public List<Map<String,String>> loadMenu(String roleMenus) {
		return menuDao.loadMenu(roleMenus);
	}

	public List<Menu> getParentMenu() {
		return menuDao.getParentMenu();
	}

	public Menu getMenuByID(String maxaccept) {
		return menuDao.getMenuByID(maxaccept);
	}

	public List<Menu> getMenuInID(String maxaccept) {
		return menuDao.getMenuInID(maxaccept);
	}

	public void updateMenuByID(Map<String, String> param) {
		menuDao.updateMenuByID(param);
	}

	public String getMaxIndeByParenID(String parentMenuID) {
		return menuDao.getMaxIndeByParenID(parentMenuID);
	}

	public String getMaxInde() {
		return menuDao.getMaxInde();
	}

	public void addMenu(Map<String, String> param) {
		menuDao.addMenu(param);
	}

	public List<Menu> getMenusByParenID(String pid) {
		return menuDao.getMenusByParenID(pid);
	}

	public void deleteMenuByID(String maxaccept) {
		menuDao.deleteMenuByID(maxaccept);
	}

	public List<Map<String, String>> getAllParentMemus() {
		return menuDao.getAllParentMemus();
	}

	public List<Map<String, String>> getAllSonMemus() {
		return menuDao.getAllSonMemus();
	}

	public List<Menu> getMenuInIDByLevel(Map<String, String> param) {
		return menuDao.getMenuInIDByLevel(param);
	}

	@Override
	public void deleteMenusByPID(String ids) {
		menuDao.deleteMenusByPID(ids);
	}

}
