package com.yl.login.dao;

import java.util.Map;

import com.yl.common.user.pojo.UserView;

public interface LoginDao {

	public UserView getUserLogin(Map<String, String> param);
	
	public void updateUserOnline(Map<String, String> param);

	public String getUserPwd(Map<String, String> para);

	public void updateUserPwd(Map<String, String> param);
}
