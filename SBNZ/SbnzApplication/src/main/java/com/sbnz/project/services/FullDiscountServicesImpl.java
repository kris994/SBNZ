package com.sbnz.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.project.model.FullDiscount;
import com.sbnz.project.repository.FullDiscountRepository;

@Service
public class FullDiscountServicesImpl implements FullDiscountServices{

	@Autowired
	private FullDiscountRepository r;
	
	@Override
	public FullDiscount save(FullDiscount fullDiscount) {
		return r.save(fullDiscount);
	}

}
