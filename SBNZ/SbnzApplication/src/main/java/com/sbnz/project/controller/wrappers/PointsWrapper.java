package com.sbnz.project.controller.wrappers;

import com.sbnz.project.model.BuyerCategory;

public class PointsWrapper {
	
	public BuyerCategory buyerCategory;
	public Float finalReceiptPrice;
	public Integer achievedPoints;
	
	@Override
	public String toString() {
		return "PointsWrapper [buyerCategory=" + buyerCategory + ", finalReceiptPrice=" + finalReceiptPrice
				+ ", achievedPoints=" + achievedPoints + "]";
	}
		
	
}
