package com.yl.transaction.dept.controller;

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

import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.Result;
import com.yl.common.user.pojo.UserView;
import com.yl.common.util.DBUtil;
import com.yl.transaction.dept.service.DeptService;

@Controller
@RequestMapping("/dept")
public class DeptController extends BaseController{

	@Resource
	private DeptService deptService;
	
	@Resource
	private PublicDao publicDao;
	
	@RequestMapping("/deptList")
	@ResponseBody
	public List<Map<String, Object>> deptList(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		List<Map<String, Object>> dataList = null;
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			UserView user = this.getUserView(request);
			param.put("deptCode", user.getDeptCode());
			
			List<Map<String, Object>> xtDeptList = deptService.getPageDept(param);//系统管理总部门
			if(xtDeptList != null && xtDeptList.size()>0){
				dataList = new ArrayList<Map<String, Object>>();
				
				Map<String, Object> xtDept = xtDeptList.get(0);
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("parenID", xtDept.get("id"));
				
				List<Map<String, Object>> kfDeptList = deptService.getDeptListByPID(para);//客服总公司
				if(kfDeptList != null){
					for(int ix=0; ix<kfDeptList.size(); ix++){
						Map<String, Object> kfDept = kfDeptList.get(ix);
						para.put("parenID", kfDept.get("id"));
						
						List<Map<String, Object>> fgDeptList = deptService.getDeptListByPID(para);//分公司
						for(int iy=0; iy<fgDeptList.size(); iy++){
							Map<String, Object> fgDept = fgDeptList.get(iy);
							para.put("parenID", fgDept.get("id"));
							
							List<Map<String, Object>> whDeptList = deptService.getDeptListByPID(para);//维护站
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
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			dataList = new ArrayList<Map<String, Object>>();
		}
		return dataList;
	}
	/**
	 * 增加部门
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/addDept")
	@ResponseBody
	public Result addDept(HttpServletRequest request, HttpServletResponse response, Model model) {
		String pDeptCode = request.getParameter("add_dept_pid");
		String deptName = request.getParameter("add_dept_name");
		String deptDes = request.getParameter("add_dept_des");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("pDeptCode", pDeptCode);
			param.put("deptName", deptName);
			param.put("deptDes", deptDes);
			param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
			
			deptService.addDept(param);
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
