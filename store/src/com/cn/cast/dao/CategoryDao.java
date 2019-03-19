package com.cn.cast.dao;

import java.util.List;

import com.cn.cast.domain.Category;

public interface CategoryDao {

	List<Category> getAllCats() throws Exception;

	Object addCategory(Category c) throws Exception;

}
