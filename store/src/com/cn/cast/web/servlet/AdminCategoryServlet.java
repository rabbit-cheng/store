package com.cn.cast.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.cast.domain.Category;
import com.cn.cast.service.CategoryService;
import com.cn.cast.service.serviceImpl.CategoryServiceImpl;
import com.cn.cast.utils.UUIDUtils;
import com.cn.cast.web.base.BaseServlet;

/**
 * Servlet implementation class AdminCategoryServlet
 */
public class AdminCategoryServlet extends BaseServlet {
	
	//findAllCats
	public String findAllCats(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//调用业务层获取全部分类信息放入request中
		CategoryService category = new CategoryServiceImpl();
		List<Category> list = category.getAllCats();
		request.setAttribute("allCats", list);
		//转发到admin/category/list.jsp
		return "/admin/category/list.jsp";
	}
	
	//addCategoryUI
	public String addCategoryUI(HttpServletRequest request, HttpServletResponse response) {
		return "/admin/category/add.jsp";
	}

	//addCategory
	public String addCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取分类名称
		String cname = request.getParameter("cname");
		//创建分类ID
		String cid = UUIDUtils.getId();
		Category c = new Category();
		c.setCid(cid);
		c.setCname(cname);
		//调用业务层添加分类功能
		CategoryService category = new CategoryServiceImpl();
		category.addCategory(c);
		//重定向到查询全部分类信息
		response.sendRedirect("/store/AdminCategoryServlet?method=findAllCats");
		return null;
	}
}
