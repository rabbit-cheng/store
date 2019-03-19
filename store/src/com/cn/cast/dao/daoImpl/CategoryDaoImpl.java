package com.cn.cast.dao.daoImpl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.cn.cast.dao.CategoryDao;
import com.cn.cast.domain.BeanFactory;
import com.cn.cast.domain.Category;
import com.cn.cast.utils.JDBCUtils;

public class CategoryDaoImpl implements CategoryDao{

	CategoryDao categoryDao = (CategoryDao)BeanFactory.createObject("categoryDao");
	@Override
	public List<Category> getAllCats() throws Exception {
		String sql = "select * from category";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		
		return qr.query(sql,new BeanListHandler<Category>(Category.class));
	}

	@Override
	public Object addCategory(Category c) throws Exception {
		String sql = "insert into category values(?,?)";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		return qr.update(sql,c.getCid(),c.getCname());
	}

}
