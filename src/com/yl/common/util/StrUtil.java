package com.yl.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字符串工具
 * @author swz
 *
 */
public class StrUtil {
	
	/**
	 * 判断字符串是否包含数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	}
	/**
	 * 获取字符串最后数字组合位置
	 * @param str
	 * @return
	 */
	public static int lastNumIndex(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    boolean flag = true;
	    int index = str.length();
	    for(int ix=1; ix<=str.length(); ix++){
			String ss = str.substring(str.length()-ix);
			if(!pattern.matcher(ss).matches() && flag){
				flag = false;
				index = str.length()-ix+1;
			}
		}
	    
	    return index;   
	}
	
	public static void main(String[] args) {
		String str = "万龙名城A-8#0608";
		System.out.println(str.substring(0,lastNumIndex(str)));
	}

	public static String filterChinese(String str) {
		// 用于返回结果
		String result = str;
		boolean flag = isContainChinese(str);
		if (flag) {// 包含中文
			// 用于拼接过滤中文后的字符
			StringBuffer sb = new StringBuffer();
			// 用于校验是否为中文
			boolean flag2 = false;
			// 用于临时存储单字符
			char chinese = 0;
			// 5.去除掉文件名中的中文
			// 将字符串转换成char[]
			char[] charArray = str.toCharArray();
			// 过滤到中文及中文字符
			for (int i = 0; i < charArray.length; i++) {
				chinese = charArray[i];
				flag2 = isChinese(chinese);
				if (!flag2) {// 不是中日韩文字及标点符号
					sb.append(chinese);
				}
			}
			result = sb.toString();
		}
		return result;
	}

	/**
	 * 判断字符串中是否包含中文
	 * 
	 * @param str
	 *            待校验字符串
	 * @return 是否为中文
	 * @warn 不能校验是否为中文标点符号
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 判定输入的是否是汉字
	 * 
	 * @param c
	 *            被校验的字符
	 * @return true代表是汉字
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	
	/**
	 * 为空替换字符串
	 * @param str
	 * @param repStr
	 * @return
	 */
	public static String nvl(String str, String repStr){
		if(str == null || "null".equals(str) || "".equals(str))
			return repStr;
		else 
			return str;
	}

/*	public static void main(String[] args) {
		String fileName = "朝阳区东地天澜小区002";
		System.out.println(Integer.parseInt(filterChinese(fileName))-1);
		System.out.println(filterChinese(fileName));
		
		String str = "0102";
		System.out.println(str.substring(0,2));
		System.out.println(str.substring(2));
		int num = Integer.parseInt(str);
		System.out.println(num);
		System.out.println(str.indexOf(num+""));
		System.out.println(str.substring(0, str.indexOf(num+"")));
	}*/
}
