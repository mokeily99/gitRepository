package com.yl.transaction.smsManager.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.yl.common.util.DateUtils;
import com.yl.transaction.custManager.service.CustService;
import com.yl.transaction.senwords.service.SenwordsService;
import com.yl.transaction.smsManager.service.SMSService;

@Controller
@RequestMapping("/sms")
public class SMSController extends BaseController{

	@Resource
	private SMSService smsService;
	
	@Resource
	private SenwordsService senwordsService;
	
	@Resource
	private CustService custService;
	
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
	 * 获取黑名单信息
	 * @param blackPhone
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getBlackInfo")
	@ResponseBody
	public Result getBlackInfo(String blackPhone, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("blackPhone", blackPhone);
			List<Map<String, String>> blackList = smsService.getBlackList(param);
			result.setResultData(blackList);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
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
	/**
	 * 模板列表加载
	 * @param page
	 * @param limit
	 * @param mouldTitle
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/mouldList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> mouldList(Integer page, Integer limit, String mouldTitle, HttpServletRequest request, HttpServletResponse response, Model model){
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);
			
			UserView user = this.getUserView(request);
			param.put("deptCode",user.getDeptCode());
			param.put("mouldTitle",mouldTitle);
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> mouldList = smsService.getMouldList(param);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(mouldList);
			
			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(mouldList);
		}catch(Exception e){
			tableResult.setCode(1);
			tableResult.setMsg("数据加载失败！");
			tableResult.setCount(0);
			tableResult.setData(new ArrayList<Map<String, String>>());
		}
		return tableResult;
	}
	/**
	 * 获取模板信息
	 * @param mouldTitle
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getMouldInfo")
	@ResponseBody
	public List<Map<String, String>> getMouldInfo(String maxaccept, HttpServletRequest request, HttpServletResponse response, Model model){
		List<Map<String, String>> mouldList = null;
		try{
			Map<String, String> param = new HashMap<String, String>();
			
			UserView user = this.getUserView(request);
			param.put("deptCode",user.getDeptCode());
			param.put("maxaccept",maxaccept);
			
			mouldList = smsService.getMouldInfo(param);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return mouldList;
	}
	/**
	 * 模板新增
	 * @param add_mould_title
	 * @param add_mould_type
	 * @param add_mould_content
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/addMould")
	@ResponseBody
	public Result addMould(String add_mould_title, String add_mould_type, String add_mould_content, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			//敏感词过滤
			List<Map<String, String>> senwordsList = senwordsService.getSenWordsList(null);
			for(Map<String, String> map : senwordsList){
				String sen = map.get("SENSITIVE_WORDS");
				if(add_mould_content.replaceAll(" ", "").contains(sen)){
					result.setResultCode("0001");
					result.setResultMsg("添加失败！模板内容包含敏感词:"+sen);
				}
			}
			
			if("0000".equals(result.getResultCode())){
				UserView user = this.getUserView(request);
				Map<String, String> param = new HashMap<String, String>();
				param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
				param.put("mouldTitle", add_mould_title);
				param.put("mouldType", add_mould_type);
				param.put("mouldContent", add_mould_content);
				param.put("oprID", user.getMaxaccept());
				param.put("oprName", user.getUserName());
				param.put("deptCode", user.getDeptCode());
				smsService.addMould(param);
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 模板修改
	 * @param edit_mould_id
	 * @param edit_mould_title
	 * @param edit_mould_content
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/editMould")
	@ResponseBody
	public Result editMould(String edit_mould_id, String edit_mould_title, String edit_mould_content, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Result result = new Result();
		try{
			//敏感词过滤
			List<Map<String, String>> senwordsList = senwordsService.getSenWordsList(null);
			for(Map<String, String> map : senwordsList){
				String sen = map.get("SENSITIVE_WORDS");
				if(edit_mould_content.replaceAll(" ", "").contains(sen)){
					result.setResultCode("0001");
					result.setResultMsg("修改失败！模板内容包含敏感词:"+sen);
				}
			}
			
			if("0000".equals(result.getResultCode())){
				UserView user = this.getUserView(request);
				Map<String, String> param = new HashMap<String, String>();
				param.put("maxaccept", edit_mould_id);
				param.put("mouldTitle", edit_mould_title);
				param.put("mouldContent", edit_mould_content);
				param.put("oprID", user.getMaxaccept());
				param.put("oprName", user.getUserName());
				param.put("deptCode", user.getDeptCode());
				smsService.editMould(param);
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 模板删除
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/delMould")
	@ResponseBody
	public Result delMould(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		String ids = request.getParameter("ids");
		try {
			if(ids.contains(",")){
				ids = ids.substring(0, ids.length()-1);
			}
			smsService.delMouldInIDS(ids);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 短息发送存储
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/addSendSms")
	@ResponseBody
	public Result addSendSms(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		String ids = request.getParameter("cust_ids");
		String sendType = request.getParameter("send_sms_type");
		String sendDate = request.getParameter("send_sms_date");
		String smsContent = request.getParameter("send_sms_content");
		try {
			if(ids.contains(",")){
				ids = ids.substring(0, ids.length()-1);
			}
			String[] idArr = ids.split(",");
			for(int ix=0; ix<idArr.length; ix++){
				String custID = idArr[ix];
				Map<String, String> param = new HashMap<String, String>();
				param.put("custID",custID);
				Map<String, String> custInfo = custService.getCustInfo(param);
				if(custInfo != null){
					UserView user = this.getUserView(request);
					param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
					param.put("custName", custInfo.get("CUST_NAME"));
					param.put("phone", custInfo.get("CONN_PHONE"));
					param.put("oprID", user.getMaxaccept());
					param.put("oprName", user.getUserName());
					param.put("deptCode", user.getDeptCode());
					if("10501".equals(sendType)){
						param.put("sendDate", DateUtils.dateFormat(new Date(), DateUtils.DATE_PATTERN));
					}else{
						param.put("sendDate", sendDate);
					}
					param.put("smsContent", smsContent);
					smsService.insertSmsInfo(param);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 短信列表查询
	 * @param page
	 * @param limit
	 * @param smsPhone
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/smsList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> smsList(Integer page, Integer limit, String smsPhone, HttpServletRequest request, HttpServletResponse response, Model model){
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);
			
			UserView user = this.getUserView(request);
			param.put("smsPhone",smsPhone);
			param.put("deptCode",user.getDeptCode());
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> smsList = smsService.getSmsList(param);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(smsList);
			
			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(smsList);
		}catch(Exception e){
			tableResult.setCode(1);
			tableResult.setMsg("数据加载失败！");
			tableResult.setCount(0);
			tableResult.setData(new ArrayList<Map<String, String>>());
		}
		return tableResult;
	}
	/**
	 * 
	 * @param page
	 * @param limit
	 * @param smsPhone
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/smsHis")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> smsHis(Integer page, Integer limit, String smsPhone, String sendFlag, HttpServletRequest request, HttpServletResponse response, Model model){
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);
			
			UserView user = this.getUserView(request);
			param.put("smsPhone",smsPhone);
			param.put("sendFlag",sendFlag);
			param.put("deptCode",user.getDeptCode());
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> smsList = smsService.getSmsHisList(param);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(smsList);
			
			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(smsList);
		}catch(Exception e){
			tableResult.setCode(1);
			tableResult.setMsg("数据加载失败！");
			tableResult.setCount(0);
			tableResult.setData(new ArrayList<Map<String, String>>());
		}
		return tableResult;
	}
}
