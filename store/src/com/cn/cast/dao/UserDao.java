package com.cn.cast.dao;

import java.sql.SQLException;

import com.cn.cast.domain.User;

public interface UserDao {

	 void userRegister(User user) throws SQLException;

	 User userAction(String code) throws SQLException;

	 void updateUser(User user) throws SQLException;

	 User userLogin (User user) throws SQLException;
	 
	 

}
