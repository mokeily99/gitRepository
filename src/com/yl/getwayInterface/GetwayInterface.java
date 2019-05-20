package com.yl.getwayInterface;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yl.common.controller.BaseController;
import com.yl.common.pojo.Entity;
import com.yl.common.pojo.Result;
import com.yl.transaction.code.service.CodeService;

@Controller
@RequestMapping("/getwayInterface")
public class GetwayInterface extends BaseController{

	@Resource
	private CodeService codeService;
	
	@RequestMapping(value="report",consumes="application/json;charset=utf-8",produces="application/json;charset=utf-8")
	@ResponseBody
	public Map<String, String> report(HttpServletRequest request, HttpServletResponse response, Model model, @RequestBody Map<String, String> json) {
		logger.error("=============="+json);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("status", "success");
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			
		}
		return result;
	}
	/**
	 * 坐席置忙
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/agentBreak")
	@ResponseBody
	public Result agentBreak(HttpServletRequest request, HttpServletResponse response, Model model) {
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
	
	public static void main(String[] args) throws Exception {  
        SSLContext context = SSLContext.getInstance("TLS");  
        context.init(null, null, null);  

        SSLSocketFactory factory = (SSLSocketFactory) context.getSocketFactory();  
        SSLSocket socket = (SSLSocket) factory.createSocket();  

        String[] protocols = socket.getSupportedProtocols();  

        System.out.println("Supported Protocols: " + protocols.length);  
        for (int i = 0; i < protocols.length; i++) {  
            System.out.println(" " + protocols[i]);  
        }  

        protocols = socket.getEnabledProtocols();  

        System.out.println("Enabled Protocols: " + protocols.length);  
        for (int i = 0; i < protocols.length; i++) {  
            System.out.println(" " + protocols[i]);  
        }  

    }  
}
