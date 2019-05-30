package com.yl.transaction.order.controller;

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
import com.github.pagehelper.util.StringUtil;
import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.LayTableResult;
import com.yl.common.pojo.Result;
import com.yl.common.pojo.TableResult;
import com.yl.common.user.pojo.UserView;
import com.yl.common.user.service.UserService;
import com.yl.common.util.DBUtil;
import com.yl.common.util.DateUtils;
import com.yl.transaction.dept.service.DeptService;
import com.yl.transaction.order.service.OrderService;
import com.yl.transaction.personnel.service.PersonnelService;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

	@Resource
	private OrderService orderService;
	
	@Resource
	private DeptService deptService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private PublicDao publicDao;

	@Resource
	private PersonnelService personnelService;

	private String[][] content;

	
	@RequestMapping("/orderPageList")
	@ResponseBody
	public TableResult<List<Map<String, Object>>> orderPageList(Integer pageSize, Integer pageIndex, HttpServletRequest request, HttpServletResponse response, Model model) {
		String deptCode = request.getParameter("deptCode");
		String oprID = request.getParameter("oprID");
		String orderStatus = request.getParameter("orderStatus");
		String projectType = request.getParameter("projectType");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		
		TableResult<List<Map<String, Object>>> tableResult = new TableResult<List<Map<String, Object>>>();
		try{
			Map<String, String> para = new HashMap<String, String>();
			para.put("maxaccept", deptCode);
			List<Map<String, String>> deptList = deptService.getSonDept(para);
			String deptCodes = "";
			for(Map<String, String> map : deptList){
				deptCodes = deptCodes + map.get("MAXACCEPT") + ",";
			}
			if(StringUtil.isNotEmpty(deptCodes)){
				deptCodes = deptCodes.substring(0, deptCodes.length()-1);
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("row", pageSize);
			param.put("page", pageIndex);
			param.put("deptCode", deptCodes);
			param.put("orderOprID", oprID);
			param.put("orderStatus", orderStatus);
			param.put("projectType", projectType);
			param.put("orderAbleFlag", "10900");
			if(StringUtil.isNotEmpty(beginTime)){
				param.put("beginTime", DateUtils.dateParse(beginTime+" 00:00:00", DateUtils.DATE_TIME_PATTERN));
			}
			if(StringUtil.isNotEmpty(beginTime)){
				param.put("endTime", DateUtils.dateParse(endTime+" 23:59:59", DateUtils.DATE_TIME_PATTERN));
			}
			
			PageHelper.startPage(pageIndex, pageSize);
			List<Map<String, Object>> orderlList = orderService.getPageOrder(param);
			PageInfo<Map<String, Object>> pageinfo = new PageInfo<Map<String, Object>>(orderlList);
			
			tableResult.setTotal((int) pageinfo.getTotal());
			tableResult.setRows(orderlList);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			tableResult.setTotal(0);
			tableResult.setRows(new ArrayList<Map<String, Object>>());
		}
		return tableResult;
	}
	/**
	 * 获取未发送工单
	 * @param pageSize
	 * @param pageIndex
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getPageUnsendOrderList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> getPageUnsendOrderList(Integer page, Integer limit, HttpServletRequest request, HttpServletResponse response, Model model) {
		String orderPhone = request.getParameter("order_phone");
		String custName = request.getParameter("cust_name");
		String sendFlag = request.getParameter("send_flag");
		
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> para = new HashMap<String, Object>();
			UserView user = this.getUserView(request);
			if("10202".equals(user.getRoleLevel())){//部门管理员看本部门
				para.put("deptCode", user.getDeptCode());
			}else if("10203".equals(user.getRoleLevel())){//部门人员看自己的
				para.put("oprID", user.getMaxaccept());
			}
			para.put("orderPhone", orderPhone);
			para.put("custName", custName);
			para.put("sendFlag", sendFlag);
			
			para.put("row", limit);
			para.put("page", page);
			
			PageHelper.startPage(page, limit);
			List<Map<String, String>> orderList = orderService.getOrderList(para);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(orderList);
			
			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(orderList);
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
	 * 工单存储
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveOrder")
	@ResponseBody
	public Result saveOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
		String custName = request.getParameter("cust_name");
		String connPhone = request.getParameter("conn_phone");
		String custAdr = request.getParameter("cust_address");
		String markContent = request.getParameter("order_mark_content");
		String isSend = request.getParameter("is_send");
		String sendOpr = request.getParameter("order_send_opr");
		
		Result result = new Result();
		try {
			Map<String, String> param = new HashMap<String, String>();
			
			if("1".equals(isSend) && !StringUtil.isEmpty(sendOpr)){
				Map<String, String> personnel = personnelService.getPersonnelByMax(sendOpr);
				param.put("sendOprID", sendOpr);
				param.put("sendOprName", personnel.get("USER_NAME"));
				param.put("sendDeptCode", personnel.get("DEPT_CODE"));
				param.put("sendDeptName", personnel.get("DEPT_NAME"));
			}
			UserView user = this.getUserView(request);
			String maxaccept = DBUtil.getMaxAccept();
			param.put("maxaccept", maxaccept);
			param.put("custName", custName);
			param.put("connPhone", connPhone);
			param.put("custAdr", custAdr);
			param.put("markContent", markContent);
			param.put("isSend", isSend);
			param.put("oprID", user.getMaxaccept());
			param.put("oprName", user.getUserName());
			param.put("oprDeptID", user.getDeptCode());
			param.put("oprDeptName", user.getDeptName());
			//存储工单
			orderService.saveInitOrder(param);
			
			if("1".equals(isSend)){
				orderService.saveOrderList(param);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 工单修改
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/editOrderList")
	@ResponseBody
	public Result editOrderList(HttpServletRequest request, HttpServletResponse response, Model model) {
		String orderID = request.getParameter("edit_order_id");
		String custName = request.getParameter("edit_cust_name");
		String connPhone = request.getParameter("edit_conn_phone");
		String connAdr = request.getParameter("edit_conn_adr");
		String markContent = request.getParameter("edit_mark_content");
		
		Result result = new Result();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("maxaccept", orderID);
			param.put("custName", custName);
			param.put("connPhone", connPhone);
			param.put("connAdr", connAdr);
			param.put("markContent", markContent);
			orderService.updateOrderInfo(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 工单派发
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/sendOrder")
	@ResponseBody
	public Result sendOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
		String ids = request.getParameter("send_order_ids");
		String sendOprID = request.getParameter("order_send_opr");
		String sendMark = request.getParameter("send_mark_content");
		
		Result result = new Result();
		try {
			if(ids.contains(",")){
				ids = ids.substring(0, ids.length()-1);
			}
			
			Map<String, String> personnel = personnelService.getPersonnelByMax(sendOprID);
			Map<String, String> param = new HashMap<String, String>();
			param.put("sendOprID", sendOprID);
			param.put("sendOprName", personnel.get("USER_NAME"));
			param.put("sendOprDeptID", personnel.get("DEPT_CODE"));
			param.put("sendOprDeptName", personnel.get("DEPT_NAME"));
			param.put("ids", ids);
			param.put("markContent", sendMark);
			orderService.sendOrder(param);
			
			//记录工单记录表
			orderService.insertOrderList(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
}
