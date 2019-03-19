package com.cn.cast.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.cn.cast.domain.Category;
import com.cn.cast.domain.User;
import com.cn.cast.service.CategoryService;
import com.cn.cast.service.UserService;
import com.cn.cast.service.serviceImpl.CategoryServiceImpl;
import com.cn.cast.service.serviceImpl.UserServiceImpl;
import com.cn.cast.utils.MailUtils;
import com.cn.cast.utils.MyBeanUtils;
import com.cn.cast.utils.UUIDUtils;
import com.cn.cast.web.base.BaseServlet;

public class UserServlet extends BaseServlet{
	
	//实现一个空跳转-跳转到 注册jsp
	public String registUI(HttpServletRequest request, HttpServletResponse response){
		return "/jsp/register.jsp";
	}
	
	//loginUI
	public String loginUI(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		//调用业务层：获取全部分类信息，返回集合
		CategoryService categoryService = new CategoryServiceImpl();
		List<Category> list = categoryService.getAllCats();
		//将集合放入request中
		request.setAttribute("allCats", list);
		return "/jsp/login.jsp";
	}
	//用户注册
	public String userRegist(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//接收表单参数
		Map<String,String[]> map = request.getParameterMap();
		User user = new User();
		MyBeanUtils.populate(user, map);
		
		//为用户的其他属性赋值
		user.setUid(UUIDUtils.getId());
		user.setState(0);
		user.setCode(UUIDUtils.getCode());
		
		
						/*遍历map
						Set<String> keyset = map.keySet();
						Iterator<String> iterator = keyset.iterator();
						while(iterator.hasNext()) {
							String str = iterator.next();
							System.out.println(str);
							String[] strs = map.get(str);
							for(String s:strs) {
								System.out.println(s);
							}
						}*/
		//调用业务层 注册功能
		UserService userService = new UserServiceImpl();
		try {
			userService.userRegist(user);
			//注册成功向用户邮箱发邮件，跳转提示页面
			//TODO
			MailUtils.sendMail(user.getEmail(), user.getCode());
			request.setAttribute("msg","用户注册成功！请激活");
		} catch (Exception e) {
			// 注册失败，跳转提示页面
			request.setAttribute("msg","用户注册成功！请激活");
		}
		return "/jsp/info.jsp";
	}
	
	//激活用户
	public String active(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//获取激活码
		String code = request.getParameter("code");
		//调用业务层进行激活
		UserService userService = new UserServiceImpl();
		boolean flag = userService.userActive(code);
		//显示激活信息
		if(flag) {//用户激活成功，向request放入提示信息，转发到登录页面
			request.setAttribute("msg","用户激活成功！请登录");
			return "/jsp/login.jsp";
		}else {
			//用户激活失败，请重新激活
			request.setAttribute("msg","用户激活失败！请重新激活");
			return "/jsp/info.jsp";
		}
	}
	
	//用户登录 userLogin
	public String userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//获取用户数据（账号、密码）
		User user = new User();
		MyBeanUtils.populate(user, request.getParameterMap());
		
		//调用业务层登录功能
		UserService userService = new UserServiceImpl();
		User user02 = userService.userLogin(user);
		
		try {
			//用户登录成功，将用户信息放入session中，然后跳转到首页
			request.getSession().setAttribute("loginUser", user02);
			response.sendRedirect("/store/index.jsp");
			return null;
		} catch (Exception e) {
			//用户登录失败
			String msg = e.getMessage();
			System.out.println(msg);
			//将失败信息放入request中
			request.setAttribute("msg",msg);
			return "/jsp/login.jsp";
		}
	}
	//用户退出 logOut
	public String logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//清除session
		request.getSession().invalidate();
		//重新定向到首页
		response.sendRedirect("/jsp/index.jsp");
		return null;
	}
}
