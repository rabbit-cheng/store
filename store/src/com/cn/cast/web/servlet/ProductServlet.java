package com.cn.cast.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.cast.domain.Product;
import com.cn.cast.service.ProductService;
import com.cn.cast.service.serviceImpl.ProductServicelmpl;
import com.cn.cast.utils.PageModel;
import com.cn.cast.web.base.BaseServlet;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {
	
    protected String findProductByPid(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//获得商品的参数pid
    	String pid = request.getParameter("pid");
    	//根据商品的pid查询商品详情
    	ProductService ProductService = new ProductServicelmpl();
    	Product product = ProductService.findProductByPid(pid);
    	//将获取到的商品放入request中
    	request.setAttribute("product", product);
    	//转发到/jsp/product_info.jsp
    	return "/jsp/product_info.jsp";
    }
    
    //findProductsByCidWithPage
    protected String findProductsByCidWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//获取cid，num
    	String cid = request.getParameter("cid");
    	int currentNum = Integer.parseInt(cid);
    	//调用业务层以分页形式查询当前类别下的商品信息
    	ProductService ProductService = new ProductServicelmpl();
    	//返回PageModel对象（1，当前页商品信息，2.分页 3.url）
    	PageModel pm = ProductService.findProductsByCidWithPage(cid,currentNum);
    	//将PageModel对象放入request对象中
    	request.setAttribute("page",pm);
    	//转发jsp
    	return "/jsp/product_list.jsp";
    }
    
}
