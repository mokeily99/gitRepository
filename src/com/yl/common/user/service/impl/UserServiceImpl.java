package com.yl.common.user.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yl.common.user.dao.UserDao;
import com.yl.common.user.pojo.User;
import com.yl.common.user.pojo.UserView;
import com.yl.common.user.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource
	private UserDao userDao;
	
	public UserView getUserView(String maxaccept) {
		return userDao.getUserView(maxaccept);
	}

}
