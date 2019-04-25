package com.yl.common.user.dao;

import org.apache.ibatis.annotations.Param;

import com.yl.common.user.pojo.UserView;

public interface UserDao {

	public UserView getUserView(@Param("maxaccept") String maxaccept);
}
