package com.sbnz.project.services;

import com.sbnz.project.model.Buyer;

public interface BuyerServices {

	public Buyer getBuyerById(Integer id);	
	public void updateBuyerPoints(Integer buyerId, Integer pointsOffset);	
	public Buyer save(Buyer b);
}
