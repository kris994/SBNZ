package com.sbnz.project.controller.wrappers;

import com.sbnz.project.model.Product;

public class ProductOrderingWrapper {
	
	public Product product;
	public int orderedNumber;
	
	@Override
	public String toString() {
		return "ProductOrderingWrapper [product=" + product + ", orderedNumber=" + orderedNumber + "]";
	}
	
}
