package com.cn.cast.dao;

import java.util.List;

import com.cn.cast.domain.Order;
import com.cn.cast.domain.OrderItem;
import com.cn.cast.domain.User;
import com.mysql.jdbc.Connection;
public interface OrderDao {

	void saveOrder(Connection conn, Order order) throws Exception;

	void saveOrderItem(Connection conn, OrderItem item) throws Exception;

	int getTotalRecords(User user) throws Exception;

	List findMyOrderWithPage(User user, int startIndex, int pageSize) throws Exception;

	Order findOrderByOid(String oid) throws Exception;

	void updateOrder(Order order) throws Exception;

	List<Order> findAllOrders()throws Exception;

}
