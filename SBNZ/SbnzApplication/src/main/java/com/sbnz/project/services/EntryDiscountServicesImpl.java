package com.sbnz.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.project.model.EntryDiscount;
import com.sbnz.project.repository.EntryDiscountRepository;

@Service
public class EntryDiscountServicesImpl implements EntryDiscountServices{

	@Autowired
	private EntryDiscountRepository repo;
	
	@Override
	public EntryDiscount save(EntryDiscount entryDiscount) {
		return repo.save(entryDiscount);
	}

}
