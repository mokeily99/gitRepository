package com.yl.transaction.code.controller;

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
import com.yl.common.user.pojo.UserView;
import com.yl.transaction.code.service.CodeService;

@Controller
@RequestMapping("/code")
public class CodeController extends BaseController{

	@Resource
	private CodeService codeService;
	
	/**
	 * 根据类型代码翻译
	 */
	@RequestMapping("/{code_key}/{code_id}")
	@ResponseBody
	public Map<String, String> getCodename(@PathVariable("code_key") String code_key,
			@PathVariable("code_id") String code_id,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, String> codeMap = codeService.getCodename(code_key,code_id);
		return codeMap;
	}
	/**
	 * 公共获取转换码方法
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/getCommonCode")
	public void getCommonCode(HttpServletRequest request, HttpServletResponse response, Model model, String codeKey, String codeIDS) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("codeKey", codeKey);
		param.put("codeIDS", codeIDS);
		List<Map<String, String>> orderStatus = codeService.getCommonCode(param);
		write(response, orderStatus);
	}
	/**
	 * 客户类型转换
	 * @param request
	 * @param response
	 * @param model
	 * @param codeKey
	 * @param codeIDS
	 */
	@RequestMapping("/getCustType")
	public void getCustType(HttpServletRequest request, HttpServletResponse response, Model model, String codeKey, String codeIDS) {
		UserView user = this.getUserView(request);
		Map<String, String> param = new HashMap<String, String>();
		param.put("codeKey", codeKey);
		param.put("codeIDS", codeIDS);
		param.put("deptCode", user.getDeptCode());
		List<Map<String, String>> orderStatus = codeService.getCommonCustTypeCode(param);
		write(response, orderStatus);
	}
	
	/**
	 * 加载下拉菜单
	 */
	@RequestMapping("/{codeType}")
	@ResponseBody
	public List<Map<String, String>> getDepttype(@PathVariable("codeType") String codeType,HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Map<String, String>> orderStatus = codeService.getCodeList(codeType);
		return orderStatus;
	}
}
