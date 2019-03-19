package com.cn.cast.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.cast.domain.Category;
import com.cn.cast.domain.Product;
import com.cn.cast.service.CategoryService;
import com.cn.cast.service.ProductService;
import com.cn.cast.service.serviceImpl.CategoryServiceImpl;
import com.cn.cast.service.serviceImpl.ProductServicelmpl;
import com.cn.cast.web.base.BaseServlet;

public class IndexServlet extends BaseServlet {
	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception{
		/*//调用业务层：获取全部分类信息，返回集合
		CategoryService categoryService = new CategoryServiceImpl();
		List<Category> list = categoryService.getAllCats();
		//将集合放入request中
		request.setAttribute("allCats", list);*/
		//调用业务层查询最新，最热商品返回两个集合
		ProductService ProductService = new ProductServicelmpl();
		List<Product> list01 = ProductService.findHots();
		List<Product> list02 = ProductService.findNews();
		//将两个集合放入request中
		request.setAttribute("hots", list01);
		request.setAttribute("news", list02);
		//放回真实页面
		return "/jsp/index.jsp";
	}
}
