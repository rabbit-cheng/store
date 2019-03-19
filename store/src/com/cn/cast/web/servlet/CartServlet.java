package com.cn.cast.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.cast.domain.Cart;
import com.cn.cast.domain.CartItem;
import com.cn.cast.domain.Product;
import com.cn.cast.service.ProductService;
import com.cn.cast.service.serviceImpl.ProductServicelmpl;
import com.cn.cast.web.base.BaseServlet;

public class CartServlet extends BaseServlet{
	
	//添加购物车购物项到购物车
	public String addCartItemToCart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//从Session获取购物车
		
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		if(null == cart) {//如果获取不到，创建购物车对象，放在session中
			cart = new Cart();
			request.getSession().setAttribute("cart",cart);
		}
		//如果获取到了，使用
		//获取商品的id,数量
		String pid = request.getParameter("pid");
		int num = Integer.parseInt(request.getParameter("quantity"));
		
		//通过商品的id,查询该商品对象
		ProductService ProductService = new ProductServicelmpl();
		Product product = ProductService.findProductByPid(pid);
		
		//获取到待购买的购物项
		CartItem cartitem = new CartItem();
		cartitem.setNum(num);
		cartitem.setProduct(product);
		
		//调用购物车的方法
		cart.addCartItemToCart(cartitem);
		//重定向到。jsp.cart。jsp页面
		response.sendRedirect("/store/jsp/cart.jsp");
		return null;
	}
	
	//removeCartItem
	public String removeCartItem(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取待删除商品的pid
		String pid = request.getParameter("id");
		//获取购物车
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		cart.removeCartItem(pid);
		
		response.sendRedirect("/store/jsp/cart.jsp");
		return null;
	}
	
	//clearItem
	public String clearItem(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取购物车
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		//调用方法
		cart.clearCart();
		response.sendRedirect("/store/jsp/cart.jsp");
		return null;
	}
}
