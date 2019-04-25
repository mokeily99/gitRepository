package com.yl.common.user.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yl.common.controller.BaseController;
import com.yl.common.pojo.Result;
import com.yl.common.user.pojo.UserView;
import com.yl.common.user.service.UserService;
import com.yl.transaction.personnel.service.PersonnelService;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

	@Resource
	private UserService userService;
	
	@Resource
	private PersonnelService personnelService;
	
	@RequestMapping("/modPwd")
	@ResponseBody
	public Result modPwd(HttpServletRequest request, HttpServletResponse response, Model mode){
		String maxaccept = request.getParameter("maxaccept");
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try{
			UserView user = userService.getUserView(maxaccept);
			if(oldPwd.equals(user.getUserPwd())){
				Map<String, String> param = new HashMap<String, String>();
				param.put("maxaccept", maxaccept);
				param.put("pwd", newPwd);
				personnelService.updatePersonnelByID(param);
			}else{
				result.setResultCode("0001");
				result.setResultMsg("原密码输入错误！");
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		
		return result;
	}
	/**
	 * 头像上传
	 * @param request
	 * @param response
	 * @param mode
	 * @return
	 */
	@RequestMapping("/saveHead")
	@ResponseBody
	public Result saveHead(HttpServletRequest request, HttpServletResponse response, Model mode){
		String photoHead = request.getParameter("base64url");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try{
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", user.getMaxaccept());
			param.put("photoHead", photoHead);
			personnelService.updatePersonnelByID(param);
			
			//更新session中用户信息
			user.setPhotoHead(photoHead);
			request.getSession().setAttribute("userInfo", user);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		
		return result;
	}
}
