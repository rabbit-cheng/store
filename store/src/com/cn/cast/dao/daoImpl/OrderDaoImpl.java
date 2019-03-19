package com.cn.cast.dao.daoImpl;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.cn.cast.dao.OrderDao;
import com.cn.cast.domain.Order;
import com.cn.cast.domain.OrderItem;
import com.cn.cast.domain.Product;
import com.cn.cast.domain.User;
import com.cn.cast.utils.JDBCUtils;
import com.mysql.jdbc.Connection;

public class OrderDaoImpl implements OrderDao {

	@Override
	public void saveOrder(Connection conn, Order order) throws Exception {
		String sql = "INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)";
		QueryRunner qr = new QueryRunner();//使用了事物不用传参
		Object[] params = {order.getOid(),order.getOrdertime(),order.getTotal(),
							order.getState(),order.getAddress(),order.getName(),order.getTelephone(),
							order.getUser().getUid()}; 
		qr.update(conn,sql,params);
	}

	@Override
	public void saveOrderItem(Connection conn, OrderItem item) throws Exception {
		String sql = "INSERT INTO orderitem VALUES(?,?,?,?,?)";
		QueryRunner qr = new QueryRunner();//使用了事物不用传参
		Object[] params = {item.getItemid(),item.getQuantity(),item.getTotal(),
							item.getProduct().getPid(),item.getOrder().getOid()}; 
		qr.update(conn,sql,params);
	}

	@Override
	public int getTotalRecords(User user) throws Exception {
		String sql = "select count(*) from orders where uid=?";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Long num = (Long)qr.query(sql, new ScalarHandler(),user.getUid());
		return num.intValue();
	}

	@Override
	public List findMyOrderWithPage(User user, int startIndex, int pageSize) throws Exception {
		String sql = "select * from orders where uid=? limit ?,?";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		List<Order> list = qr.query(sql, new BeanListHandler<Order>(Order.class),user.getUid(),startIndex,pageSize);
		//仅查出了商品的订单，还没有订单项
		//遍历所有的订单
		for(Order Order:list) {
			//获取到每笔订单的oid,查询到每笔订单下的所有订单项和对应商品信息
			String oid = Order.getOid();
		    sql = "select * from orderItem o, product p where o.pid=p.pid and oid=?";
		    List<Map<String,Object>> list02 = qr.query(sql, new MapListHandler(),oid);
			//遍历list02
		    for(Map<String,Object> map:list02) {
		    	OrderItem orderItem = new OrderItem();
		    	Product product = new Product();
		    	//由于BeanUtils将字符串“1993-4-4”向user对象中的setBirthday(),方法传递参数有问题，手动向BeanUtils注册一个时间转换点
		    	//创建时间转换类型
		    	DateConverter dt = new DateConverter();
		    	//设置时间转换的格式
		    	dt.setPattern("yyyy-MM-dd");
		    	//注册转换器
		    	ConvertUtils.register(dt, java.util.Date.class);
		    	
		    	//将map中属于orderItem的数据自动填充到orderItem对象上
		    	BeanUtils.populate(orderItem,map);
		    	//将map中属于product的数据自动填充到product上
		    	BeanUtils.populate(product,map);
		    	
		    	System.out.println(orderItem);
		    	System.out.println(product);
		    	//让每个订单项和商品发生关联关系
		    	orderItem.setProduct(product);
		    	//让每个订单项存入订单下的集合中
		    	Order.getList().add(orderItem);
		    }
		}
		return list;
	}

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		String sql = "select * from orders where oid=?";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		
		//根据该笔订单的订单号去查找里面的订单项和订单项上面关联的商品
		sql = "select * from orderitem o,product p where o.pid=p.pid and oid=?";
		List<Map<String,Object>> list02 = qr.query(sql, new MapListHandler(),oid);
		
		for(Map<String,Object> map:list02) {
			OrderItem orderItem = new OrderItem();
	    	Product product = new Product();
	    	//由于BeanUtils将字符串“1993-4-4”向user对象中的setBirthday(),方法传递参数有问题，手动向BeanUtils注册一个时间转换点
	    	//创建时间转换类型
	    	DateConverter dt = new DateConverter();
	    	//设置时间转换的格式
	    	dt.setPattern("yyyy-MM-dd");
	    	//注册转换器
	    	ConvertUtils.register(dt, java.util.Date.class);
	    	
	    	//将map中属于orderItem的数据自动填充到orderItem对象上
	    	BeanUtils.populate(orderItem,map);
	    	//将map中属于product的数据自动填充到product上
	    	BeanUtils.populate(product,map);
	    	
	    	System.out.println(orderItem);
	    	System.out.println(product);
	    	//让每个订单项和商品发生关联关系
	    	orderItem.setProduct(product);
	    	//让每个订单项存入订单下的集合中
	    	order.getList().add(orderItem);
			
		}
		return order;
	}

	@Override
	public void updateOrder(Order order) throws Exception {
		String sql = "update orders set ordertime=?,total=?,state=?,address=?,name=?,telephone=? where oid=?";
		QueryRunner qr = new QueryRunner();//使用了事物不用传参
		Object[] params = {order.getOrdertime(),order.getTotal(),
				order.getState(),order.getAddress(),order.getName(),order.getTelephone()};  
		qr.update(sql,params);
		
	}

	@Override
	public List<Order> findAllOrders() throws Exception {
		String sql = "select * from orders";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		
		return qr.query(sql, new BeanListHandler<Order>(Order.class));
	}

}
