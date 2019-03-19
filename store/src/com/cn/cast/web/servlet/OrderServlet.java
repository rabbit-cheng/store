package com.cn.cast.web.servlet;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.cast.domain.Cart;
import com.cn.cast.domain.CartItem;
import com.cn.cast.domain.Order;
import com.cn.cast.domain.OrderItem;
import com.cn.cast.domain.User;
import com.cn.cast.service.OrderService;
import com.cn.cast.service.serviceImpl.OrderServiceImpl;
import com.cn.cast.utils.PageModel;
import com.cn.cast.utils.PaymentUtil;
import com.cn.cast.utils.UUIDUtils;
import com.cn.cast.web.base.BaseServlet;

/**
 * Servlet implementation class OrderServlet
 */

public class OrderServlet extends BaseServlet {
	//savaOrder:将购物车里面的信息以订单形式保存
	public String saveOrder(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//确认用户登录状态
		User user = (User)request.getSession().getAttribute("loginUser");
		if(null == user) {
			request.setAttribute("msg", "请登录后下单");
			return "/jsp/info.jsp";
		}
		
		//获取购物车
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		//创建订单，为订单对象赋值
		Order order = new Order();
		order.setOid(UUIDUtils.getCode());
		order.setOrdertime(new Date());
		order.setTotal(cart.getTotal());
		order.setState(1);
		order.setUser(user);
		//通过遍历购物车---创建订单项
		for(CartItem item:cart.getCartItems()) {
			OrderItem orderitem = new OrderItem();
			orderitem.setItemid(UUIDUtils.getCode());
			orderitem.setQuantity(item.getNum());
			orderitem.setTotal(item.getSubTotal());
			orderitem.setProduct(item.getProduct());
			
			//设置当前订单项是属于哪个订单的，从程序的角度来实现订单项和订单的关系
			orderitem.setOrder(order);
			order.getList().add(orderitem);//将订单项加入订单的list集合中
		}
		//调用业务层功能，保存订单
		OrderService OrderService = new OrderServiceImpl();
		//将订单数据，用户数据，订单下面的订单项都传入了service中
		OrderService.saveOrder(order);
		//清空购物车
		cart.clearCart();
		//将订单放入request
		request.setAttribute("order",order);
		//转发jsp/orderinfo.jsp
		return "jsp/order_info.jsp";
	}
	
	//findMyOrderWithPage
	public String findMyOrderWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//获取当前用户信息
		User user = (User)request.getSession().getAttribute("loginUser");
		//获取当前页
		int curNum = Integer.parseInt(request.getParameter("num"));
		//调用业务层功能 查询用户当前页的订单信息-返回pagemodel
		OrderService OrderService = new OrderServiceImpl();
		
		//select * from orders where uid=? limit ?,?
		//PageModel:1。分页参数2，url3.当前用户的当前页的订单集合，以及订单下的订单项和商品信息
		PageModel pm = OrderService.findMyOrderWithPage(user,curNum);
		//将pagemodel放入request中
		request.setAttribute("page",pm);
		//返回页面
		return "/jsp/order_list.jsp";
	}
	//findOrderByOid 实现订单详情查询
	public String findOrderByOid(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取订单的oid
		String oid = request.getParameter("oid");
		//调用业务层功能：根据订单的编号查询订单信息（订单项和订单项下面的商品信息）
		OrderService OrderService = new OrderServiceImpl();
		Order order = OrderService.findOrderByOid(oid);
		//将订单放入request中
		request.setAttribute("order", order);
		//返回页面
		return "jsp/order_info.jsp";
	}
	
	//payOrder
	public String payOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//获取收件人地址，姓名，电话，银行，订单编号
		String oid = request.getParameter("oid");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String telephone = request.getParameter("telephone");
		String pd_FrpId = request.getParameter("pd_FrpId");
		
		//更新订单上收货人的地址，姓名和电话
		OrderService orderService = new OrderServiceImpl();
		Order order = orderService.findOrderByOid(oid);
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		
		//调用业务层功能更新这笔订单信息
		orderService.updateOrder(order);
		//向易宝支付发送参数
		// 把付款所需要的参数准备好:
				String p0_Cmd = "Buy";
				//商户编号
				String p1_MerId = "10001126856";
				//订单编号
				String p2_Order = oid;
				//金额
				String p3_Amt = "0.01";
				String p4_Cur = "CNY";
				String p5_Pid = "";
				String p6_Pcat = "";
				String p7_Pdesc = "";
				//接受响应参数的Servlet
				String p8_Url = "http://localhost:8089/store/OrderServlet?method=callBack";
				String p9_SAF = "";
				String pa_MP = "";
				String pr_NeedResponse = "1";
				//公司的秘钥
				String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
					
				//调用易宝的加密算法,对所有数据进行加密,返回电子签名
				String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
						
				StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
				sb.append("p0_Cmd=").append(p0_Cmd).append("&");
				sb.append("p1_MerId=").append(p1_MerId).append("&");
				sb.append("p2_Order=").append(p2_Order).append("&");
				sb.append("p3_Amt=").append(p3_Amt).append("&");
				sb.append("p4_Cur=").append(p4_Cur).append("&");
				sb.append("p5_Pid=").append(p5_Pid).append("&");
				sb.append("p6_Pcat=").append(p6_Pcat).append("&");
				sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
				sb.append("p8_Url=").append(p8_Url).append("&");
				sb.append("p9_SAF=").append(p9_SAF).append("&");
				sb.append("pa_MP=").append(pa_MP).append("&");
				sb.append("pd_FrpId=").append(pd_FrpId).append("&");
				sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
				sb.append("hmac=").append(hmac);

				System.out.println(sb.toString());
				// 使用重定向：
				response.sendRedirect(sb.toString());

				//response.sendRedirect("https://www.yeepay.com/app-merchant-proxy/node?p0_cmd=Buy&p1_MerId=111111&k1=v1&k2=v2");
			
				return null;
	}
	//callBack
	public String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//接受易宝支付
				// 验证请求来源和数据有效性
				// 阅读支付结果参数说明
				// System.out.println("==============================================");
				String p1_MerId = request.getParameter("p1_MerId");
				String r0_Cmd = request.getParameter("r0_Cmd");
				String r1_Code = request.getParameter("r1_Code");
				String r2_TrxId = request.getParameter("r2_TrxId");
				String r3_Amt = request.getParameter("r3_Amt");
				String r4_Cur = request.getParameter("r4_Cur");
				String r5_Pid = request.getParameter("r5_Pid");
				String r6_Order = request.getParameter("r6_Order");
				String r7_Uid = request.getParameter("r7_Uid");
				String r8_MP = request.getParameter("r8_MP");
				String r9_BType = request.getParameter("r9_BType");
				String rb_BankId = request.getParameter("rb_BankId");
				String ro_BankOrderId = request.getParameter("ro_BankOrderId");
				String rp_PayDate = request.getParameter("rp_PayDate");
				String rq_CardNo = request.getParameter("rq_CardNo");
				String ru_Trxtime = request.getParameter("ru_Trxtime");

				// hmac
				String hmac = request.getParameter("hmac");
				// 利用本地密钥和加密算法 加密数据
				String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
				boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
						r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
						r8_MP, r9_BType, keyValue);
				if (isValid) {
					// 有效
					if (r9_BType.equals("1")) {
						//如果支付成功，更新订单状态
						OrderService orderservice = new OrderServiceImpl();
						Order order = orderservice.findOrderByOid(r6_Order);
						order.setState(2);
						orderservice.updateOrder(order);
						//向request中放入提示信息
						request.setAttribute("msg", "支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
						//转发至、jsp/info.jsp
						return "/jsp/info.jsp";
					} else if (r9_BType.equals("2")) {
						// 修改订单状态:
						// 服务器点对点，来自于易宝的通知
						System.out.println("收到易宝通知，修改订单状态！");//
						// 回复给易宝success，如果不回复，易宝会一直通知
						response.getWriter().print("success");
					}

				} else {
					throw new RuntimeException("数据被篡改！");
				}
				
		//如果支付成功，更新订单状态
		//向request中放入提示信息
		//转发至、jsp/info.jsp
		return "/jsp/info.jsp";
	}
}

