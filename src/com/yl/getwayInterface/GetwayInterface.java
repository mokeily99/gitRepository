package com.yl.getwayInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public Result saveRoleMenus(HttpServletRequest request, HttpServletResponse response, Model model) {
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
