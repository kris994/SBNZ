package com.sbnz.project.services;

import java.util.ArrayList;

import com.sbnz.project.model.BuyerCategory;

public interface BuyerCategoryServices {
	
	public ArrayList<BuyerCategory> getAllCategories();
	public void editBuyerCategory(Integer id, Float pointsPercentage, Float max, Float min);
	public BuyerCategory getBuyer(Integer id);
}
