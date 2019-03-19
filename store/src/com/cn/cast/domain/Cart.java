package com.cn.cast.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {
	//个数不确定的购物项
	Map<String,CartItem> map = new HashMap<>();
	//总积分
	private double total;
	
	//添加购物项到购物车
	//当用户点击加入购物车按钮时，可以将当前要购买的商品id,商品数量发送到服务端，服务端可以根据商品id查询到商品信息
	//有了商品的product信息和要购买的数量就可以获得当前的购物项
	public void addCartItemToCart(CartItem cartitem) {
		//将当前购物项加入购物车之前，判断之前是否买过这类商品
		//如果没有买过list.add(CartItem)
		//如果买过了,获取原先的数量，现在要购买的数量，相加设置在CartItem上
		
		//获取到正在向购物车中添加的商品的pid
		String pid = cartitem.getProduct().getCid();
		if(map.containsKey(pid)) {
			//获取到原先的购物项
			CartItem oldItem = map.get(pid);
			//将新的购物数量放入之前的购物项中
			oldItem.setNum(oldItem.getNum()+cartitem.getNum());
		}else {
			map.put(pid, cartitem);
		}
	}
		
	//移除购物项
	public void removeCartItem(String pid) {
		map.remove(pid);
	}
	//清空购物车
	public void clearCart() {
		map.clear();
	}
	
	//总计可以通过计算获取
	public double getTotal() {
		///先让总计清零
		total = 0;
		//获取map所有的购物项
		Collection<CartItem> items = map.values();
		
		//遍历所有的购物项，将购物项上的小计相加
		for(CartItem cart:items) {
			total+=cart.getSubTotal();
		}
		return total;
	}
	
	//返回map中所有的数据-》用来判断map为不为空
	public Collection<CartItem> getCartItems() {
		return map.values();
	}
}
