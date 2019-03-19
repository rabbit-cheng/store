package com.cn.cast.service;

import java.util.List;

import com.cn.cast.domain.Category;

public interface CategoryService {

	List<Category> getAllCats() throws Exception;

	void addCategory(Category c) throws Exception;

}
