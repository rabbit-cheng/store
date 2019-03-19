package com.cn.cast.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.cn.cast.domain.Category;
import com.cn.cast.domain.Product;
import com.cn.cast.service.CategoryService;
import com.cn.cast.service.ProductService;
import com.cn.cast.service.serviceImpl.CategoryServiceImpl;
import com.cn.cast.service.serviceImpl.ProductServicelmpl;
import com.cn.cast.utils.PageModel;
import com.cn.cast.utils.UUIDUtils;
import com.cn.cast.utils.UploadUtils;
import com.cn.cast.web.base.BaseServlet;

public class AdminProductServlet extends BaseServlet {
	//findAllProductsWithPage
	public String findAllProductsWithPage(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//获取当前页
		int currentNum = Integer.parseInt(request.getParameter("num"));
		//调用业务层查询全部信息返回Pagemodel
		System.out.println(currentNum);
		ProductService ProductService = new ProductServicelmpl();
		PageModel pm = ProductService.findAllProductWithPage(currentNum);
		//将pagemodel放入request中
		System.out.println(pm);
		request.setAttribute("page", pm);
		//转发至admin/product/list.jsp
		return "/admin/product/list.jsp";
	}
	//addProductUI
	public String addProductUI(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取到商品的全部分类
		CategoryService categoryService = new CategoryServiceImpl();
		List<Category> list = categoryService.getAllCats();	
		//将商品的全部分类放入request中
		request.setAttribute("allCats", list);
		//页面转发到/admin/add.jsp
		return "/admin/add.jsp";
	}
	//addProduct
	public String addProduct(HttpServletRequest request,HttpServletResponse response) {
		try {
			//利用request.getInputStream()获取到请求体中的全部数据，进行拆分再封装
			DiskFileItemFactory dff  = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(dff);
			List<FileItem> list = upload.parseRequest(request);
			
			//遍历集合
			Map<String,String> map = new HashMap<>();
			for(FileItem fileitem:list) {
				if(fileitem.isFormField()) {
					//如果当前项是普通项,name当做key,对应的值当做value放入map中
					map.put(fileitem.getFieldName(), fileitem.getString("utf-8"));
				}else {
					//如果当前的对象是需要上传的对象
					//获取到原始的文件名称
					String oldFile = fileitem.getName();
					//获取到要保存文件（新文件）的信息  111.doc->12343.doc
					String newFile = UploadUtils.getUUIDName(oldFile);
					
					//通过fielitem获取到输入流，通过输入流能获取到二进制对象
					InputStream is = fileitem.getInputStream();
					//获取当前目录/product/3/的真实路径
					String realPath = getServletContext().getRealPath("/products/3/");				}
					String dir = UploadUtils.getDir(newFile);// f/e/8/3/2/3/4/1
					String path = realPath+dir;
					//内存中声明一个目录
					File newDir = new File(path);
					if(!newDir.exists()) {
						newDir.mkdirs();
					}
					//在服务端创建一个空目录（后缀必须和上传到服务端的文件名一致）
					File finalFile = new File(newDir,newFile);
					if(!finalFile.exists()) {
						finalFile.createNewFile();
					}
					//建立与空文件对应的输出流
					OutputStream os = new FileOutputStream(finalFile);
					//将输入流中的数据刷到输出流中
					IOUtils.copy(is, os);
					//释放资源
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(os);
					//向map存放一个键值对
					map.put("pimage","/products/3/"+dir+"/"+newFile);
					
					//利用BeanUtils将map里面的数据填充到Product对象上
					Product product = new Product();
					BeanUtils.populate(product,map);
					product.setPid(UUIDUtils.getId());
					product.setPdate(new Date());
					product.setPflag(0);
					//调用service方法将product上携带的数据存入数据库中，重定向到查询全部商品信息路径。、
					ProductService productService = new ProductServicelmpl();
					productService.saveProduct(product);
					
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
