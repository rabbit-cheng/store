package com.cn.cast.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.cast.domain.Order;
import com.cn.cast.service.OrderService;
import com.cn.cast.service.serviceImpl.OrderServiceImpl;
import com.cn.cast.web.base.BaseServlet;

/**
 * Servlet implementation class AdminOrdersServlet
 */
public class AdminOrdersServlet extends BaseServlet {
	//findOrders
	public String findOrders(HttpServletRequest request,HttpServletResponse response)throws Exception {
		//获取全部订单
		OrderService orderService = new OrderServiceImpl();
		List<Order> list = orderService.findAllOrders();
		//将全部订单放入request中
		request.setAttribute("allOrders", list);
		return "/admin/order/list.jsp";
	}

}
