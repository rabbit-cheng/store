package com.cn.cast.service.serviceImpl;

import java.sql.SQLException;

import com.cn.cast.dao.UserDao;
import com.cn.cast.dao.daoImpl.UserDaoImpl;
import com.cn.cast.domain.User;
import com.cn.cast.service.UserService;

public class UserServiceImpl implements UserService{

	@Override
	public void userRegist(User user) throws SQLException{
		//实现注册功能
		UserDao userdao = new UserDaoImpl();
		userdao.userRegister(user);
		
	}

	@Override
	public boolean userActive(String code) throws SQLException{

		UserDao userdao = new UserDaoImpl();
		//对DB发生一个命令，select * from user where code=?
		User user = userdao.userAction(code);
		
		if(null != user) {
			//可以根据激活码查到一个用户
			//修改用户状态，清除激活码
			user.setState(1);
			user.setCode(null);
			//对数据库执行一次真实的更新操作update user set state=1,code=null where uid=?
			//update user set username=?,password=?,name=?,email=?,telephone=?,birthday=?,sex=?,state=?,code=? where uid=?
			userdao.updateUser(user);
			return true;
		}else {
			//不可以根据激活码查到一个用户
			return false;
		}
	}

	@Override
	public User userLogin(User user) throws SQLException {
		//可以利用异常在模块之间传递一些数据
		
		UserDao userdao = new UserDaoImpl();
		//select * from user where username=? and password=?
		User uu = userdao.userLogin(user);
		if(null == user) {
			throw new RuntimeException("密码有误！");
		}else if (uu.getState() == 0){
			throw new RuntimeException("用户未激活！");
		}else {
			return uu;
		}
	}

}
