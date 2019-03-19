package com.cn.cast.dao;

import java.util.List;

import com.cn.cast.domain.Product;
import com.cn.cast.utils.PageModel;

public interface ProductDao {
	List<Product> findHots() throws Exception;
	List<Product> findNews() throws Exception;
	Product findProductByPid(String pid) throws Exception;
	//PageModel findProductsByCidWithPage(String cid, int currentNum) throws Exception;
	int findTotalRecords(String cid) throws Exception;
	List findProductsByCidWithPage(String cid, int startIndex, int pageSize) throws Exception;
	int findTotalRecords()throws Exception;
	List<Product> findAllProductsWithPage(int startIndex, int currentPageNum)throws Exception;
	void saveProduct(Product product)throws Exception;
	
}
