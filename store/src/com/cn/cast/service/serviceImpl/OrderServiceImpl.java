package com.cn.cast.service.serviceImpl;

import java.util.List;

import com.cn.cast.dao.OrderDao;
import com.cn.cast.dao.daoImpl.OrderDaoImpl;
import com.cn.cast.domain.Order;
import com.cn.cast.domain.OrderItem;
import com.cn.cast.domain.User;
import com.cn.cast.service.OrderService;
import com.cn.cast.utils.JDBCUtils;
import com.cn.cast.utils.PageModel;
import com.mysql.jdbc.Connection;

public class OrderServiceImpl implements OrderService {

	OrderDao orderDao = new OrderDaoImpl();
	@Override
	public void saveOrder(Order order) throws Exception {
		//保存订单和订单下所有的订单项（必须同时成功，失败）
		/*try {
			JDBCUtils.startTransaction();
			OrderDao orderDao = new OrderDaoImpl();
			orderDao.saveOrder(order);
			for(OrderItem item:order.getList()) {
				orderDao.saveOrderItem(item);
			}
			JDBCUtils.commitAndClose();
		} catch (Exception e) {
			JDBCUtils.rollbackAndClose();
		}*/
		Connection conn = null;
		try {
			//获取连接
			conn = (Connection) JDBCUtils.getConnection();
			//开启事务
			conn.setAutoCommit(false);
			//保存订单
			//OrderDao orderDao = new OrderDaoImpl();
			orderDao.saveOrder(conn,order);//conn向下传递保持是同一个连接
			//保存订单项
			for(OrderItem item:order.getList()) {
				orderDao.saveOrderItem(conn,item);
			}
			conn.commit();
		} catch (Exception e) {
			// 回滚
			conn.rollback();
		}
	}

	@Override
	public PageModel findMyOrderWithPage(User user,int curNum) throws Exception {
		//创建PageModel参数：目的：携带并计算分页参数,select count(*) from orders where uid=?
		int totalRecords = orderDao.getTotalRecords(user);
		PageModel pm = new PageModel(curNum,totalRecords,3);
	
		//关联集合
		//select * from orders where uid=? limit ?,?
		List list = orderDao.findMyOrderWithPage(user,pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		//关联url
		pm.setUrl("OrderServlet?method=findMyOrdersWithPage&num="+curNum);
		return null;
	}

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		return orderDao.findOrderByOid(oid);
	}

	@Override
	public void updateOrder(Order order) throws Exception {
		 orderDao.updateOrder(order);
	}

	@Override
	public List<Order> findAllOrders() throws Exception {
		return orderDao.findAllOrders();
	}

}
