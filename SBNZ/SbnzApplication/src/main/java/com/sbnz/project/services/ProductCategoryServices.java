package com.sbnz.project.services;

import java.util.ArrayList;

import com.sbnz.project.model.ProductCategory;

public interface ProductCategoryServices {

	public ArrayList<ProductCategory> getAllCategories();
	public void createNewProductCategory(String categoryName, Float maxDiscount, Integer parentId);
	public void updateProductCategory(String name, Float maxDiscount, Integer parentId, Integer id);
}
