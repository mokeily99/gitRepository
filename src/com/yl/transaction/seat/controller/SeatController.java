package com.yl.transaction.seat.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.LayTableResult;
import com.yl.common.pojo.Result;
import com.yl.common.user.pojo.UserView;
import com.yl.common.util.ConfigUtil;
import com.yl.common.util.DBUtil;
import com.yl.common.util.ExcelUtil;
import com.yl.common.util.HttpUtil;
import com.yl.common.util.JsonUtils;
import com.yl.common.util.MD5Utils;
import com.yl.common.util.UploadUtil;
import com.yl.transaction.code.service.CodeService;
import com.yl.transaction.custManager.service.CustService;
import com.yl.transaction.seat.service.SeatService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/seat")
public class SeatController extends BaseController {

	@Resource
	private SeatService seatService;

	@Resource
	private PublicDao publicDao;
	
	/**
	 * 登录验证
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/loginVal")
	@ResponseBody
	public Result loginVal(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("username", ConfigUtil.getConfigKey("API_USER_NAME"));
			param.put("password", MD5Utils.MD5Encode(ConfigUtil.getConfigKey("API_PWD"), "utf-8"));
			param.put("url", ConfigUtil.getConfigKey("API_ACCEPT_URL"));
			String para = JsonUtils.toJsonObj(param);
			JSONObject resp = HttpUtil.doPostJson(para, ConfigUtil.getConfigKey("SWITCH_URL")+"/API/login");
			
			if("success".equals(resp.optString("status"))){
				param.put("token", resp.optString("token"));
				seatService.updateSeatStatus(param);
			}else{
				result.setResultCode("0001");
				result.setResultMsg("API登录失败!");
			}
			
			/*UserView user = this.getUserView(request);
			param.put("seatID", user.getMaxaccept());
			List<Map<String, String>> seatList = seatService.getSeatFreeBusyInfo(param);
			if(seatList.size() > 0){
				result.setResultData(seatList.get(0));
			}*/
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}

	/**
	 * 获取闲忙状态
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSeatFreeBusy")
	@ResponseBody
	public Result getSeatFreeBusy(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		
		try {
			UserView user = this.getUserView(request);
			//从网关获取状态
			Map<String, String> param = new HashMap<String, String>();
			param.put("queueid", ConfigUtil.getConfigKey("QUEUEID"));
			param.put("extid", user.getSeatNO());
			JSONObject resp = HttpUtil.doPostJson(JsonUtils.toJsonObj(param), ConfigUtil.getConfigKey("SWITCH_URL")+"/API/agent_query?token="+publicDao.getToken());
			if("success".equals(resp.optString("status"))){
				String seatStatus = resp.optString("agentstatus");
				if("logged in".equals(seatStatus)){
					
				}
			}else{
				
			}
			param.put("seatID", user.getMaxaccept());
			List<Map<String, String>> seatList = seatService.getSeatFreeBusyInfo(param);
			if(seatList.size() > 0){
				result.setResultData(seatList.get(0));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	/**
	 * 置忙\置闲
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/setBusyFree")
	@ResponseBody
	public Result setBusy(String status, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();

		try {
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("seatID", user.getMaxaccept());
			param.put("status", status);
			List<Map<String, String>> seatList = seatService.getSeatFreeBusyInfo(param);
			if(seatList.size()>0){
				seatService.setBusyFree(param);
			}else{
				param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
				seatService.insertSeatInfo(param);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
}
