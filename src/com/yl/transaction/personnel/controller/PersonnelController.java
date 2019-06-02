package com.yl.transaction.personnel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.LayTableResult;
import com.yl.common.pojo.Result;
import com.yl.common.user.pojo.UserView;
import com.yl.common.util.ConfigUtil;
import com.yl.common.util.DBUtil;
import com.yl.common.util.UploadUtil;
import com.yl.transaction.personnel.service.PersonnelService;

@Controller
@RequestMapping("/personnel")
public class PersonnelController extends BaseController{

	@Resource
	private PersonnelService personnelService;
	
	@Resource
	private PublicDao publicDao;
	
	@RequestMapping("/personnelList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> personnelList(Integer page, Integer limit, String userName, String roleCode, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);
			UserView user = this.getUserView(request);
			param.put("deptCode",user.getDeptCode());
			param.put("roleLevel",user.getRoleLevel());
			param.put("maxaccept",user.getMaxaccept());
			
			param.put("userName",userName);
			param.put("roleCode",roleCode);
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> personnelList = personnelService.getAllPersonnel(param);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(personnelList);
			
			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(personnelList);
		}catch(Exception e){
			tableResult.setCode(1);
			tableResult.setMsg("数据加载失败！");
			tableResult.setCount(0);
			tableResult.setData(new ArrayList<Map<String, String>>());
		}
		return tableResult;
	}
	
	@RequestMapping("/getPersonnel")
	@ResponseBody
	public Result getPersonnel(String deptCode, String deptType, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try{
			Map<String, String> param = new HashMap<String, String>();
			param.put("deptCode", deptCode);
			param.put("deptType", deptType);
			List<Map<String, String>> personnelList = personnelService.getPersonnelByParam(param);
			result.setResultData(personnelList);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 增加人员
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/addPersonnel")
	@ResponseBody
	public Result addPersonnel(HttpServletRequest request, HttpServletResponse response, Model model) {
		String account = request.getParameter("add_personnel_account");
		String pwd = request.getParameter("add_personnel_pwd");
		String name = request.getParameter("add_personnel_name");
		String phone = request.getParameter("add_personnel_phone");
		String address = request.getParameter("add_personnel_address");
		String role = request.getParameter("add_personnel_role");
		String dept = request.getParameter("add_personnel_dept");
		Map<String, String> param = new HashMap<String, String>();
		param.put("account", account);
		param.put("pwd", pwd);
		param.put("name", name);
		param.put("phone", phone);
		param.put("address", address);
		param.put("role", role);
		param.put("dept", dept);
		param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			List<Map<String, String>> person = personnelService.getPersonnelByAccount(account);
			if(person.size() < 1){
				personnelService.addPersonnel(param);
			}else{
				result.setResultCode("9999");
				result.setResultMsg("登录账户已存在，添加失败!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 删除角色
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/delPersonnel")
	@ResponseBody
	public Result delPersonnel(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		String ids = request.getParameter("ids");
		try {
			if(ids.contains(",")){
				ids = ids.substring(0, ids.length()-1);
			}
			personnelService.delPersonnelInIDS(ids);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 修改人员信息
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/modifyPersonnel")
	@ResponseBody
	public Result modifyPersonnel(HttpServletRequest request, HttpServletResponse response, Model model) {
		String maxaccept = request.getParameter("edit_personnel_id");
		String account = request.getParameter("edit_personnel_account");
		String pwd = request.getParameter("edit_personnel_pwd");
		String name = request.getParameter("edit_personnel_name");
		String phone = request.getParameter("edit_personnel_phone");
		String address = request.getParameter("edit_personnel_address");
		String role = request.getParameter("edit_personnel_role");
		String dept = request.getParameter("edit_personnel_dept");
		Map<String, String> param = new HashMap<String, String>();
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			param.put("maxaccept", maxaccept);
			param.put("account", account);
			param.put("pwd", pwd);
			param.put("name", name);
			param.put("phone", phone);
			param.put("address", address);
			param.put("role", role);
			param.put("dept", dept);
			
			personnelService.updatePersonnelByID(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 密码修改
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/modifyPassword")
	@ResponseBody
	public Result modifyPassword(HttpServletRequest request, HttpServletResponse response, Model model) {
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		
		Map<String, String> param = new HashMap<String, String>();
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			UserView user = this.getUserView(request);
			param.put("maxaccept", user.getMaxaccept());
			param.put("oldPass", oldPwd);
			param.put("newPass", newPwd);
			
			if(user != null){
				personnelService.updatePassByMax(param);
			}
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
	/**
	 * 修改个人资料
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/updateUserInfo")
	@ResponseBody
	public Result updateUserInfo(HttpServletRequest request, HttpServletResponse response, Model model){
		 
		String account = request.getParameter("account");
		String password = request.getParameter("pwd");
		String name= request.getParameter("userName");
		String phone=request.getParameter("userPhone");
		String tel=request.getParameter("userTel");
		String address =request.getParameter("userAddress");
		String level=request.getParameter("userLevel");
		//String face=request.getParameter("userFace");
		
		Map<String, String> param = new HashMap<String, String>();
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try{
			UserView user = this.getUserView(request);
			param.put("maxaccept", user.getMaxaccept());
			param.put("account", account);
			param.put("pwd",password);
			param.put("userName",name);
			param.put("userPhone", phone);
			param.put("userTel", tel);
			param.put("userAddress", address);
		//	param.put("userFace", face);
			personnelService.updateUserInfoByID(param);
		}catch(Exception e){
			logger.error(e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
	@RequestMapping("/uploadUserFace")
	@ResponseBody
	public Result uploadUserFace(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam("user_face") MultipartFile file){
		Result result = new Result();
		try{
			UserView user = this.getUserView(request);
			
			String path = ConfigUtil.getConfigKey("USER_FACE_PATH") + "/" + user.getMaxaccept();
			Map<String, String> fileInfo = UploadUtil.uploadFile(file, path, request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("photoHead", user.getMaxaccept() + "/" + fileInfo.get("saveName"));
			param.put("maxaccept", user.getMaxaccept());
			personnelService.updatePersonnelByID(param);
			
			//更新缓存用户头像
			UserView userView = (UserView) request.getSession().getAttribute("userInfo");
			userView.setPhotoHead(user.getMaxaccept() + "/" + fileInfo.get("saveName"));
			request.getSession().setAttribute("userInfo", userView);
			
			result.setResultData(user.getMaxaccept() + "/" + fileInfo.get("saveName"));
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
	@RequestMapping("/changeUserInfo")
	@ResponseBody
	public Result changeUserInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		String maxaccept = request.getParameter("maxaccept");
		String userName = request.getParameter("userName");
		String userPhone = request.getParameter("userPhone");
		String userAddress = request.getParameter("userAddress");
		
		Result result = new Result();
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", maxaccept);
			param.put("name", userName);
			param.put("phone", userPhone);
			param.put("address", userAddress);
			personnelService.updatePersonnelByID(param);
			
			UserView user = this.getUserView(request);
			user.setUserName(userName);
			user.setPhone(userPhone);
			user.setAddress(userAddress);
			request.getSession().setAttribute("userInfo", user);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 获取派发人员
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSendPersonList")
	@ResponseBody
	public List<Map<String, String>> getSendPersonList(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		try {
			Map<String, String> param = new HashMap<String, String>();
			UserView user = this.getUserView(request);
			param.put("deptCode", user.getDeptCode());
			userList = personnelService.getDeptPerson(param);
		} catch (Exception e) {
			logger.error(e);
		}
		return userList;
	}
	/**
	 * 人员分析
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getUserNum")
	@ResponseBody
	public Result getUserNum(@RequestParam("dateList[]") List<String> dateList, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		try {
			Map<String, String> param = new HashMap<String, String>();
			UserView user = this.getUserView(request);
			if("10202".equals(user.getRoleLevel()) || "10203".equals(user.getRoleLevel())){
				param.put("deptCode", user.getDeptCode());
			}
			
			List<Integer> list = new ArrayList<Integer>();
			for(String date : dateList){
				param.put("date", date);
				int num = personnelService.getUserNum(param);
				list.add(num);
			}
			result.setResultData(list);
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
}
