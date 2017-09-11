package com.sbnz.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.project.model.ReceiptEntry;
import com.sbnz.project.repository.ReceiptEntryRepository;

@Service
public class ReceiptEntryServicesImpl implements ReceiptEntryServices{

	@Autowired
	private ReceiptEntryRepository repository;
	
	@Override
	public ReceiptEntry save(ReceiptEntry entry) {
		return repository.save(entry);
	}

}
