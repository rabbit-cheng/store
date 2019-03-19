package com.cn.cast.service;

import java.util.List;

import com.cn.cast.domain.Order;
import com.cn.cast.domain.User;
import com.cn.cast.utils.PageModel;

public interface OrderService {

	void saveOrder(Order order) throws Exception;

	PageModel findMyOrderWithPage(User user, int curNum) throws Exception;

	Order findOrderByOid(String oid) throws Exception;

	void updateOrder(Order order) throws Exception;

	List<Order> findAllOrders()throws Exception;

}
