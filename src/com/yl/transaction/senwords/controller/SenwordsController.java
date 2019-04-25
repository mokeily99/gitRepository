package com.yl.transaction.senwords.controller;

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
import com.yl.transaction.senwords.service.SenwordsService;

@Controller
@RequestMapping("/senwords")
public class SenwordsController extends BaseController{

	@Resource
	private SenwordsService senwordsService;
	
	@Resource
	private PublicDao publicDao;
	
	@RequestMapping("/senwordsList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> senwordsList(Integer page, Integer limit, String senwords, HttpServletRequest request, HttpServletResponse response, Model model){
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);
			
			param.put("senwords",senwords);
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> personnelList = senwordsService.getSenWordsList(param);
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
	/**
	 * 修改敏感词状态
	 * @param deptCode
	 * @param deptType
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/changeSenwordsStatus")
	@ResponseBody
	public Result changeSenwordsStatus(String maxaccept, String ableFlag, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", maxaccept);
			param.put("ableFlag", ableFlag);
			senwordsService.updateSenwordsStatus(param);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 敏感词新增
	 * @param senwords
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/addSenwords")
	@ResponseBody
	public Result addSenwords(String add_senwords, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
			param.put("senwords", add_senwords);
			param.put("oprID", user.getMaxaccept());
			param.put("ableFlag", "10101");
			senwordsService.addSenwords(param);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 敏感词修改
	 * @param edit_senwords
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/editSenwords")
	@ResponseBody
	public Result editSenwords(String edit_senwords_id, String edit_senwords, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", edit_senwords_id);
			param.put("senwords", edit_senwords);
			param.put("oprID", user.getMaxaccept());
			senwordsService.updateSenwords(param);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/delSenwords")
	@ResponseBody
	public Result delSenwords(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		String ids = request.getParameter("ids");
		try {
			if(ids.contains(",")){
				ids = ids.substring(0, ids.length()-1);
			}
			senwordsService.delSenwordsInIDS(ids);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
}
