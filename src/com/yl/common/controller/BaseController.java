package com.yl.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.yl.common.pojo.Result;
import com.yl.common.user.pojo.User;
import com.yl.common.user.pojo.UserView;
import com.yl.common.user.service.UserService;
import com.yl.common.util.JsonUtils;


public class BaseController{

	public Logger logger = Logger.getLogger(this.getClass().getName());
	
	public Result result = new Result();
	@Resource
	private UserService userService;
	
	public UserView getUserView(HttpServletRequest request){
		UserView user = (UserView)request.getSession().getAttribute("userInfo");
		return user;
	}
	/**
	 * 公共写出方法
	 * @param response
	 * @param job
	 * @return
	 */
	public void write(HttpServletResponse response, Object obj){
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally {
			writer.println(JsonUtils.toJsonArr(obj));
			writer.flush();
			writer.close();
		}
	}
	/**
	 * 分页写出
	 * @param response
	 * @param obj
	 */
	public void pageWrite(HttpServletResponse response, Object obj){
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally {
			writer.println(JsonUtils.toJsonObj(obj));
			writer.flush();
			writer.close();
		}
	}
	/**
	 * 直接写出不区分格式
	 * @param response
	 * @param obj
	 */
	public void allWrite(HttpServletResponse response, Object obj){
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally {
			writer.println(obj);
			writer.flush();
			writer.close();
		}
	}
	
}
