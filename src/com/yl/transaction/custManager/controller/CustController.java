package com.yl.transaction.custManager.controller;

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
import com.yl.common.util.UploadUtil;
import com.yl.transaction.code.service.CodeService;
import com.yl.transaction.custManager.service.CustService;

@Controller
@RequestMapping("/cust")
public class CustController extends BaseController {

	private final static String HOME_PATH = CustController.class.getResource("/").getPath();

	private final static String DOWNLOAD_TEMP_FILE = HOME_PATH.subSequence(0, HOME_PATH.indexOf("WEB-INF")) + "";

	@Resource
	private CustService custService;

	@Resource
	private CodeService codeService;

	@Resource
	private PublicDao publicDao;

	@RequestMapping("/custList")
	@ResponseBody
	public LayTableResult<List<Map<String, String>>> custList(Integer page, Integer limit, String custName, String custType, HttpServletRequest request, HttpServletResponse response, Model model) {
		LayTableResult<List<Map<String, String>>> tableResult = new LayTableResult<List<Map<String, String>>>();
		try {
			UserView user = this.getUserView(request);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("row", limit);
			param.put("page", page);

			param.put("custName", custName);
			param.put("custType", custType);
			param.put("deptCode", user.getDeptCode());

			PageHelper.startPage(page, limit);
			List<Map<String, String>> blackList = custService.getCustList(param);
			PageInfo<Map<String, String>> pageinfo = new PageInfo<Map<String, String>>(blackList);

			tableResult.setCount((int) pageinfo.getTotal());
			tableResult.setData(blackList);
		} catch (Exception e) {
			tableResult.setCode(1);
			tableResult.setMsg("数据加载失败！");
			tableResult.setCount(0);
			tableResult.setData(new ArrayList<Map<String, String>>());
		}
		return tableResult;
	}

	/**
	 * 单个客户新增
	 * 
	 * @param senwords
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/addCustList")
	@ResponseBody
	public Result addCustList(HttpServletRequest request, HttpServletResponse response, Model model) {
		String custName = request.getParameter("add_cust_name");
		String custSex = request.getParameter("add_cust_sex");
		String custAge = request.getParameter("add_cust_age");
		String connPhone = request.getParameter("add_conn_phone");
		String custAddr = request.getParameter("add_cust_addr");
		String custType = request.getParameter("add_cust_type");
		String custMark = request.getParameter("add_cust_mark");

		Result result = new Result();
		try {
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
			param.put("custName", custName);
			param.put("custSex", custSex);
			param.put("custAge", custAge);
			param.put("connPhone", connPhone);
			param.put("custAddr", custAddr);
			param.put("custType", custType);
			param.put("custMark", custMark);
			param.put("oprID", user.getMaxaccept());
			custService.addCustList(param);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}

	/**
	 * 批量客户资料模板下载
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downlodCustFile")
	public void downlodCustFile(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			String filePath = DOWNLOAD_TEMP_FILE + ConfigUtil.getConfigKey("BAT_CUST_FILE");
			File file = new File(filePath);
			String fileName = file.getName();

			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.setContentType("text/html;charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Content-Length", "" + file.length());
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			fis.close();
			toClient.close();
		}
	}

	/**
	 * 批量客户资料上传
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param file
	 * @return
	 */
	@RequestMapping("/uploadUserFace")
	@ResponseBody
	public Result batCustInfo(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam("custList") MultipartFile file) {
		Result result = new Result();
		try {
			File excelFile = new File(file.getOriginalFilename());
			// 转换成file
			UploadUtil.inputStreamToFile(file.getInputStream(), excelFile);

			List<Map<String, String>> excelList = null;
			String suffix = excelFile.getName().substring(excelFile.getName().lastIndexOf(".") + 1);
			if ("xlsx".equals(suffix)) {
				excelList = ExcelUtil.readExcelByFileForXlsx(excelFile, 1, 0, 0);
			} else {
				excelList = ExcelUtil.readExcelByFileForXls(excelFile, 1, 0, 0);
			}

			if (excelList != null) {
				UserView user = this.getUserView(request);
				for (Map<String, String> info : excelList) {
					Map<String, String> param = new HashMap<String, String>();
					param.put("maxaccept", DBUtil.getMaxaccept(publicDao));
					param.put("custName", info.get("var0"));
					String bsex = info.get("var1");
					Map<String, String> sexCode = codeService.getCodeByName("SEX", bsex);
					param.put("custSex", sexCode.get("CODE_ID"));
					param.put("custAge", info.get("var2"));
					param.put("connPhone", info.get("var3"));
					param.put("custAddr", info.get("var4"));
					param.put("custMark", info.get("var5"));
					param.put("oprID", user.getMaxaccept());
					custService.addCustList(param);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}

	/**
	 * 客户资料修改
	 * 
	 * @param edit_senwords
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/editCustInfo")
	@ResponseBody
	public Result editBlackList(HttpServletRequest request, HttpServletResponse response, Model model) {
		String maxaccept = request.getParameter("edit_cust_id");
		String custName = request.getParameter("edit_cust_name");
		String custSex = request.getParameter("edit_cust_sex");
		String custAge = request.getParameter("edit_cust_age");
		String connPhone = request.getParameter("edit_conn_phone");
		String custAddr = request.getParameter("edit_cust_addr");
		String custType = request.getParameter("edit_cust_type");
		String custMark = request.getParameter("edit_cust_mark");

		Result result = new Result();
		try {
			UserView user = this.getUserView(request);
			Map<String, String> param = new HashMap<String, String>();
			param.put("maxaccept", maxaccept);
			param.put("custName", custName);
			param.put("custSex", custSex);
			param.put("custAge", custAge);
			param.put("connPhone", connPhone);
			param.put("custAddr", custAddr);
			param.put("custType", custType);
			param.put("custMark", custMark);
			param.put("oprID", user.getMaxaccept());
			custService.updateCustInfo(param);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}

	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/delCustInfo")
	@ResponseBody
	public Result delCustInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		Result result = new Result();
		result.setResultCode("0000");
		result.setResultMsg("操作成功！");

		String ids = request.getParameter("ids");
		try {
			if (ids.contains(",")) {
				ids = ids.substring(0, ids.length() - 1);
			}
			custService.delCustInIDS(ids);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setResultCode("9999");
			result.setResultMsg("操作失败!" + e);
		}
		return result;
	}
}
