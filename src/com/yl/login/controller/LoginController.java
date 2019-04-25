package com.yl.login.controller;

import java.io.IOException;
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
import com.yl.common.user.pojo.User;
import com.yl.common.user.pojo.UserView;
import com.yl.login.service.LoginService;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
	@Resource
	private LoginService loginService;

	@RequestMapping("/userLogin")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		String userName = request.getParameter("username");
		String pwd = request.getParameter("password");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userName", userName);
		param.put("pwd", pwd);
		UserView user = loginService.getUserLogin( param);
		
		if(user != null){
			request.getSession().setAttribute("userInfo", user);
			model.addAttribute("user", user);
			response.sendRedirect("../transaction/main/index.jsp");
			//response.sendRedirect("../transaction/index/index.jsp");
			return null;
		}else{
			model.addAttribute("msg", "账号或密码错误");
			return "login";
		}
	}
	
	@RequestMapping("/phoneLogin")
	public String phoneLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		String userName = request.getParameter("username");
		String pwd = request.getParameter("password");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userName", userName);
		param.put("pwd", pwd);
		UserView user = loginService.getUserLogin(param);
		
		if(user != null && "11302".equals(user.getRoleLevel()) && "11201".equals(user.getDeptType())){
			request.getSession().setAttribute("userInfo", user);
			model.addAttribute("user", user);
			response.sendRedirect("../transaction/phoneApp/index/index.jsp");
			
			//标注用户登录
			Map<String, String> para = new HashMap<String, String>();
			para.put("maxaccept", user.getMaxaccept());
			para.put("isOnline", "0");
			loginService.updateUserOnline(para);
			return null;
		}else{
			model.addAttribute("msg", "账号或密码错误");
			return "/transaction/phoneApp/login";
		}
	}
	
	@RequestMapping("/cancel")
	public void cancel(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		//标注用户登录
		Map<String, String> para = new HashMap<String, String>();
		UserView user = this.getUserView(request);
		para.put("maxaccept", user.getMaxaccept());
		para.put("isOnline", "1");
		loginService.updateUserOnline(para);
		
		//移除session
		request.getSession().removeAttribute("userInfo");
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		write(response, result);
	}
	
	/**
	 * bky:修改密码01-04
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping("/editPWD")
	@ResponseBody
	public Result editPWD(HttpServletRequest request, HttpServletResponse response, Model model,
			String oldPw, String newPw, String newPwOk) throws IOException {
		/*String oldPwd = request.getParameter("old_pwd");
		String newPwd = request.getParameter("new_pwd");
		String newPwdOk = request.getParameter("new_pwd_ok");*/
		
		String oldPwd = oldPw;
		String newPwd = newPw;
		String newPwdOk = newPwOk;
		
		UserView user = this.getUserView(request);
		String maxaccept = user.getMaxaccept();
		//String userName = user.getUserName();
		
		Map<String, String> param = new HashMap<String, String>();
		Map<String, String> para = new HashMap<String, String>();
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			para.put("maxaccept", user.getMaxaccept());
			String pwd = loginService.getUserPwd(para);
			
			if(oldPwd.equals(pwd)){
				if((!"".equals(newPwd) || !newPwd.equals(null)) && newPwd.equals(newPwdOk)){
					param.put("maxaccept", maxaccept);
					param.put("newPwd", newPwd);
					
					loginService.updateUserPwd(param);
					
					result.setResultCode("0000");
					//result.setResultMsg("操作成功!");
				}else{
					result.setResultCode("7777");
					//result.setResultMsg("新密码与确认密码不一致!");
				}
			}else{
				result.setResultCode("8888");
				//result.setResultMsg("原始密码有误!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
}
