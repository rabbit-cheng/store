package com.cn.cast.service;

import java.util.List;

import com.cn.cast.domain.Product;
import com.cn.cast.utils.PageModel;

public interface ProductService {

	List<Product> findHots() throws Exception;
	List<Product> findNews() throws Exception;
	Product findProductByPid(String pid) throws Exception;
	PageModel findProductsByCidWithPage(String cid, int currentNum) throws Exception;
	PageModel findAllProductWithPage(int currentNum) throws Exception;
	void saveProduct(Product product) throws Exception;

}
