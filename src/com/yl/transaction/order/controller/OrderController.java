package com.yl.transaction.order.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.PageData;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.Result;
import com.yl.common.pojo.TableResult;
import com.yl.common.user.pojo.UserView;
import com.yl.common.user.service.UserService;
import com.yl.common.util.ConfigUtil;
import com.yl.common.util.DBUtil;
import com.yl.common.util.DateUtils;
import com.yl.common.util.ExcelUtil;
import com.yl.common.util.ImgUtil;
import com.yl.common.util.SMSUtil;
import com.yl.common.util.StrUtil;
import com.yl.transaction.dept.service.DeptService;
import com.yl.transaction.order.service.OrderService;
import com.yl.transaction.patientManager.service.PatientManagerService;

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
	private PatientManagerService patientManagerService;

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
	 * 获取工单信息
	 * @param maxaccept
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/orderList")
	@ResponseBody
	public Result orderList(String maxaccept, String ableFlag, String makeFlag, String orderStatus, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			param.put("orderAbleFlag", ableFlag);
			param.put("orderStatus", orderStatus);
			List<Map<String, Object>> orderList = orderService.getOrderPatientDet(param);
			for (Map<String, Object> map : orderList) {
				Clob clob = (Clob) map.get("A_PATIENT_SIGN_PHONE");
				if(clob != null){
					Reader inReader = clob.getCharacterStream();
					BufferedReader br = new BufferedReader(inReader);
					String s = br.readLine();
					StringBuffer sb = new StringBuffer();
					while (s != null) {
						sb.append(s);
						s = br.readLine();
					}
					map.put("A_PATIENT_SIGN_PHONE", sb);
				}
			}
			result.setResultData(orderList);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	
	@RequestMapping("/getOprOrderList")
	@ResponseBody
	public Result getOprOrderList(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		String orderStatus = request.getParameter("orderStatus");
		String ableFlag = request.getParameter("ableFlag");
		try {
			UserView user = this.getUserView(request);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("oprID", user.getMaxaccept());
			param.put("orderStatus", orderStatus);
			param.put("ableFlag", ableFlag);
			List<Map<String, String>> orderList = orderService.getOrderByStatusAndAble(param);
			result.setResultData(orderList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}

	/**
	 * 验证码发送
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/sendCode")
	@ResponseBody
	public Result sendCode(HttpServletRequest request, HttpServletResponse response, Model model) {
		String phone = request.getParameter("phone");
		String patientID = request.getParameter("patientID");
		String patientName = request.getParameter("patientName");
		String maxaccept = request.getParameter("maxaccept");

		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			UserView user = this.getUserView(request);
			int code = (int) ((Math.random() * 9 + 1) * 1000);
			StringBuffer message = new StringBuffer();
			message.append("尊敬的").append(patientName).append("您好，");
			message.append("康复师：").append(user.getUserName()).append("将为您进行康复治疗，");
			message.append("回填验证码：").append(code).append("，当天有效。请当面告知康复师！【社区康复中心】");

			// 发送短信
		    SMSUtil.sendSMS(phone, message.toString());

			// 插入记录表和验证码表
			DBUtil.insertSMS(publicDao, user.getMaxaccept(), phone, message.toString(), patientID, code + "", maxaccept);

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}

	/**
	 * 取消预约
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/cannelMake")
	@ResponseBody
	public Result cannelMake(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		String maxaccept = request.getParameter("maxaccept");
		String patientID = request.getParameter("patientID");
		try {
			// 修改订单取消预约
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
//			param.put("ableFlag", "10901");
			param.put("orderStatus", "10602");
			orderService.updateOrderInfo(param);

			// 患者预约状态置为已完成
			param.put("maxaccept", patientID);
			param.put("makeFlag", "10702");
			patientManagerService.updatePatientByParam(param);
			
			// 患者预约状态置为已完成
			UserView user = this.getUserView(request);
			param.put("maxaccept", patientID);
			param.put("oprID", user.getMaxaccept());
			param.put("ableFlag", "10901");
			patientManagerService.updateBasePatient(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}

	/**
	 * 获取工单、患者、病患类型详情
	 * 
	 * @param maxaccept
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getOrderPatientDet")
	@ResponseBody
	public Result getOrderPatientDet(String maxaccept, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			param.put("orderAbleFlag", "10900");
			param.put("patientAbleFlag", "10900");
			param.put("makeFlag", "10700,10701");
			List<Map<String, Object>> orderList = orderService.getOrderPatientDet(param);
			for (Map<String, Object> map : orderList) {
				Clob clob = (Clob) map.get("A_PATIENT_SIGN_PHONE");
				if(clob != null){
					Reader inReader = clob.getCharacterStream();
					BufferedReader br = new BufferedReader(inReader);
					String s = br.readLine();
					StringBuffer sb = new StringBuffer();
					while (s != null) {
						sb.append(s);
						s = br.readLine();
					}
					map.put("A_PATIENT_SIGN_PHONE", sb);
				}
			}
			result.setResultData(orderList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	
	@RequestMapping("/uploadSignName")
	@ResponseBody
	public Result uploadSignName(HttpServletRequest request, HttpServletResponse response, Model model) {
		String maxaccept = request.getParameter("maxaccept");
		String imgBase64 = request.getParameter("imgBase64");
		
		System.out.println(imgBase64);
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			param.put("imgBase64", imgBase64);
			orderService.updateOrderInfo(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	
	@RequestMapping("/createBasePatient")
	@ResponseBody
	public Result createBasePatient(String patientID, String orderID, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		try{
			List<Map<String, Object>> baseList = null;
			
			UserView user = this.getUserView(request);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", orderID);
			param.put("patientID", patientID);
			param.put("oprID", user.getMaxaccept());
			param.put("oprName", user.getUserName());
			param.put("orderAbleFlag", "10900");
			param.put("patientAbleFlag", "10900");
			baseList = orderService.getOrderBasePatientDet(param);
			if(baseList == null || baseList.size() < 1){
				patientManagerService.insertBasePatientInfo(param);
				baseList = orderService.getOrderBasePatientDet(param);
			}
			
			for (Map<String, Object> map : baseList) {
				Clob clob = (Clob) map.get("A_PATIENT_SIGN_PHONE");
				if(clob != null){
					Reader inReader = clob.getCharacterStream();
					BufferedReader br = new BufferedReader(inReader);
					String s = br.readLine();
					StringBuffer sb = new StringBuffer();
					while (s != null) {
						sb.append(s);
						s = br.readLine();
					}
					map.put("A_PATIENT_SIGN_PHONE", sb);
				}
			}
			result.setResultData(baseList.get(0));
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
	@RequestMapping("/saveOrderPatient")
	@ResponseBody
	public Result saveOrderPatient(HttpServletRequest request, HttpServletResponse response, Model model) {
		String saveFlag = request.getParameter("sub_type");
		String orderID = request.getParameter("order_id");
		String patientID = request.getParameter("patient_id");
		String patientName = request.getParameter("patient_name");
		String patientSex = request.getParameter("patient_sex");
		String patientNation = request.getParameter("patient_nation");
		String cardID = request.getParameter("card_id");
		String patientPhone = request.getParameter("patient_phone");
		String streetComm = request.getParameter("street_comm");
		String patientAddress = request.getParameter("patient_address");
		String patientPathogeny = request.getParameter("patient_pathogeny");
		String nowPresent = request.getParameter("now_present");
		String hisPresent = request.getParameter("his_present");
		String cCardID = request.getParameter("c_card_id");
		String cCardSub = request.getParameter("c_card_sub");
		String patientType = request.getParameter("patient_type");
		String guName = request.getParameter("gu_name");
		String guPhone = request.getParameter("gu_phone");
		String guRelative = request.getParameter("gu_relative");
		String cardPositive = request.getParameter("card_positive_val");
		String cardOther = request.getParameter("card_other_val");
		String cCardIndex = request.getParameter("c_card_index_val");
		String cCardPhoto = request.getParameter("c_card_photo_val");
		String hbIndex = request.getParameter("hb_index_val");
		String hbSelf = request.getParameter("hb_self_val");
		String togetherPhoto = request.getParameter("together_photo_val");
		String kfBeforePhoto = request.getParameter("kf_before_photo_val");
		String kfMiddlePhoto = request.getParameter("kf_middle_photo_val");
		String kfEndPhoto = request.getParameter("kf_end_photo_val");
		String projectType = request.getParameter("project_type");
		String payType = request.getParameter("pay_type");
		String payFee = request.getParameter("pay_fee");
		String codeError = request.getParameter("code_error_mark");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			/**照片值为0默认未改变**/
			if("0".equals(cardPositive)){
				cardPositive = "";
			}
			if("0".equals(cardOther)){
				cardOther = "";
			}
			if("0".equals(cCardIndex)){
				cCardIndex = "";
			}
			if("0".equals(cCardPhoto)){
				cCardPhoto = "";
			}
			if("0".equals(hbIndex)){
				hbIndex = "";
			}
			if("0".equals(hbSelf)){
				hbSelf = "";
			}
			if("0".equals(togetherPhoto)){
				togetherPhoto = "";
			}
			if("0".equals(kfBeforePhoto)){
				kfBeforePhoto = "";
			}
			if("0".equals(kfMiddlePhoto)){
				kfMiddlePhoto = "";
			}
			if("0".equals(kfEndPhoto)){
				kfEndPhoto = "";
			}
			
			UserView user = this.getUserView(request);
			
			//插入残疾信息
			Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				Map<String, Object> cjParam = new HashMap<String, Object>();
				String paramKey = (String) enu.nextElement();
				if (paramKey.startsWith("cj_type")) {
					String cjType = request.getParameter(paramKey);
					cjParam.put("cjType", cjType);
					String levelKey = "cj_level" + paramKey.substring(paramKey.lastIndexOf("_"));
					String cjLevel = request.getParameter(levelKey);
					cjParam.put("cjLevel", cjLevel);
					
					if(StringUtils.isNotBlank(cjType) && StringUtils.isNotBlank(cjLevel)){
						cjParam.put("maxaccept", patientID);
						List<Map<String, String>> cjList = patientManagerService.getPatientCJInfo(cjParam);
						if(cjList == null || cjList.size() < 1){
							cjParam.put("maxaccept", DBUtil.getMaxaccept(publicDao));
							cjParam.put("patientID", patientID);
							cjParam.put("oprID", user.getMaxaccept());
							cjParam.put("oprName", user.getUserName());
							cjParam.put("ableFlag", "10900");
							patientManagerService.insertCJMapper(cjParam);
						}
					}
				}
				
			}
		
			//图片后缀
			String suffix = null;
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("saveFlag", saveFlag);
			param.put("orderID", orderID);
			param.put("patientID", patientID);
			param.put("patientName", patientName);
			param.put("patientSex", patientSex);
			param.put("patientNation", patientNation);
			param.put("cardID", cardID);
			param.put("patientPhone", patientPhone);
			param.put("streetComm", streetComm);
			param.put("patientAddress", patientAddress);
			param.put("patientPathogeny", patientPathogeny);
			param.put("nowPresent", nowPresent);
			param.put("hisPresent", hisPresent);
			param.put("cCardID", cCardID);
			param.put("cCardSub", cCardSub);
			param.put("patientType", patientType);
			param.put("guName", guName);
			param.put("guPhone", guPhone);
			param.put("guRelative", guRelative);
			//存储服务照片
			if(StringUtil.isNotEmpty(cardPositive)){
				suffix = cardPositive.substring(cardPositive.indexOf("data:image/")+"data:image/".length(), cardPositive.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String cardPositivePath = patientID + "/cardPositivePath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(cardPositive, ConfigUtil.getConfigKey("PATIENT_PHOTO_PATH") + "/" + cardPositivePath);
				param.put("cardPositive", cardPositivePath);
			}else{
				param.put("cardPositive", "");
			}
			if(StringUtil.isNotEmpty(cardOther)){
				suffix = cardOther.substring(cardOther.indexOf("data:image/")+"data:image/".length(), cardOther.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String cardOtherPath = patientID + "/cardOtherPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(cardOther, ConfigUtil.getConfigKey("PATIENT_PHOTO_PATH") + "/" + cardOtherPath);
				param.put("cardOther", cardOtherPath);
			}else{
				param.put("cardOther", "");
			}
			if(StringUtil.isNotEmpty(cCardIndex)){
				suffix = cCardIndex.substring(cCardIndex.indexOf("data:image/")+"data:image/".length(), cCardIndex.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String cCardIndexPath = patientID + "/cCardIndexPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(cCardIndex, ConfigUtil.getConfigKey("PATIENT_PHOTO_PATH") + "/" + cCardIndexPath);
				param.put("cCardIndex", cCardIndexPath);
			}else{
				param.put("cCardIndex", "");
			}
			if(StringUtil.isNotEmpty(cCardPhoto)){
				suffix = cCardPhoto.substring(cCardPhoto.indexOf("data:image/")+"data:image/".length(), cCardPhoto.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String cCardPhotoPath = patientID + "/cCardPhotoPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(cCardPhoto, ConfigUtil.getConfigKey("PATIENT_PHOTO_PATH") + "/" + cCardPhotoPath);
				param.put("cCardPhoto", cCardPhotoPath);
			}else{
				param.put("cCardPhoto", "");
			}
			if(StringUtil.isNotEmpty(hbIndex)){
				suffix = hbIndex.substring(hbIndex.indexOf("data:image/")+"data:image/".length(), hbIndex.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String hbIndexPath = patientID + "/hbIndexPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(hbIndex, ConfigUtil.getConfigKey("PATIENT_PHOTO_PATH") + "/" + hbIndexPath);
				param.put("hbIndex", hbIndexPath);
			}else{
				param.put("hbIndex", "");
			}
			if(StringUtil.isNotEmpty(hbSelf)){
				suffix = hbSelf.substring(hbSelf.indexOf("data:image/")+"data:image/".length(), hbSelf.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String hbSelfPath = patientID + "/hbSelfPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(hbSelf, ConfigUtil.getConfigKey("PATIENT_PHOTO_PATH") + "/" + hbSelfPath);
				param.put("hbSelf", hbSelfPath);
			}else{
				param.put("hbSelf", "");
			}
			if(StringUtil.isNotEmpty(togetherPhoto)){
				suffix = togetherPhoto.substring(togetherPhoto.indexOf("data:image/")+"data:image/".length(), togetherPhoto.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String togetherPhotoPath = patientID + "/togetherPhotoPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(togetherPhoto, ConfigUtil.getConfigKey("PATIENT_PHOTO_PATH") + "/" + togetherPhotoPath);
				param.put("togetherPhoto", togetherPhotoPath);
			}else{
				param.put("togetherPhoto", "");
			}
			param.put("oprID", user.getMaxaccept());
			param.put("oprName", user.getUserName());
			//修改临时患者表数据
			patientManagerService.updateBasePatient(param);
			
			param.put("maxaccept", orderID);
			
			//存储服务照片
			if(StringUtil.isNotEmpty(kfBeforePhoto)){
				suffix = kfBeforePhoto.substring(kfBeforePhoto.indexOf("data:image/")+"data:image/".length(), kfBeforePhoto.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String kfBeforePhotoPath = patientID + "/kfBeforePhotoPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(kfBeforePhoto, ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + kfBeforePhotoPath);
				param.put("kfBeforePhoto", kfBeforePhotoPath);
			}else{
				param.put("kfBeforePhoto", "");
			}
			if(StringUtil.isNotEmpty(kfMiddlePhoto)){
				suffix = kfMiddlePhoto.substring(kfMiddlePhoto.indexOf("data:image/")+"data:image/".length(), kfMiddlePhoto.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String kfMiddlePhotoPath = patientID + "/kfMiddlePhotoPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(kfMiddlePhoto, ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + kfMiddlePhotoPath);
				param.put("kfMiddlePhoto", kfMiddlePhotoPath);
			}else{
				param.put("kfMiddlePhoto", "");
			}
			if(StringUtil.isNotEmpty(kfEndPhoto)){
				suffix = kfEndPhoto.substring(kfEndPhoto.indexOf("data:image/")+"data:image/".length(), kfEndPhoto.indexOf(";base64,"));
				if("jpeg".equalsIgnoreCase(suffix)){
					suffix = "jpg";
				}
				String kfEndPhotoPath = patientID + "/kfEndPhotoPath/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
				+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
				ImgUtil.BaseToImage(kfEndPhoto, ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + kfEndPhotoPath);
				param.put("kfEndPhoto", kfEndPhotoPath);
			}else{
				param.put("kfEndPhoto", "");
			}
			param.put("projectType", projectType);
			param.put("payType", payType);
			param.put("payFee", payFee);
			param.put("codeError", codeError);
			//修改工单表
			orderService.updateOrderInfoApp(param);
			
			if("1".equals(saveFlag)){
				param.put("maxaccept", patientID);
				//患者临时表数据插入正式表
				patientManagerService.patientTempToReal(param);
				Map<String, Object> para = new HashMap<String, Object>();
				
				//修改临时患者数据为失效
				para.put("patientID", patientID);
				para.put("ableFlag", "10901");
				patientManagerService.updateBasePatient(para);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 验证码校验
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/equalsCode")
	@ResponseBody
	public Result equalsCode(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		String maxaccept = request.getParameter("maxaccept");
		String codeNum = request.getParameter("codeNum");
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			param.put("codeNum", codeNum);
			List<Map<String, String>> codeList = orderService.getCodeNum(param);
			if(codeList == null || codeList.size() < 1){
				result.setResultCode("9999");
				result.setResultMsg("操作失败！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	
	@RequestMapping("/updateStartTime")
	@ResponseBody
	public Result updateStartTime(String maxaccept, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			orderService.updateStartTime(param);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 已完成工单查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getFinashOrder")
	@ResponseBody
	public Result getFinashOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		try {
			UserView user = this.getUserView(request);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("oprID", user.getMaxaccept());
			param.put("orderAbleFlag", "10900");
			param.put("orderStatus", "10601");
			List<Map<String,Object>> orderList = orderService.getOrderPatientDet(param);
			for (Map<String, Object> map : orderList) {
				Clob clob = (Clob) map.get("A_PATIENT_SIGN_PHONE");
				if(clob != null){
					Reader inReader = clob.getCharacterStream();
					BufferedReader br = new BufferedReader(inReader);
					String s = br.readLine();
					StringBuffer sb = new StringBuffer();
					while (s != null) {
						sb.append(s);
						s = br.readLine();
					}
					map.put("A_PATIENT_SIGN_PHONE", sb);
				}
			}
			result.setResultData(orderList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 工单详情查询
	 * @param maxaccept
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getOrderDet")
	@ResponseBody
	public Result getOrderDet(String maxaccept, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			List<Map<String,Object>> orderList = orderService.getOrderPatientDet(param);
			for (Map<String, Object> map : orderList) {
				Clob clob = (Clob) map.get("A_PATIENT_SIGN_PHONE");
				if(clob != null){
					Reader inReader = clob.getCharacterStream();
					BufferedReader br = new BufferedReader(inReader);
					String s = br.readLine();
					StringBuffer sb = new StringBuffer();
					while (s != null) {
						sb.append(s);
						s = br.readLine();
					}
					map.put("A_PATIENT_SIGN_PHONE", sb);
				}
			}
			result.setResultData(orderList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 查询超时工单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getOutTimeOrder")
	@ResponseBody
	public Result getOutTimeOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			UserView user = this.getUserView(request);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("oprID", user.getMaxaccept());
			param.put("ableFlag", "10900");
			param.put("orderStatus", "10600");
			List<Map<String, String>> orderList = orderService.getOutTimeOrder(param);
			result.setResultData(orderList);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 工单添加
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/addOrder")
	@ResponseBody
	public Result addOrder(@RequestParam(value="add_before_pic", required=false) MultipartFile add_before_pic, @RequestParam(value="add_middle_pic", required=false) MultipartFile add_middle_pic, 
			@RequestParam(value="add_after_pic", required=false) MultipartFile add_after_pic, @RequestParam(value="add_sign_pic", required=false) MultipartFile add_sign_pic, HttpServletRequest request, HttpServletResponse response, Model model) {
		String patientIDCard = request.getParameter("add_patient_idcard");
		String orderOpr = request.getParameter("add_order_opr");
		String beginTime = request.getParameter("add_begin_time");
		String endTime = request.getParameter("add_end_time");
		String projectType = request.getParameter("add_project_type");
		String payType = request.getParameter("add_pay_type");
		String payFee = request.getParameter("add_pay_fee");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			//判断客户信息是否存在
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("patientCardNO", patientIDCard);
			para.put("ableFlag", "10900");
			List<Map<String, String>> patienList = patientManagerService.getPatientByParam(para);
			if(patienList == null || patienList.size() < 1){
				result.setResultCode("0001");
				result.setResultMsg("患者信息不存在，不能添加工单！");
			}else{
				Map<String, Object> param = new HashMap<String, Object>();
				
				Map<String, String> patientInfo = patienList.get(0);
				Date date = new Date();
				
				//服务前照片存储
				if(add_before_pic != null && StringUtils.isNotBlank(add_before_pic.getOriginalFilename())){
					String addName = add_before_pic.getOriginalFilename();
					String suffix = addName.substring(addName.lastIndexOf("."));
					String tempPath = ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + patientInfo.get("MAXACCEPT") + "/kfBeforePhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN);
					
					String kfBeforePhotoPath = ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + patientInfo.get("MAXACCEPT") + "/kfBeforePhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + suffix;
					String orlPath = patientInfo.get("MAXACCEPT") + "/kfBeforePhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + suffix; 
					
					File dirPath = new File(tempPath);
					if (!dirPath.exists()) {
						dirPath.mkdirs();
					}
					File dealPath = new File(kfBeforePhotoPath);
					add_before_pic.transferTo(dealPath);
					param.put("kfBeforePhotoPath", orlPath);
				}
				
				//服务中照片存储
				if(add_middle_pic != null && StringUtils.isNotBlank(add_middle_pic.getOriginalFilename())){
					String addName = add_middle_pic.getOriginalFilename();
					String suffix = addName.substring(addName.lastIndexOf("."));
					String tempPath = ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + patientInfo.get("MAXACCEPT") + "/kfMiddlePhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN);
					
					String kfMiddlePhoto = ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + patientInfo.get("MAXACCEPT") + "/kfMiddlePhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + suffix;
					String orlPath = patientInfo.get("MAXACCEPT") + "/kfMiddlePhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + suffix;
					
					File dirPath = new File(tempPath);
					if (!dirPath.exists()) {
						dirPath.mkdirs();
					}
					File dealPath = new File(kfMiddlePhoto);
					add_middle_pic.transferTo(dealPath);
					param.put("kfMiddlePhoto", orlPath);
				}
				
				//服务后照片存储
				if(add_after_pic != null && StringUtils.isNotBlank(add_after_pic.getOriginalFilename())){
					String addName = add_before_pic.getOriginalFilename();
					String suffix = addName.substring(addName.lastIndexOf("."));
					String tempPath = ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + patientInfo.get("MAXACCEPT") + "/kfEndPhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN);
					
					String kfEndPhoto = ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + patientInfo.get("MAXACCEPT") + "/kfEndPhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + suffix;
					String orlPath = patientInfo.get("MAXACCEPT") + "/kfEndPhotoPath/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
							+ DateUtils.dateFormat(date, DateUtils.DATE_TIME_NOSIGN_PATTERN) + suffix;
					
					File dirPath = new File(tempPath);
					if (!dirPath.exists()) {
						dirPath.mkdirs();
					}
					File dealPath = new File(kfEndPhoto);
					add_after_pic.transferTo(dealPath);
					param.put("kfEndPhoto", orlPath);
				}
				
				UserView kfsInfo = userService.getUserView(orderOpr);
				UserView user = this.getUserView(request);
				param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
				param.put("patientID", patientInfo.get("MAXACCEPT"));
				param.put("patientName", patientInfo.get("PATIENT_NAME"));
				param.put("oprID", orderOpr);
				param.put("oprName", kfsInfo.getUserName());
				param.put("projectType", projectType);
				param.put("payType", payType);
				param.put("payFee", payFee);
				param.put("beginTime", beginTime + ":00");
				param.put("endTime", endTime + ":00");
				param.put("ableFlag", "10900");
				if(add_sign_pic != null && StringUtils.isNotBlank(add_sign_pic.getOriginalFilename())){
					String signName = add_sign_pic.getOriginalFilename();
					String suffix = signName.substring(signName.lastIndexOf("."));
					if("jpeg".equalsIgnoreCase(suffix)){
						suffix = "jpg";
					}
					BASE64Encoder encoder = new BASE64Encoder();
					String patientSign = "data:image/" +suffix+ ";base64," + encoder.encode(add_sign_pic.getBytes());
					param.put("patientSign", patientSign);
				}
				param.put("orderType", "11400");
				param.put("orderStatus", "10601");
				param.put("createOpr", user.getMaxaccept());
				param.put("createOprName", user.getUserName());
				orderService.insertOrder(param);
				
				//增加服务次数
				Map<String, Object> patientMap = new HashMap<String, Object>();
				String fwTimes = StrUtil.nvl(patientInfo.get("FW_TIMES"), "0");
				fwTimes = Integer.parseInt(fwTimes) + 1 + "";
				patientMap.put("maxaccept", patientInfo.get("MAXACCEPT"));
				patientMap.put("updateOpr", user.getMaxaccept());
				patientMap.put("updateOprName", user.getUserName());
				patientMap.put("serverTime", endTime + ":00");
				patientMap.put("fwTimes", fwTimes);
				patientMap.put("serverOpr", kfsInfo.getMaxaccept());
				patientMap.put("serverOprName", kfsInfo.getUserName());
				patientManagerService.updatePatientByParam(patientMap);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 工单修改
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/modOrder")
	@ResponseBody
	public Result modOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
		String orderID = request.getParameter("order_id");
		String oldPatientIDCard = request.getParameter("old_patient_idcard");
		String patientIDCard = request.getParameter("mod_patient_idcard");
		String orderOpr = request.getParameter("mod_order_opr");
		String beginTime = request.getParameter("mod_begin_time");
		String endTime = request.getParameter("mod_end_time");
		String projectType = request.getParameter("mod_project_type");
		String payType = request.getParameter("mod_pay_type");
		String payFee = request.getParameter("mod_pay_fee");
		
		//图片数据获取
		String kfBeforePhoto = request.getParameter("mod_before_pic_bak");
		String kfMiddlePhoto = request.getParameter("mod_middle_pic_bak");
		String kfEndPhoto = request.getParameter("mod_after_pic_bak");
		String patientSign = request.getParameter("mod_sign_pic_bak");
		
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			//判断客户信息是否存在
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("patientCardNO", patientIDCard);
			para.put("ableFlag", "10900");
			List<Map<String, String>> patienList = patientManagerService.getPatientByParam(para);
			if(patienList == null || patienList.size() < 1){
				result.setResultCode("0001");
				result.setResultMsg("患者信息不存在，不能添加工单！");
			}else{
				Map<String, Object> param = new HashMap<String, Object>();
				
				Map<String, String> patientInfo = patienList.get(0);
				String patientID = patientInfo.get("MAXACCEPT");
				
				String suffix = "";
				//服务前照片存储
				if(StringUtil.isNotEmpty(kfBeforePhoto)){
					suffix = kfBeforePhoto.substring(kfBeforePhoto.indexOf("data:image/")+"data:image/".length(), kfBeforePhoto.indexOf(";base64,"));
					if("jpeg".equalsIgnoreCase(suffix)){
						suffix = "jpg";
					}
					String kfBeforePhotoPath = patientID + "/kfBeforePhotoPath/" 
					+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
					+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
					ImgUtil.BaseToImage(kfBeforePhoto, ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + kfBeforePhotoPath);
					param.put("kfBeforePhoto", kfBeforePhotoPath);
				}else{
					param.put("kfBeforePhoto", "");
				}
				
				if(StringUtil.isNotEmpty(kfMiddlePhoto)){
					suffix = kfMiddlePhoto.substring(kfMiddlePhoto.indexOf("data:image/")+"data:image/".length(), kfMiddlePhoto.indexOf(";base64,"));
					if("jpeg".equalsIgnoreCase(suffix)){
						suffix = "jpg";
					}
					String kfMiddlePhotoPath = patientID + "/kfMiddlePhotoPath/" 
					+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
					+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
					ImgUtil.BaseToImage(kfMiddlePhoto, ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + kfMiddlePhotoPath);
					param.put("kfMiddlePhoto", kfMiddlePhotoPath);
				}else{
					param.put("kfMiddlePhoto", "");
				}

				if(StringUtil.isNotEmpty(kfEndPhoto)){
					suffix = kfEndPhoto.substring(kfEndPhoto.indexOf("data:image/")+"data:image/".length(), kfEndPhoto.indexOf(";base64,"));
					if("jpeg".equalsIgnoreCase(suffix)){
						suffix = "jpg";
					}
					String kfEndPhotoPath = patientID + "/kfEndPhotoPath/" 
					+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "/" 
					+ DateUtils.dateFormat(new Date(), DateUtils.DATE_TIME_NOSIGN_PATTERN) + "." + suffix;
					ImgUtil.BaseToImage(kfEndPhoto, ConfigUtil.getConfigKey("SERVER_PHOTO_PATH") + "/" + kfEndPhotoPath);
					param.put("kfEndPhoto", kfEndPhotoPath);
				}else{
					param.put("kfEndPhoto", "");
				}
				
				UserView kfsInfo = userService.getUserView(orderOpr);
				UserView user = this.getUserView(request);
				param.put("patientID", patientInfo.get("MAXACCEPT"));
				param.put("patientName", patientInfo.get("PATIENT_NAME"));
				param.put("oprID", orderOpr);
				param.put("oprName", kfsInfo.getUserName());
				param.put("projectType", projectType);
				param.put("payType", payType);
				param.put("payFee", payFee);
				if(StringUtil.isNotEmpty(beginTime)){
					param.put("serverBeginTime", beginTime + ":00");
				}
				if(StringUtil.isNotEmpty(endTime)){
					param.put("serverEndTime", endTime + ":00");
				}
				param.put("ableFlag", "10900");
				param.put("patientSign", patientSign);
				/*param.put("orderType", "11400");
				param.put("orderStatus", "10601");*/
				param.put("maxaccept", orderID);
				param.put("createOpr", user.getMaxaccept());
				param.put("createOprName", user.getUserName());
				orderService.updateOrderInfo(param);
				
				//修改患者信息，没有修改证件没有更换患者，至需要修改修改信息，修改证件更换用户需要删少原患者服务次数，增加新患者服务次数
				Map<String, Object> patientMap = new HashMap<String, Object>();
				if(oldPatientIDCard.equals(patientIDCard)){
					patientMap.put("maxaccept", patientInfo.get("MAXACCEPT"));
					patientMap.put("updateOpr", user.getMaxaccept());
					patientMap.put("updateOprName", user.getUserName());
					/*if(StringUtil.isNotEmpty(endTime)){
						patientMap.put("serverTime", endTime + ":00");
					}*/
					patientMap.put("serverOpr", kfsInfo.getMaxaccept());
					patientMap.put("serverOprName", kfsInfo.getUserName());
					patientManagerService.updatePatientByParam(patientMap);
				}else{
					//修改老患者
					Map<String, Object> paras = new HashMap<String, Object>();
					paras.put("patientCardNO", oldPatientIDCard);
					List<Map<String, String>> pList = patientManagerService.getPatientByParam(paras);
					if(pList != null && pList.size()>0){
						patientMap.put("maxaccept", pList.get(0).get("MAXACCEPT"));
					}
					patientMap.put("fwTimesMinus", "1");
					patientMap.put("updateOpr", user.getMaxaccept());
					patientMap.put("updateOprName", user.getUserName());
					patientMap.put("serverOpr", kfsInfo.getMaxaccept());
					patientMap.put("serverOprName", kfsInfo.getUserName());
					patientManagerService.updatePatientByParam(patientMap);
					
					//修改新患者
					patientMap.remove("patientIDCard");
					patientMap.put("maxaccept", patientInfo.get("MAXACCEPT"));
					patientMap.put("fwTimesAdd", "1");
					patientMap.remove("fwTimesMinus");
					patientMap.put("updateOpr", user.getMaxaccept());
					patientMap.put("updateOprName", user.getUserName());
					patientMap.put("serverOpr", kfsInfo.getMaxaccept());
					patientMap.put("serverOprName", kfsInfo.getUserName());
					patientManagerService.updatePatientByParam(patientMap);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}
	/**
	 * 工单删除
	 * @param request
	 * @param response
	 * @param model
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delOrder")
	@ResponseBody
	public Result delOrder(HttpServletRequest request, HttpServletResponse response, Model model, String ids){
		Result result=new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功");
		
		try {
			//患者信息复位(暂不支持上次服务人员、时间等信息复位，因为在此工单后可能存在新工单覆盖)
			String[] orderIds = ids.split(",");
			for(int ix=0; ix<orderIds.length; ix++){
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("maxaccept", orderIds[ix]);
				List<Map<String, String>> orderList = orderService.getOrderByParam(para);
				if(orderList != null && orderList.size() > 0){
					String patientID = orderList.get(0).get("PATIENT_ID");
					para.put("maxaccept", patientID);
					para.put("fwTimesMinus", "minus");
					patientManagerService.updatePatientByParam(para);
				}
			}
			
			if(ids.contains(",")){
				ids = ids.substring(0, ids.length()-1);//多条数据删除的时候使用，这里是单条数据删除 所以不使用。
			}
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", ids);
			orderService.delOrder(param);
			
			
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败");
		}
		return result;
	}
	
	/**
	 * bky:统计列表0114
	 * @param pageSize
	 * @param pageIndex
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getStatisticsList")
	@ResponseBody
	public TableResult<List<Map<String, Object>>> getStatisticsList(Integer pageSize, Integer pageIndex, HttpServletRequest request, HttpServletResponse response, Model model) {
		String deptCode = request.getParameter("deptCode");
		String orderStatus = request.getParameter("orderStatus");
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
			if(deptCodes.endsWith(",")){
				deptCodes = deptCodes.substring(0, deptCodes.length()-1);
			}
			/*if(StringUtil.isNotEmpty(deptCodes)){
				deptCodes = deptCodes.substring(0, deptCodes.length()-1);
			}*/
			
			Map<String, Object> param = new HashMap<String, Object>();
			/*if(orderStatus.equals("") || orderStatus.equals(null)){
				param.put("orderStatus", "10603,10600,10601,10602");
			}else{
				param.put("orderStatus", orderStatus);
			}*/
			param.put("orderStatus", orderStatus);
			param.put("row", pageSize);
			param.put("page", pageIndex);
			param.put("deptCode", deptCodes);
			if(StringUtil.isNotEmpty(beginTime)){
				param.put("beginTime", DateUtils.dateParse(beginTime+" 00:00:00", DateUtils.DATE_TIME_PATTERN));
			}
			if(StringUtil.isNotEmpty(endTime)){
				param.put("endTime", DateUtils.dateParse(endTime+" 23:59:59", DateUtils.DATE_TIME_PATTERN));
			}
			
			PageHelper.startPage(pageIndex, pageSize);
			List<Map<String, Object>> orderlList = orderService.getStatisticsList(param);
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
	 * bky:下载功能，动态生成Excel表格0117
	 * @param request
	 * @param response
	 * @param model
	 * @param deptCode
	 * @param orderStatus
	 * @param beginTime
	 * @param endTime
	 * @throws IOException
	 */
	@RequestMapping("/getExcel")
	@ResponseBody
	public void getExcel(HttpServletRequest request, HttpServletResponse response, Model model,
			String deptCode, String orderStatus, String beginTime, String endTime) throws IOException {
			TableResult<List<Map<String, Object>>> tableResult = new TableResult<List<Map<String, Object>>>();
		try{
			Map<String, String> para = new HashMap<String, String>();
			para.put("maxaccept", deptCode);
			List<Map<String, String>> deptList = deptService.getSonDept(para);
			String deptCodes = "";
			for(Map<String, String> map : deptList){
				deptCodes = deptCodes + map.get("MAXACCEPT") + ",";
			}
			if(deptCodes.endsWith(",")){
				deptCodes = deptCodes.substring(0, deptCodes.length()-1);
			}
			/*if(StringUtil.isNotEmpty(deptCodes)){
				deptCodes = deptCodes.substring(0, deptCodes.length()-1);
			}*/
			Map<String, Object> param = new HashMap<String, Object>();
			/*if(orderStatus.equals("") || orderStatus.equals(null)){
				param.put("orderStatus", "10603,10600,10601,10602");
			}else{
				param.put("orderStatus", orderStatus);
			}*/
			param.put("orderStatus", orderStatus);
			param.put("deptCode", deptCodes);
			if(StringUtil.isNotEmpty(beginTime)){
				param.put("beginTime", DateUtils.dateParse(beginTime+" 00:00:00", DateUtils.DATE_TIME_PATTERN));
			}
			if(StringUtil.isNotEmpty(endTime)){
				param.put("endTime", DateUtils.dateParse(endTime+" 23:59:59", DateUtils.DATE_TIME_PATTERN));
			}
			List<Map<String, Object>> orderlList = orderService.getStatisticsList(param);//查询表格数据 
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String day = sdf.format(date);//创建日期对象，用于生成Excel表格名称，时间截止到秒，避免Excel名称重复
			
			//****************数据写入Excel***********************************
			String[] title = {"康复师","毫米波","针灸","拔罐","肢体精准康复","其他","工单总数/人"};
			String fileName = "康复师工单统计表"+day+".xls";
			String sheetName = "康复师工单统计表";
			int ol =orderlList.size()+1;
			String[][] content = new String[ol][];
			int hmbhj=0;
			int zjhj=0;
			int bghj=0;
			int ztkfhj=0;
			int qthj=0;
			for (int i = 0; i < ol; i++) {
			    content[i] = new String[title.length];
			    if(i==orderlList.size()){
	            	int zhj=hmbhj+zjhj+bghj+ztkfhj+qthj;
	            	content[i][0] ="合计";
	 	            content[i][1] =String.valueOf(hmbhj);
	 	            content[i][2] =String.valueOf(zjhj);
	 	            content[i][3] =String.valueOf(bghj);
	 	            content[i][4] =String.valueOf(ztkfhj);
	 	            content[i][5] =String.valueOf(qthj);
	 	            content[i][6] =String.valueOf(zhj);
	            }else{
	            	Map<String, Object> obj =  orderlList.get(i);
	  	            String hmb=String.valueOf(obj.get("HMB_CONUT"));
	  	            String zj=String.valueOf(obj.get("ZJ_COUNT"));
	  	            String bg=String.valueOf(obj.get("BG_COUNT"));
	  	            String ztkf=String.valueOf(obj.get("ZTKF_COUNT"));
	  	            String qt=String.valueOf(obj.get("QT_COUNT"));
	  	            if(hmb==null || hmb.equals("")){
	  	            	hmb="0";
	  	            }
	  	            if(zj==null  || hmb.equals("")){
	  	            	zj="0";
	  	            }
	  	            if(bg==null  || hmb.equals("")){
	  	            	bg="0";
	  	            }
	  	            if(ztkf==null|| hmb.equals("")){
	  	            	ztkf="0";
	  	            }
	  	            if(qt==null  || hmb.equals("")){
	  	            	qt="0";
	  	            }
	  		        content[i][0] =String.valueOf(obj.get("USER_NAME"));
	  		        content[i][1] =hmb;
	  		        content[i][2] =zj;
	  		        content[i][3] =bg;
	  		        content[i][4] =ztkf;
	  		        content[i][5] =qt;
	  		        int hmbint=Integer.valueOf(hmb).intValue();
	  		        int zjint=Integer.valueOf(zj).intValue();
	  		        int bgint=Integer.valueOf(bg).intValue();
	  		        int ztkfint=Integer.valueOf(ztkf).intValue();
	  		        int qtint=Integer.valueOf(qt).intValue();
	  		        hmbhj+=hmbint;
	  	            zjhj+=zjint;
	  	            bghj+=bgint;
	  	            ztkfhj+=ztkfint;
	  	            qthj+=qtint;
	  	            int sum=hmbint+zjint+bgint+ztkfint+qtint;
	  	            String TOTAL_SUM=Integer.toString(sum);
	  	            content[i][6] =TOTAL_SUM;
	            }
			}
			
			//创建HSSFWorkbook 
			HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}catch(Exception e){
			logger.error(e);
			tableResult.setTotal(0);
			tableResult.setRows(new ArrayList<Map<String, Object>>());
		}
	}
	
	//发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "989820939045.jpg";
		System.out.println(str.substring(str.lastIndexOf(".")));
	}

	/**shl 获取合计工单
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getStatisticsTotal")
	@ResponseBody
	public Result getStatisticsTotal( HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		String deptCode = request.getParameter("deptCode");
		String orderStatus = request.getParameter("orderStatus");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		
		try{
			Map<String, String> para = new HashMap<String, String>();
			para.put("maxaccept", deptCode);
			List<Map<String, String>> deptList = deptService.getSonDept(para);
			String deptCodes = "";
			for(Map<String, String> map : deptList){
				deptCodes = deptCodes + map.get("MAXACCEPT") + ",";
			}
			if(deptCodes.endsWith(",")){
				deptCodes = deptCodes.substring(0, deptCodes.length()-1);
			}
			/*if(StringUtil.isNotEmpty(deptCodes)){
				deptCodes = deptCodes.substring(0, deptCodes.length()-1);
			}*/
			Map<String, Object> param = new HashMap<String, Object>();
		/*	if(orderStatus.equals("") || orderStatus.equals(null)){
				param.put("orderStatus", "10603,10600,10601,10602");
			}else{
				param.put("orderStatus", orderStatus);
			}*/
			param.put("orderStatus", orderStatus);
			param.put("deptCode", deptCodes);
			if(StringUtil.isNotEmpty(beginTime)){
				param.put("beginTime", DateUtils.dateParse(beginTime+" 00:00:00", DateUtils.DATE_TIME_PATTERN));
			}
			if(StringUtil.isNotEmpty(endTime)){
				param.put("endTime", DateUtils.dateParse(endTime+" 23:59:59", DateUtils.DATE_TIME_PATTERN));
			}
			List<Map<String, Object>> orderTotal = orderService.getStatisticsTotal(param);
			result.setResultData(orderTotal);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	}	
}
