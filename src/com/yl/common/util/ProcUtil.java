package com.yl.common.util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class ProcUtil {
	

	/**
     * @desc 启动进程
     * @author zp
     * @date 2018-3-29
     */
    public static void startProc(String processName) { 
         System.out.println("启动应用程序：" + processName);  
         if (StringUtils.isNotBlank(processName)) {  
             try {  
                 Desktop.getDesktop().open(new File(processName));  
             } catch (Exception e) {  
                 e.printStackTrace();  
                 System.out.println("应用程序：" + processName + "不存在！");  
             }  
         }   
    }
    /**
     * @desc 杀死进程
     * @author zp
     * @throws IOException 
     * @date 2018-3-29
     */
    public static void killProc(String processName) throws IOException {  
        System.out.println("关闭应用程序：" + processName);  
        if (StringUtils.isNotBlank(processName)) {  
            executeCmd("taskkill /F /IM " + processName);  
        } 
    }
    /**
     * @desc 执行cmd命令 
     * @author zp
     * @date 2018-3-29
     */
    public static String executeCmd(String command) throws IOException {  
        System.out.println("Execute command : " + command);  
        Runtime runtime = Runtime.getRuntime();  
        Process process = runtime.exec("cmd /c " + command);  
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));  
        String line = null;  
        StringBuilder build = new StringBuilder();  
        while ((line = br.readLine()) != null) {  
            System.out.println(line);  
            build.append(line);  
        }  
        return build.toString();  
    }  
    /**
     * @desc 判断进程是否开启
     * @author zp
     * @date 2018-3-29
     */
    public static boolean findProcess(String processName) {
        BufferedReader bufferedReader = null;
        try {
            Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName +'"');
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {}
            }
        }
    }

	public static void main(String[] args) {
		String url = "C:\\Users\\Administrator\\Desktop\\YouLian\\EnJiaPhone.exe";
		String procName = url.substring(url.lastIndexOf("\\") + 1, url.length());
		boolean exist = findProcess(procName);
		try {
			killProc(procName);
		} catch (Exception e) {
			System.out.println("重启/杀死提取程序失败。。。");
		}
		
		System.out.println("tasklist -fi " + '"' + "imagename eq " + procName +'"');
	}
}
