package com.sbnz.project.services;

import java.util.ArrayList;

import com.sbnz.project.model.Product;

public interface ProductServices {

	public ArrayList<Product> getAllProducts();	
	public ArrayList<Product> getAllAvailableProducts();	
	public Product getOneProduct(Integer id);	
	public void updateProductAmount(Integer productId, Integer amountSold);	
	public void setRestockOfProduct(Boolean restock, Integer productId);	
	public ArrayList<Product> getNonArchivedProducts();
}
