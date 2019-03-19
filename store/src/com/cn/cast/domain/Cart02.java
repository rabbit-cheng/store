package com.cn.cast.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 * @author 123
 *
 */
public class Cart02 {
	//个数不确定的购物项
	List<CartItem> list = new ArrayList<>();
	//总计、总积分
	private double total;
	//添加购物项到购物车
	//当用户点击加入购物车按钮时，可以将当前要购买的商品id,商品数量发送到服务端，服务端可以根据商品id查询到商品信息
	//有了商品的product信息和要购买的数量就可以获得当前的购物项
	public void addCartItemToCart(CartItem cartitem) {
		//将当前购物项加入购物车之前，判断之前是否买过这类商品
		//如果没有买过list.add(CartItem)
		//如果买过了,获取原先的数量，现在要购买的数量，相加设置在CartItem上
		
		//设置变量：默认为false,没有买过该商品
		boolean flag = false;
		
		CartItem old = null;//存储原先的购物项
		for(CartItem ct2:list) {
			if(ct2.getProduct().getCid().equals(cartitem.getProduct().getCid())) {
				flag = true;	
				old = ct2;
			}
		}
		
		if(flag==false) {
			list.add(cartitem);
		}else {//如果买过了,获取原先的数量，现在要购买的数量，相加设置在CartItem上
			old.setNum(old.getNum()+cartitem.getNum());;
		}
	}
	
	
	
	
	//移除购物项
	//当用户点击移除商品时，可以将当前商品的id发送给服务端
	public void removeCartItem(String cid) {
		//遍历List,看CartItem里面的product的cid是否和服务端获取到的一致，若一致则删除当前项
		for(CartItem ct:list) {
			if(ct.getProduct().getCid().equals(cid)) {
				//删除当前的CartItem
				//直接调用list.remove(CartItem)无法删除，需通过迭代器删除当前的购物项
			}
		}
		
	}
	//清空购物车
	public void clearCart() {
		list.clear();
	}
}
