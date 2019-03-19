package com.cn.cast.domain;

/**
 * 抽取购物车里面的一个购物项
 * @author 123
 *
 */
public class CartItem {
	private Product product;//目的：携带购物项的三种参数（图片路径，商品名称和商品价格）
	private int num;//当前类别的商品数量
	private double subTotal;//商品小计
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	
	
}
