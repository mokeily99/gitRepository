package com.yl.transaction.conver.controller;

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
import com.yl.transaction.conver.service.ConverService;
import com.yl.transaction.dept.service.DeptService;

@Controller
@RequestMapping("/conver")
public class ConverController extends BaseController{

	@Resource
	private ConverService converService;
	
	@RequestMapping("/getConverList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> getConverList(Integer page, Integer limit, String callerPhone, String calledPhone, HttpServletRequest request, HttpServletResponse response, Model model) {
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try {
			UserView user = this.getUserView(request);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);

			param.put("callerPhone", callerPhone);
			param.put("calledPhone", calledPhone);
			
			if("10203".equals(user.getRoleLevel())){//分销商工作人员只能看自己的通话
				param.put("seatID", user.getMaxaccept());
			}else if("10202".equals(user.getRoleLevel())){//分销商管理员看自己部门所有通话
				param.put("deptCode", user.getDeptCode());
			}

			PageHelper.startPage(page, limit);
			List<Map<String, String>> converList = converService.getConverList(param);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(converList);

			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(converList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			tableResult.setCode(1);
			tableResult.setMsg("数据加载失败！");
			tableResult.setCount(0);
			tableResult.setData(new ArrayList<Map<String, String>>());
		}
		return tableResult;
	}
}
