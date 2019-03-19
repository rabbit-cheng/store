package com.cn.cast.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.cn.cast.domain.User;

/**
 * Servlet Filter implementation class PriviledgeFilter
 */
public class PriviledgeFilter implements Filter {

    public PriviledgeFilter() {
    }

	public void destroy() {
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//判断当前session中是否存在已经登录成功的用户
		HttpServletRequest myRequest = (HttpServletRequest)request;
		User user = (User)myRequest.getSession().getAttribute("loginUser");
		if(null != user) {
			//如果用户已经登录成功则放行
			chain.doFilter(request, response);
		}else{
			//用户未登录，转入提示页面
			myRequest.setAttribute("msg", "请先登录");
			myRequest.getRequestDispatcher("/jsp/info.jsp").forward(request, response);
		}
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
