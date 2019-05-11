package com.yl.getwayInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yl.common.controller.BaseController;
import com.yl.common.pojo.Result;
import com.yl.common.user.pojo.UserView;
import com.yl.transaction.code.service.CodeService;

@Controller
@RequestMapping("/getwayInterface")
public class GetwayInterface extends BaseController{

	@Resource
	private CodeService codeService;
	
	@RequestMapping("/report")
	@ResponseBody
	public Map<String, String> report(@RequestParam Map<String,String> params) {
		logger.error("=============="+params);
		Iterator<Entry<String, String>> entries = params.entrySet().iterator(); 
		while (entries.hasNext()) { 
		  Map.Entry<String, String> entry = entries.next(); 
		  logger.error("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
		}
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("status", "success");
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			
		}
		return result;
	}
	/**
	 * 坐席置忙
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/agentBreak")
	@ResponseBody
	public Result agentBreak(HttpServletRequest request, HttpServletResponse response, Model model) {
		String event = request.getParameter("event");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
}
