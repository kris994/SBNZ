package com.sbnz.project.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.project.model.ProductCategory;
import com.sbnz.project.repository.ProductCategoryRepository;

@Service
public class ProductCategoryServicesImpl implements ProductCategoryServices{

	@Autowired
	private ProductCategoryRepository repository;
	
	@Override
	public ArrayList<ProductCategory> getAllCategories() {
		return (ArrayList<ProductCategory>) repository.findAll();
	}

	@Override
	public void createNewProductCategory(String categoryName, Float maxDiscount, Integer parentId) {
		repository.createNewProductCategory(categoryName, maxDiscount, parentId);
	}

	@Override
	public void updateProductCategory(String name, Float maxDiscount, Integer parentId, Integer id) {
		repository.updateProductCategory(name, maxDiscount, parentId, id);
	}

}
