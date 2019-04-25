package com.yl.transaction.role.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface RoleDao {

	public List<Map<String, String>> getAllRoles();
	
	public Map<String, String> getRoleByID(@Param("maxaccept") String maxaccept);
	
	public void saveRoleMenus(Map<String, String> param);
	
	public void addRole(Map<String, String> param);
	
	public void delRoleInIDS(@Param("ids") String ids);
	
	public void modRole(Map<String, String> param);
}
