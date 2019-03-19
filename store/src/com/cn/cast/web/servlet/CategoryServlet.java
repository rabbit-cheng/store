package com.cn.cast.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.cast.domain.Category;
import com.cn.cast.service.CategoryService;
import com.cn.cast.service.serviceImpl.CategoryServiceImpl;
import com.cn.cast.utils.JedisUtils;
import com.cn.cast.web.base.BaseServlet;

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;
/*
 * 分类 servlet
 */
public class CategoryServlet extends BaseServlet {
	
	public String getAllCats(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//在redis中获取全部分类信息
		Jedis jedis = JedisUtils.getJedis();
		String strJson = jedis.get("allcats");
		
		if(null==strJson || "".equals(strJson)) {
			//调用业务层功能：获取分类集合
			CategoryService cateService = new CategoryServiceImpl();
			List<Category> list = cateService.getAllCats();
			//将集合封装成json格式
			strJson = JSONArray.fromObject(list).toString();
			
			System.out.println(strJson);
			
			//将json格式的字符串放进去
			jedis.set("allcats", strJson);
			//将数据传给服务端并告诉服务端本次传输的是json格式的数据
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(strJson);
		}else {
			System.out.println("redis里面有对象");
			//将数据传给服务端并告诉服务端本次传输的是json格式的数据
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(strJson);
		}
		
		return null;
	}
}
