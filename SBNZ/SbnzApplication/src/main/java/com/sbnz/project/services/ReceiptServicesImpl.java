package com.sbnz.project.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sbnz.project.model.Receipt;
import com.sbnz.project.repository.ReceiptRepository;

@Service
public class ReceiptServicesImpl implements ReceiptServices{

	@Autowired
	private ReceiptRepository receiptRepository;

	@Override
	public ArrayList<Receipt> getReceiptsForUser(Integer user_id) {
		return receiptRepository.getReceiptsForUser(user_id);
	}

	@Override
	public Receipt save(Receipt r) {
		return receiptRepository.save(r);
	}

	@Override
	public ArrayList<Receipt> findAllReceipts() {
		return (ArrayList<Receipt>) receiptRepository.findAll();
	}

	@Override
	public ArrayList<Receipt> getAllUnprocessedReceipts() {
		return receiptRepository.getAllUnprocessedReceipts();
	}

	@Override
	public void updateReceiptState(Integer state, Integer id) {
		this.receiptRepository.updateReceiptState(state, id);
	}

	@Override
	public Receipt findOneReceipt(Integer id) {
		return this.receiptRepository.findOne(id);
	}
	
	
}
