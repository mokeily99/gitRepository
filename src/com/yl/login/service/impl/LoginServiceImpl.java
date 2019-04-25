package com.yl.login.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yl.common.user.pojo.UserView;
import com.yl.login.dao.LoginDao;
import com.yl.login.service.LoginService;

@Service("loginService")
public class LoginServiceImpl implements LoginService{

	@Resource  
	private LoginDao loginDao; 
	
	public UserView getUserLogin(Map<String, String> param) {
		return loginDao.getUserLogin(param);
	}

	public void updateUserOnline(Map<String, String> param) {
		loginDao.updateUserOnline(param);
	}

	@Override
	public String getUserPwd(Map<String, String> para) {
		return loginDao.getUserPwd(para);
	}

	@Override
	public void updateUserPwd(Map<String, String> param) {
		loginDao.updateUserPwd(param);
	}

}
