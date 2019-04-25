package com.yl.transaction.role.service;

import java.util.List;
import java.util.Map;

public interface RoleService {

	public List<Map<String, String>> getAllRoles();
	
	public Map<String, String> getRoleByID(String maxaccept);
	
	public void saveRoleMenus(Map<String, String> param);
	
	public void addRole(Map<String, String> param);
	
	public void delRoleInIDS(String maxaccept);
	
	public void modRole(Map<String, String> param);
}
