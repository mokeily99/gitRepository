package com.yl.transaction.patientManager.controller;

import java.io.BufferedReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.yl.common.controller.BaseController;
import com.yl.common.dao.PublicDao;
import com.yl.common.pojo.Result;
import com.yl.common.pojo.TableResult;
import com.yl.common.user.pojo.UserView;
import com.yl.common.util.DBUtil;
import com.yl.common.util.DateUtils;
import com.yl.common.util.StrUtil;
import com.yl.transaction.order.service.OrderService;
import com.yl.transaction.patientManager.service.PatientManagerService;
/**
 * 获取患者信息shl
 * @param request
 * @param response
 * @param model
 * @return
 */
@Controller
@RequestMapping("/patientManager")
public class PatientManagerController extends BaseController{
	
	@Resource
	private PatientManagerService patientManagerService;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private PublicDao publicDao;
	

	/**
	 * 获取患者信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/patientInfoList")
	@ResponseBody
	public TableResult<List<Map<String, String>>> patientInfoList(Integer pageSize, Integer pageIndex, 
		String patientInfoType,String patientInfocardID,String patientInfoName,String beginTime,String endTime,String queryType,
		String queryParam,HttpServletRequest request, HttpServletResponse response, Model model) {
		TableResult<List<Map<String, String>>> tableResult = new TableResult<List<Map<String, String>>>();
		try{
			Map<String, Object> paramMap=new HashMap<String,Object>();
			paramMap.put("queryType",queryType);
			paramMap.put("queryParam", URLDecoder.decode(URLDecoder.decode(queryParam, "UTF-8"), "UTF-8"));
			paramMap.put("type",patientInfoType);
			paramMap.put("name", URLDecoder.decode(URLDecoder.decode(patientInfoName, "UTF-8"), "UTF-8"));
			paramMap.put("cardID",patientInfocardID);
			paramMap.put("row", pageSize);
			paramMap.put("page", pageIndex);
			if(StringUtil.isNotEmpty(beginTime)){
				paramMap.put("beginTime", DateUtils.dateParse(beginTime+" 00:00:00", DateUtils.DATE_TIME_PATTERN));
			}
			if(StringUtil.isNotEmpty(endTime)){
				paramMap.put("endTime", DateUtils.dateParse(endTime+" 23:59:59", DateUtils.DATE_TIME_PATTERN));
			}
			
			PageHelper.startPage(pageIndex, pageSize);
			List<Map<String, String>> patientInfoList = patientManagerService.getAllPatientManger(paramMap);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(patientInfoList);
			
			tableResult.setTotal((int) pageinfo.getTotal());
			tableResult.setRows(patientInfoList); 
		}catch (Exception e) {
			tableResult.setTotal(0);
			tableResult.setRows(new ArrayList<Map<String, String>>());
		}
		return  tableResult;
	}
	
	/**
	 * 删除患者信息 shl
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/delPatientInfo")
	@ResponseBody
	public Result delPatientInfo(HttpServletRequest request, HttpServletResponse response, Model model, String ids){
		Result result=new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功");
		/*获取当前操作人的姓名和id*/
		UserView userView = this.getUserView(request);
	    String userName = userView.getUserName();
	    String userId = userView.getMaxaccept();
		Map<String, String> param = new HashMap<String, String>();
		try {
			param.put("maxaccept", ids);
			param.put("updateOpr",userId);
			param.put("updateOprName", userName);
			patientManagerService.delPatientMangerInIDS(param);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败");
		}
		return result;
	}
	
	/**
	 * 增加患者shl
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/addPatientInfo")
	@ResponseBody
	public Result addPatientInfo(HttpServletRequest request, HttpServletResponse response, Model model){
		String patientType=request.getParameter("add_patient_type");
		String patientName=request.getParameter("add_patient_name");
		String patientSex=request.getParameter("add_patient_sex");
		String patientNation=request.getParameter("add_patient_nation");
		String cardID=request.getParameter("add_card_id");
		String patientTel=request.getParameter("add_patient_tel");
		String patientPhone=request.getParameter("add_patient_phone");
		String patientAddress=request.getParameter("add_patient_address");
		String comm=request.getParameter("add_street_comm");
		String patientPathogeny=request.getParameter("add_patient_pathogeny");
		String nowPresent=request.getParameter("add_now_present");
		String hisPresent=request.getParameter("add_his_present");
		String ccardID=request.getParameter("add_ccard_id");
		String sub=request.getParameter("add_ccard_sub");
		String guName=request.getParameter("add_gu_name");
		String guTel=request.getParameter("add_gu_tel");
		String guRelative=request.getParameter("add_gu_relative");
		String emName=request.getParameter("add_em_name");
		String emTel=request.getParameter("add_em_tel");
		String emRelative=request.getParameter("add_em_relative");
		String version=request.getParameter("add_version");
		String ableFlag=request.getParameter("add_able_flag");
	    
		//获取当前登录的用户姓名和用户id
		UserView userView = this.getUserView(request);
		String userName = userView.getUserName();
		String userId = userView.getMaxaccept();
		Map<String, String> param=new HashMap<String,String>();
		//传患者身份证号， 判断是否有该用户存在
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("cardID", cardID);
			
		param.put("type",patientType);
		param.put("name", patientName);
		param.put("sex", patientSex);
		param.put("nation",patientNation);
		param.put("cardID", cardID);
		param.put("ptel", patientTel);
		param.put("pphone", patientPhone);
		param.put("address",patientAddress);
		param.put("comm", comm);
		param.put("pathogeny", patientPathogeny);
		param.put("nowpresent", nowPresent);
		param.put("hispresent", hisPresent);
		param.put("ccardID", ccardID);
		param.put("sub", sub);
		param.put("guname", guName);
		param.put("gutel", guTel);
		param.put("gurelative", guRelative);
		param.put("emname", emName);
		param.put("emtel", emTel);
		param.put("emrelative", emRelative);
		param.put("ableflag", "10900");
		param.put("version", "0");
		param.put("maxaccept",DBUtil.getMaxaccept(publicDao));
		param.put("updateOpr",userId);
		param.put("updateOprName", userName);
			
		Result result = new Result();
			result.setResultCode("0000");
			result.setResultMsg("操作成功！");
		try{
			List<Map<String, String>> patient=patientManagerService.getPatientMangerByAccount(para);
			if(patient.size()<1){
				patientManagerService.addPatientManger(param);
			}else {
				result.setResultCode("9999");
				result.setResultMsg("添加失败,该患者已经存在!");
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
	/**
	 * 修改患者信息 shl
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/modifyPatientInfo")
	@ResponseBody
	public Result modifyPatientInfo(HttpServletRequest request, HttpServletResponse response, Model model,String getVersion){
		String maxaccept =request.getParameter("mod_maxaccept");
		String patientType=request.getParameter("mod_patient_type");
		String patientName=request.getParameter("mod_patient_name");
		String patientSex=request.getParameter("mod_patient_sex");
		String patientNation=request.getParameter("mod_patient_nation");
		String cardID=request.getParameter("mod_card_id");
		String patientTel=request.getParameter("mod_patient_tel");
		String patientPhone=request.getParameter("mod_patient_phone");
		String patientAddress=request.getParameter("mod_patient_address");
		String comm=request.getParameter("mod_street_comm");
		String patientPathogeny=request.getParameter("mod_patient_pathogeny");
		String nowPresent=request.getParameter("mod_now_present");
		String hisPresent=request.getParameter("mod_his_present");
		String ccardID=request.getParameter("mod_ccard_id");
		String sub=request.getParameter("mod_ccard_sub");
		String guName=request.getParameter("mod_gu_name");
		String guTel=request.getParameter("mod_gu_tel");
		String guRelative=request.getParameter("mod_gu_relative");
		String emName=request.getParameter("mod_em_name");
		String emTel=request.getParameter("mod_em_tel");
		String emRelative=request.getParameter("mod_em_relative");
		String version=request.getParameter("mod_version");//数据库获取的
		//获取当前操作人的姓名和id
		UserView userView = this.getUserView(request);
	    String userName = userView.getUserName();
	    String userId = userView.getMaxaccept();
		//修改时版本号自动加1
		int version1 = Integer.parseInt(version);
		int vercun = version1+1;
		String vercun1=String.valueOf(vercun);//修改后要传的
		
		Map<String, String> param = new HashMap<String,String>();
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			param.put("type",patientType);
			param.put("name", patientName);
			param.put("sex", patientSex);
			param.put("nation",patientNation);
			param.put("cardID", cardID);
			param.put("ptel", patientTel);
			param.put("pphone", patientPhone);
			param.put("address",patientAddress);
			param.put("comm", comm);
			param.put("pathogeny", patientPathogeny);
			param.put("nowpresent", nowPresent);
			param.put("hispresent", hisPresent);
			param.put("ccardID", ccardID);
			param.put("sub", sub);
			param.put("guname", guName);
			param.put("gutel", guTel);
			param.put("gurelative", guRelative);
			param.put("emname", emName);
			param.put("emtel", emTel);
			param.put("emrelative", emRelative);
			param.put("maxaccept", maxaccept);
			param.put("version", version);
			param.put("ver", vercun1);
			param.put("updateOpr",userId);
			param.put("updateOprName", userName);
		Map<String, String> para =new HashMap<String,String>();
		para.put("maxaccept", maxaccept);
		String getVersion1=patientManagerService.getVersion(para);
			if(version.equals(getVersion1)){ 
				patientManagerService.updatePatientMangertByID(param);
			}else{
				result.setResultCode("9999");
				result.setResultMsg("操作失败请重新获取修改数据");
			}
		}catch(Exception e) {
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
	/**
	 * 获取版本号
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getVersion")
	@ResponseBody
	public Result getVersion(HttpServletRequest request, HttpServletResponse response, Model model){
		String getVersion=request.getParameter("mod_version");
		String maxaccept=request.getParameter("maxaccept");
		Map<String, String> param=new HashMap<String, String>();
		param.put("getVersion", getVersion);
		param.put("maxaccept", maxaccept);
		patientManagerService.getVersion(param);
		return result;
	}
	
	@RequestMapping("/queryPatientInfo")
	@ResponseBody
	public Result queryPatientInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		String patientCardNO = request.getParameter("patientCardNO");
		String maxaccept = request.getParameter("maxaccept");
		
		String patientName = request.getParameter("patient_name");
		String patientPhone = request.getParameter("patient_phone");
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("patientCardNO", patientCardNO);
			param.put("ableFlag", "10900");
			param.put("maxaccept", maxaccept);
			param.put("patientName", patientName);
			param.put("patientPhone", patientPhone);
			List<Map<String, String>> patientList = patientManagerService.getPatientByParam(param);
			result.setResultData(patientList);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
	/**
	 * 患者预约
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/makePatient")
	@ResponseBody
	public Result makePatient(HttpServletRequest request, HttpServletResponse response, Model model) {
		String maxaccept = request.getParameter("maxaccept");
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		try{
			UserView user = this.getUserView(request);
			
			//序列为空先添加患者信息
			if(StringUtils.isEmpty(maxaccept)){
				maxaccept = DBUtil.getMaxaccept(publicDao);
				String patientName = request.getParameter("patient_name");
				String patientPhone = request.getParameter("patient_phone");
				String patientAddress = request.getParameter("patient_address");
				String patientCardID = request.getParameter("patient_cardID");
				
				//判断证件信息是否存在,否则插入用户信息
				Map<String, Object> cardParam = new HashMap<String, Object>();
				cardParam.put("patientCardNO", patientCardID);
				List<Map<String, String>> patientList = patientManagerService.getPatientByParam(cardParam);
				if(patientList != null && patientList.size() > 0){
					maxaccept = patientList.get(0).get("MAXACCEPT");
				}else{
					Map<String, Object> addParam = new HashMap<String, Object>();
					addParam.put("maxaccept", maxaccept);
					addParam.put("patientName", patientName);
					addParam.put("patientPhone", patientPhone);
					addParam.put("patientAddress", patientAddress);
					addParam.put("patientCardID", patientCardID);
					addParam.put("oprID", user.getMaxaccept());
					addParam.put("oprName", user.getUserName());
					addParam.put("ableFlag", "10900");
					addParam.put("fwTimes", "0");
					addParam.put("version", "0");
					patientManagerService.insertPatientInfo(addParam);
				}
			}
			
			//预约
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			List<Map<String, String>> patientList = patientManagerService.getPatientByParam(param);
			if(patientList != null && patientList.size() > 0){
				Map<String, String> patientInfo = patientList.get(0);
				String makeFlag = patientInfo.get("MAKE_FLAG");
				if("10700".equals(makeFlag) || "10701".equals(makeFlag)){
					result.setResultCode("0002");
					result.setResultMsg("患者已被其他康复师预约！");
				}else{
					String version = patientInfo.get("VERSION");
					param.put("version", version);
					int versionAdd = Integer.parseInt(StrUtil.nvl(version, "0"));
					param.put("versionAdd", ++versionAdd);
					param.put("makeFlag", "10700");
					int flag = patientManagerService.updatePatientByParam(param);
					if(flag == 0){
						result.setResultCode("0002");
						result.setResultMsg("患者已被其他康复师预约！");
					}
					
					//预约工单创建
					Map<String, Object> orderParam = new HashMap<String, Object>();
					orderParam.put("maxaccept", DBUtil.getMaxaccept(publicDao));
					orderParam.put("patientID", maxaccept);
					orderParam.put("patientName", patientInfo.get("PATIENT_NAME"));
					orderParam.put("oprID", user.getMaxaccept());
					orderParam.put("oprName", user.getUserName());
					orderParam.put("orderStatus", "10603");
					orderParam.put("ableFlag", "10900");
					orderParam.put("createOpr", user.getMaxaccept());
					orderParam.put("createOprName", user.getUserName());
					
					orderService.insertOrder(orderParam);
				}
			}else{
				result.setResultCode("0001");
				result.setResultMsg("患者信息不存在无法预约！");
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}

	/**
	 * 患者类型下拉菜单
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/patientTypeList")
	public void patientTypeList(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Map<String, String>> typeList = patientManagerService.getPatientTypeList();
		Map<String, String> para = new HashMap<String, String>();
		para.put("CODE_NAME","请选择");
		para.put("CODE_ID","");
		typeList.add(0,para);
		write(response, typeList);
	}
	
	/**
	 * 患者性别下拉菜单
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/patientSexList")
	public void patientSexList(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Map<String, String>> sexList = patientManagerService.getPatientSexList();
		Map<String, String> param =new HashMap<String, String>();
		param.put("CODE_NAME","请选择");
		param.put("CODE_ID","");
		/*让其第一条显示的数据为新增的这条*/
		sexList.add(0, param);
		write(response, sexList);
	}
	
	/**
	 * 获取残疾类型信息
	 * @param maxaccept
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCJInfo")
	@ResponseBody
	public Result getCJInfo(String maxaccept, HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			param.put("ableFlag", "10900");
			List<Map<String, String>> patientList = patientManagerService.getPatientCJInfo(param);
			result.setResultData(patientList);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
	
	/**
	 * 查询详情患者信息
	 * @param maxaccept
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/patientInfoDetList")
	@ResponseBody
	public Result patientInfoDetList(String maxaccept, HttpServletRequest request, HttpServletResponse response, Model model){
		Result result =new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("maxaccept", maxaccept);
			List<Map<String, Object>> patientInfoDetList = patientManagerService.getPatientInfoDet(param);
			result.setResultData(patientInfoDetList);
		}catch  (Exception e){
			logger.error(e.getMessage(),e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败！");
		}
		return result;
	} 
	
}