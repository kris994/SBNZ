package com.sbnz.project.services;

import java.util.ArrayList;

import com.sbnz.project.model.Receipt;

public interface ReceiptServices {
	
	public ArrayList<Receipt> getReceiptsForUser(Integer user_id);	
	public Receipt save(Receipt r);	
	public ArrayList<Receipt> findAllReceipts();
	public ArrayList<Receipt> getAllUnprocessedReceipts();
	public void updateReceiptState(Integer state, Integer id);
	public Receipt findOneReceipt(Integer id);
}
