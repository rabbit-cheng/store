package com.cn.cast.service.serviceImpl;

import java.util.List;

import com.cn.cast.dao.CategoryDao;
import com.cn.cast.dao.daoImpl.CategoryDaoImpl;
import com.cn.cast.domain.Category;
import com.cn.cast.service.CategoryService;
import com.cn.cast.utils.JedisUtils;

import redis.clients.jedis.Jedis;

public class CategoryServiceImpl implements CategoryService{

	@Override
	public List<Category> getAllCats() throws Exception {
		CategoryDao Categorydao = new CategoryDaoImpl();
		return Categorydao.getAllCats();
	}

	//本质是对mysql数据库插入一条数据
	@Override
	public void addCategory(Category c) throws Exception {
		CategoryDao Categorydao = new CategoryDaoImpl();
		Categorydao.addCategory(c);
		//更新redis缓存
		Jedis jedis = JedisUtils.getJedis();
		jedis.del("allCats");
		JedisUtils.closeJedis(jedis);
	}

}
