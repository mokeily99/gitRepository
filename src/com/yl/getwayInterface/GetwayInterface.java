package com.yl.getwayInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.util.StringUtil;
import com.mysql.jdbc.StringUtils;
import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.Result;
import com.yl.common.util.JsonUtils;
import com.yl.transaction.conver.service.ConverService;
import com.yl.transaction.personnel.service.PersonnelService;

@Controller
@RequestMapping("/getwayInterface")
public class GetwayInterface extends BaseController {

	@Resource
	private PersonnelService personnelService;

	@Resource
	private ConverService converService;

	@Resource
	private PublicDao publicDao;

	@RequestMapping(value = "report", consumes = "application/json;charset=utf-8", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, String> report(HttpServletRequest request, HttpServletResponse response, Model model, @RequestBody Map<String, Object> requestMap) {
		String event = (String) requestMap.get("event");
		String callid = (String) requestMap.get("callid");
		logger.info(callid + "事件event==============" + event + " BEGIN===");
		logger.info(callid + "全部参数==============" + JsonUtils.toJsonObj(requestMap));
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("status", "success");
		
		try {
			if ("RING".equals(event)) {
				Map<String, String> trunk = (Map<String, String>) requestMap.get("callertrunk");
				if(trunk == null){
					trunk = (Map<String, String>) requestMap.get("calledtrunk");
				}
				// 通话参数
				String from = trunk.get("from");// 主叫
				String to = trunk.get("to");// 被叫
				String talkFlag = "0";// 接通状态，默认未接通
				String callForward = "";// 呼叫方向
				String callID = (String) callid;// 通话唯一标识
				String seatID = "";// 坐席编码
				String seatName = "";// 坐席名称
				String deptCode = "";//所属部门

				// 判断呼叫方向
				Map<String, String> param = new HashMap<String, String>();
				param.put("seatNO", from);
				List<Map<String, String>> fromList = personnelService.isMySeat(param);
				param.put("seatNO", to);
				List<Map<String, String>> toList = personnelService.isMySeat(param);

				if (fromList.size() > 0) {
					callForward = "1";

					Map<String, String> seatInfo = fromList.get(0);
					seatID = seatInfo.get("MAXACCEPT");
					seatName = seatInfo.get("USER_NAME");
					deptCode = seatInfo.get("DEPT_CODE");
				} else if (toList.size() > 0) {
					callForward = "0";

					Map<String, String> seatInfo = toList.get(0);
					seatID = seatInfo.get("MAXACCEPT");
					seatName = seatInfo.get("USER_NAME");
					deptCode = seatInfo.get("DEPT_CODE");
				} else {// 垃圾数据
					return null;
				}

				// 首次插入通话内容
				param.put("maxaccept", publicDao.getMaxaccept());
				param.put("callerNO", from);
				param.put("calledNO", to);
				param.put("talkFlag", talkFlag);
				param.put("callForward", callForward);
				param.put("callID", callID);
				param.put("seatID", seatID);
				param.put("seatName", seatName);
				param.put("deptCode", deptCode);

				converService.insertConver(param);
				
			} else if ("ANSWER".equals(event)) {// 设置接通状态
				List<Map<String, String>> converList = converService.getConverByCallID(callid);

				if (converList.size() > 0) {
					// 修改通话状态
					Map<String, String> conver = converList.get(0);
					converService.updateTalkFlag(conver.get("MAXACCEPT"));
					
					
					/**插入呼叫状态表**/
					String callForward = conver.get("CALL_FORWARD");
					String phone = "";
					Map<String, String> param = new HashMap<String, String>();
					if("0".equals(callForward)){
						phone = conver.get("CALLER_NO");
					}else{
						phone = conver.get("CALLED_NO");
					}
					
					param.put("phone", phone);
					param.put("showFlag", "0");
					param.put("seatID", conver.get("SEAT_ID"));
					
					//判断坐席状态数据是否存在
					List<Map<String, String>> callLsit = converService.getCallStatusBySeatID(conver.get("SEAT_ID"));
					if(callLsit.size()>0){
						param.put("maxaccept", callLsit.get(0).get("MAXACCEPT"));
						converService.updateCallStatus(param);
					}else{
						param.put("maxaccept", publicDao.getMaxaccept());
						converService.insertCallStatus(param);
					}
					
				} else {// 垃圾数据
					return null;
				}
			} else if ("CALLFAILED".equals(event) || "HANGUP".equals(event)) {// 设置挂机方
				List<Map<String, String>> converList = converService.getConverByCallID(callid);

				if (converList.size() > 0) {
					// 判断是否已设置挂断方
					Map<String, String> conver = converList.get(0);
					if (StringUtil.isEmpty(conver.get("HANGUP_TAR"))) {
						// 设置挂断方
						Map<String, String> param = new HashMap<String, String>();
						param.put("maxaccept", conver.get("MAXACCEPT"));
						param.put("hangupTar", "caller".equals((String)requestMap.get("originator")) ? "0" : "1");
						converService.updateHangupTar(param);
					} else {
						return null;
					}

				} else {// 垃圾数据
					return null;
				}
			} else if ("CDR".equals(event)) {
				List<Map<String, String>> converList = converService.getConverByCallID(callid);

				if (converList.size() > 0) {
					// 修改通话状态
					Map<String, String> conver = converList.get(0);
					Map<String, String> param = new HashMap<String, String>();
					param.put("maxaccept", conver.get("MAXACCEPT"));
					param.put("callTime", (String) requestMap.get("calltime"));
					param.put("answerTime", (String) requestMap.get("answertime"));
					param.put("hangupTime", (String) requestMap.get("hanguptime"));
					param.put("talkTime", (String) requestMap.get("talktime"));
					param.put("downloadPath", (String) requestMap.get("download_path"));

					converService.finashConver(param);

				} else {// 垃圾数据
					return null;
				}
			} else {// 垃圾数据
				return null;
			}

			logger.info(callid + "记录event==============" + event + " END===成功！");
		} catch (Exception e) {
			logger.info(callid + "记录event==============" + event + " END===失败！");
			logger.error(e.getMessage(), e);
			result.put("status", "fail");
		}
		return result;
	}

	/**
	 * 坐席置忙
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/agentBreak")
	@ResponseBody
	public Result agentBreak(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String event = request.getParameter("event");

		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		String str = "null";
		if (StringUtil.isEmpty(str)) {
			System.out.print("==============");
		}

	}
}
