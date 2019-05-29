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
}
