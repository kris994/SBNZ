package com.sbnz.project.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.project.model.BuyerCategory;
import com.sbnz.project.repository.BuyerCategoryRepository;

@Service
public class BuyerCategoryServicesImpl implements BuyerCategoryServices{

	@Autowired
	private BuyerCategoryRepository repository;
	
	@Override
	public ArrayList<BuyerCategory> getAllCategories() {
		return (ArrayList<BuyerCategory>) repository.findAll();
	}

	@Override
	public void editBuyerCategory(Integer id, Float pointsPercentage, Float max, Float min) {
		repository.editBuyerCategory(id, pointsPercentage, max, min);
	}

	@Override
	public BuyerCategory getBuyer(Integer id) {
		return repository.getBuyer(id);
	}

}
