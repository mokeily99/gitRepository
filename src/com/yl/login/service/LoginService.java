package com.yl.login.service;

import java.util.Map;
import com.yl.common.user.pojo.User;
import com.yl.common.user.pojo.UserView;

public interface LoginService {

	public UserView getUserLogin(Map<String, String> param);
	
	public void updateUserOnline(Map<String, String> param);

	public String getUserPwd(Map<String, String> para);

	public void updateUserPwd(Map<String, String> param);
}
