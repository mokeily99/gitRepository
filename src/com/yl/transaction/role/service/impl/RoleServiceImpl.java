package com.yl.transaction.role.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yl.transaction.role.dao.RoleDao;
import com.yl.transaction.role.service.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService{

	@Resource  
	private RoleDao roleDao;

	public List<Map<String, String>> getAllRoles() {
		return roleDao.getAllRoles();
	}

	public Map<String, String> getRoleByID(String maxaccept) {
		return roleDao.getRoleByID(maxaccept);
	}

	public void saveRoleMenus(Map<String, String> rolesMenu) {
		roleDao.saveRoleMenus(rolesMenu);
	}

	public void addRole(Map<String, String> param) {
		roleDao.addRole(param);
	}

	public void delRoleInIDS(String maxaccept) {
		roleDao.delRoleInIDS(maxaccept);
	}

	public void modRole(Map<String, String> param) {
		roleDao.modRole(param);
	} 
}
