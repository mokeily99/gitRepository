package com.yl.transaction.role.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.LayTableResult;
import com.yl.common.pojo.Result;
import com.yl.common.pojo.TableResult;
import com.yl.common.user.pojo.UserView;
import com.yl.common.util.DBUtil;
import com.yl.transaction.menu.service.MenuService;
import com.yl.transaction.role.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Resource
	private RoleService roleService;

	@Resource
	private MenuService menuService;

	@Resource
	private PublicDao publicDao;

	/**
	 * 加载角色列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/roleList")
	@ResponseBody
	public LayTableResult<?> roleList(Integer page, Integer limit, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> roleList = roleService.getAllRoles();
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(roleList);
			
			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(roleList);
		}catch(Exception e){
			tableResult.setCode(1);
			tableResult.setMsg("数据加载失败！");
			tableResult.setCount(0);
			tableResult.setData(new ArrayList<Map<String, String>>());
		}
		return tableResult;
	}
	/**
	 * 组件加载角色列表
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/comRoleList")
	public void roleList(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Map<String, String>> roleList = roleService.getAllRoles();
		write(response, roleList);
	}

	/**
	 * 加载角色下菜单
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/roleMenu")
	public void roleMenu(HttpServletRequest request, HttpServletResponse response, Model model) {
		UserView userView = this.getUserView(request);
		String maxaccept = request.getParameter("maxaccept");

		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		if (userView != null) {
			List<Map<String, String>> pmList = menuService.getAllParentMemus();
			List<Map<String, String>> smList = menuService.getAllSonMemus();
			Map<String, String> roleMap = roleService.getRoleByID(maxaccept);
			String roleMenus = roleMap.get("ROLE_MENUS");
			String[] menus = null;
			if (StringUtils.isNotBlank(roleMenus)) {
				menus = roleMenus.split(",");
			}else{
				menus = new String[0];
			}
			for (int ix = 0; ix < pmList.size(); ix++) {

				Map<String, Object> parentMap = new HashMap<String, Object>();
				parentMap.put("id", pmList.get(ix).get("MAXACCEPT"));
				parentMap.put("label", pmList.get(ix).get("MENU_NAME"));
				List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
				for (Map<String, String> menu : smList) {

					if (menu.get("PID").equals(pmList.get(ix).get("MAXACCEPT"))) {
						Map<String, Object> sonMap = new HashMap<String, Object>();
						sonMap.put("id", menu.get("MAXACCEPT"));
						sonMap.put("label", menu.get("MENU_NAME"));
						for (int iy = 0; iy < menus.length; iy++) {
							if (menu.get("MAXACCEPT").equals(menus[iy])) {
								sonMap.put("checked", true);
							}
						}
						childList.add(sonMap);
					}

				}
				parentMap.put("children", childList);
				menuList.add(parentMap);
			}
		}

		write(response, menuList);
	}

	/**
	 * 保存角色菜单列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/saveRoleMenus")
	@ResponseBody
	public Result saveRoleMenus(HttpServletRequest request, HttpServletResponse response, Model model) {
		String roleMenus = request.getParameter("ids");
		String roleID = request.getParameter("roleID");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			if (roleMenus.contains(","))
				roleMenus = roleMenus.substring(0, roleMenus.length() - 1);
			Map<String, String> param = new HashMap<String, String>();
			param.put("roleMenus", roleMenus);
			param.put("roleID", roleID);
			roleService.saveRoleMenus(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}

	/**
	 * 添加角色
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/addRole")
	@ResponseBody
	public Result addRole(HttpServletRequest request, HttpServletResponse response, Model model) {
		String roleName = request.getParameter("add_role_name");
		String roleDes = request.getParameter("add_role_des");
		String roleLevel = request.getParameter("add_role_type");

		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("roleName", roleName);
			param.put("roleDes", roleDes);
			param.put("roleLevel", roleLevel);
			param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
			roleService.addRole(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 修改角色
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/modRole")
	@ResponseBody
	public Result modRole(HttpServletRequest request, HttpServletResponse response, Model model) {
		String maxaccept = request.getParameter("edit_role_id");
		String roleName = request.getParameter("edit_role_name");
		String roleDes = request.getParameter("edit_role_des");
		String roleLevel = request.getParameter("edit_role_type");

		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("roleName", roleName);
			param.put("roleDes", roleDes);
			param.put("roleLevel", roleLevel);
			param.put("maxaccept", maxaccept);
			roleService.modRole(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}

	/**
	 * 删除角色
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/delRoleMenus")
	@ResponseBody
	public Result delRoleMenus(HttpServletRequest request, HttpServletResponse response, Model model) {
		String roleID = request.getParameter("ids");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			if (roleID.contains(","))
				roleID = roleID.substring(0, roleID.length() - 1);

			roleService.delRoleInIDS(roleID);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
}
