package com.yl.transaction.account.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.LayTableResult;
import com.yl.common.pojo.Result;
import com.yl.common.user.pojo.UserView;
import com.yl.common.util.DBUtil;
import com.yl.transaction.account.service.AccountService;
import com.yl.transaction.dept.service.DeptService;

@Controller
@RequestMapping("/account")
public class AccountController extends BaseController{

	@Resource
	private AccountService accountService;
	
	@Resource
	private DeptService deptService;
	
	@Resource
	private PublicDao publicDao;
	
	@RequestMapping("/accountList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> deptList(Integer page, Integer limit, String dept_name, HttpServletRequest request, HttpServletResponse response, Model model) {
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			UserView user = this.getUserView(request);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);
			param.put("deptCode", user.getDeptCode());
			param.put("deptName", dept_name);
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> accountList = accountService.getAccountList(param);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(accountList);
			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(accountList);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			tableResult.setCode(1);
			tableResult.setMsg("数据加载失败！");
			tableResult.setCount(0);
			tableResult.setData(new ArrayList<Map<String, String>>());
		}
		return tableResult;
	}
	/**
	 * 修改账户金额
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/setAccountBalance")
	@ResponseBody
	public Result setAccountBalance(String maxaccept, String balance, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			UserView user = this.getUserView(request);
			if("10201".equals(user.getRoleLevel())){
				Map<String, String> param = new HashMap<String, String>();
				param.put("maxaccept", maxaccept);
				param.put("balance", balance);
				
				accountService.updateAccountBalance(param);
			}else{
				result.setResultCode("0001");
				result.setResultMsg("权限不足无法操作！");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		
		return result;
	}
	/**
	 * 删除部门
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/delDept")
	@ResponseBody
	public Result delDept(HttpServletRequest request, HttpServletResponse response, Model model) {
		String ids = request.getParameter("ids");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			if(ids.contains(",")){
				ids = ids.substring(0, ids.length()-1);
			}
			deptService.delDeptInIDS(ids);
			deptService.delDeptInPIDS(ids);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 修改部门信息
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/modifyDept")
	@ResponseBody
	public Result modifyDept(HttpServletRequest request, HttpServletResponse response, Model model) {
		String maxaccept = request.getParameter("edit_dept_id");
		String deptName = request.getParameter("edit_dept_name");
		String deptDes = request.getParameter("edit_dept_des");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			Map<String, String> param = new HashMap<String, String>();
		
			param.put("maxaccept", maxaccept);
			param.put("deptName", deptName);
			param.put("deptDes", deptDes);
			deptService.updateDeptByID(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 获取全部部门信息
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/getAllDept")
	public void getAllDept(HttpServletRequest request, HttpServletResponse response, Model model) {
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		List<Map<String, Object>> dataList = null;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			UserView user = this.getUserView(request);
			param.put("deptCode", user.getDeptCode());
			
			List<Map<String, Object>> xtDeptList = deptService.getPageDept(param);//系统管理总部门
			if(xtDeptList != null && xtDeptList.size()>0){
				dataList = new ArrayList<Map<String, Object>>();
				
				Map<String, Object> xtDept = xtDeptList.get(0);
				xtDept.put("id", xtDept.get("MAXACCEPT"));
				xtDept.put("text", xtDept.get("DEPT_NAME"));
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("parenID", xtDept.get("MAXACCEPT"));
				
				List<Map<String, Object>> kfDeptList = deptService.getDeptTreeByPID(para);//客服总公司
				if(kfDeptList != null){
					for(int ix=0; ix<kfDeptList.size(); ix++){
						Map<String, Object> kfDept = kfDeptList.get(ix);
						para.put("parenID", kfDept.get("id"));
						
						List<Map<String, Object>> fgDeptList = deptService.getDeptTreeByPID(para);//分公司
						for(int iy=0; iy<fgDeptList.size(); iy++){
							Map<String, Object> fgDept = fgDeptList.get(iy);
							para.put("parenID", fgDept.get("id"));
							
							List<Map<String, Object>> whDeptList = deptService.getDeptTreeByPID(para);//维护站
							fgDept.put("children", whDeptList);
						}
						kfDept.put("children", fgDeptList);
					}
					xtDept.put("children", kfDeptList);
				}
				dataList.add(xtDept);
			}else{
				dataList = new ArrayList<Map<String, Object>>();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		write(response, dataList);
	}
	
	/**
	 * bky:获取所有基站10.15
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/getjzList")
	@ResponseBody
	public void getjzList(HttpServletRequest request, HttpServletResponse response, Model model, String maxaccept){
		List<Map<String, String>> jzList = new ArrayList<Map<String, String>>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("maxaccept", maxaccept);
		jzList = deptService.getjzList(param);
		
		Map<String, String> para = new HashMap<String, String>();
		String MAXACCEPT = "";
		String DEPT_NAME = "【空】";
		para.put("MAXACCEPT", MAXACCEPT);
		para.put("DEPT_NAME", DEPT_NAME);
		
		jzList.add(0, para);
		write(response, jzList);
	}
	
	@RequestMapping("/getDept")
	@ResponseBody
	public String getDpet(String deptId, HttpServletRequest request, HttpServletResponse response, Model model) {
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		String deptName = null;
		
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("deptId", deptId);
			deptName = deptService.getDept(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return deptName;
	}
	/**
	 * 获取子部门列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSonDept")
	@ResponseBody
	public Result getSonDept(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		List<Map<String, String>> deptList = null;
		try {
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", user.getDeptCode());
			deptList = deptService.getSonDept(param);
			
			result.setResultData(deptList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
}
