package com.cn.cast.dao.daoImpl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.cn.cast.dao.ProductDao;
import com.cn.cast.domain.BeanFactory;
import com.cn.cast.domain.Product;
import com.cn.cast.utils.JDBCUtils;

public class ProductDaoImpl implements ProductDao{

	ProductDao productdao = (ProductDao)BeanFactory.createObject("productdao");
	@Override
	public List<Product> findHots() throws Exception {
		String sql = "select * from product where pflag=0 and is_hot=1 ORDER BY pdate DESC limit 0,9";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Product>(Product.class));
	}

	@Override
	public List<Product> findNews() throws Exception {
		String sql = "select * from product where pflag=0 ORDER BY pdate DESC limit 0,9";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Product>(Product.class));
	}

	@Override
	public Product findProductByPid(String pid) throws Exception {
		String sql = "select * from product where pid=?";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanHandler<Product>(Product.class),pid);
	}

	
	//统计当前分类下面的记录总数
	@Override
	public int findTotalRecords(String cid) throws Exception {
		String sql = "select count(*) from product where cid=?";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Long count = (Long)qr.query(sql, new ScalarHandler(),cid);
		return count.intValue();
	}

	//查询当前页面下的商品信息
	@Override
	public List findProductsByCidWithPage(String cid, int startIndex, int pageSize) throws Exception {
		String sql = "select * from product where cid=? limit ?,?";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql,new BeanListHandler<Product>(Product.class),cid,startIndex,pageSize);
	}

	@Override
	public int findTotalRecords() throws Exception {
		String sql = "select count(*) from product";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Long count = (Long)qr.query(sql, new ScalarHandler());
		return count.intValue();
	}

	@Override
	public List<Product> findAllProductsWithPage(int startIndex, int pageSize) throws Exception {
		String sql = "select * from product orderby pdate desc limit ?, ?";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Product>(Product.class),startIndex,pageSize);
	}

	@Override
	public void saveProduct(Product product) throws Exception {
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Object[] obj = {product.getPid(),product.getPname(),product.getMarket_price(),product.getShop_price(),
						product.getPimage(),product.getPdate(),product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCid()};
		qr.update(sql, obj);
	}

}
