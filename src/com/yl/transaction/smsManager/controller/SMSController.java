package com.yl.transaction.smsManager.controller;

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
import com.yl.transaction.smsManager.service.SMSService;

@Controller
@RequestMapping("/sms")
public class SMSController extends BaseController{

	@Resource
	private SMSService smsService;
	
	@Resource
	private PublicDao publicDao;
	
	@RequestMapping("/blackList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> blackList(Integer page, Integer limit, String blackPhone, HttpServletRequest request, HttpServletResponse response, Model model){
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);
			
			param.put("blackPhone",blackPhone);
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> blackList = smsService.getBlackList(param);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(blackList);
			
			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(blackList);
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
	@RequestMapping("/changeBlackStatus")
	@ResponseBody
	public Result changeBlackStatus(String maxaccept, String ableFlag, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", maxaccept);
			param.put("ableFlag", ableFlag);
			smsService.updateBlackStatus(param);
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
	@RequestMapping("/addBlackList")
	@ResponseBody
	public Result addBlackList(String add_black_phone, String add_black_reason, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
			param.put("blackPhone", add_black_phone);
			param.put("blackReason", add_black_reason);
			param.put("oprID", user.getMaxaccept());
			param.put("ableFlag", "10101");
			smsService.addBlackList(param);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 黑名单修改
	 * @param edit_senwords
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/editBlackList")
	@ResponseBody
	public Result editBlackList(String edit_black_id, String edit_black_phone, String edit_black_reason, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", edit_black_id);
			param.put("blackPhone", edit_black_phone);
			param.put("blackReason", edit_black_reason);
			param.put("oprID", user.getMaxaccept());
			smsService.updateBlackList(param);
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
	@RequestMapping("/delBlackList")
	@ResponseBody
	public Result delBlackList(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		String ids = request.getParameter("ids");
		try {
			if(ids.contains(",")){
				ids = ids.substring(0, ids.length()-1);
			}
			smsService.delBlackListInIDS(ids);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
}
