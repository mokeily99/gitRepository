package com.yl.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {

	public static Map<String, String> uploadFile(MultipartFile file, String path, HttpServletRequest request)
			throws IOException {
		String fileName = file.getOriginalFilename();
		// String
		// basepath=request.getSession().getServletContext().getRealPath("/");
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
		// File tempFile = new File(path, new Date().getTime() +
		// String.valueOf(fileName));
		String saveName = String.valueOf((new Date()).getTime()) + String.valueOf((int) (Math.random() * 9 + 1) * 1000)
				+ '.' + fileType;
		File tempFile = new File(path, String.valueOf(saveName));
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
		if (!tempFile.exists()) {
			tempFile.createNewFile();
		}
		file.transferTo(tempFile);
		Map<String, String> map = new HashMap<String, String>();
		map.put("oriName", fileName);
		map.put("saveName", saveName);
		map.put("fileType", fileType);
		// return tempFile.getName();
		return map;
	}

	public static void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
