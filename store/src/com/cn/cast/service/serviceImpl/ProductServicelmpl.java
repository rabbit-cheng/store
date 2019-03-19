package com.cn.cast.service.serviceImpl;

import java.util.List;

import com.cn.cast.dao.ProductDao;
import com.cn.cast.dao.daoImpl.ProductDaoImpl;
import com.cn.cast.domain.Product;
import com.cn.cast.service.ProductService;
import com.cn.cast.utils.PageModel;
import com.cn.cast.web.servlet.ProductServlet;

public class ProductServicelmpl implements ProductService {

	@Override
	public List<Product> findHots() throws Exception {
		ProductDao productDao = new ProductDaoImpl();
		return productDao.findHots();
	}

	@Override
	public List<Product> findNews() throws Exception {
		ProductDao productDao = new ProductDaoImpl();
		return productDao.findNews();
	}

	@Override
	public Product findProductByPid(String pid) throws Exception {
		ProductDao productDao = new ProductDaoImpl();
		return productDao.findProductByPid(pid);
	}

	@Override
	public PageModel findProductsByCidWithPage(String cid, int currentNum) throws Exception {
		//创建PageModel对象，目的：计算分页参数
		ProductDao ProductDao = new ProductDaoImpl();
		
		//统计当前分页下的产品个数select count(*) from product where cid=?
		int totalRecords = ProductDao.findTotalRecords(cid);
		PageModel pm = new PageModel(currentNum,totalRecords,12);
		//关联集合 c查询当前页的商品信息select * from product where cid=? limit ?,?
		List list = ProductDao.findProductsByCidWithPage(cid,pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		//关联url
		pm.setUrl("ProductServlet?method=findProductsByCidWithPage&cid="+cid);
		return pm;
	}

	@Override
	public PageModel findAllProductWithPage(int currentNum) throws Exception {
		// 1.创建对象
		ProductDao ProductDao = new ProductDaoImpl();
		int totalRecords = ProductDao.findTotalRecords();
		System.out.println(totalRecords);
		PageModel pm = new PageModel(currentNum,totalRecords,5);
		
		//2.关联集合select * from product limit?,?
		List<Product> list = ProductDao.findAllProductsWithPage(pm.getStartIndex(),pm.getPageSize());
		System.out.println(list);
		pm.setList(list);
		System.out.println(pm);
		//3.关联url
		pm.setUrl("AdminProductServlet?method=findAllProductsWithPage");
		return pm;
	}

	@Override
	public void saveProduct(Product product) throws Exception {
		ProductDao productdao = new ProductDaoImpl();
		productdao.saveProduct(product);
		
	}

}
