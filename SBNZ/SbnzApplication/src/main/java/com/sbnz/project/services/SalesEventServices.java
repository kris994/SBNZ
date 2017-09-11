package com.sbnz.project.services;

import java.util.ArrayList;
import com.sbnz.project.model.SalesEvent;

public interface SalesEventServices {
	
	public ArrayList<SalesEvent> getAllSalesEvents();
	public void killAllRelationsWithSalesEvent(Integer seId);	
	public void insertProductCategoryRelation(Integer e_id, Integer p_id);	
	public Integer insertNewSalesEvent(Long endDate, Float eventDiscount, String eventName, Long startDate);	
	public void editSalesEvent(Long endDate, Float eventDiscount, String eventName, Long startDate,Integer id);
	public SalesEvent getOneSalesEvent(Integer id);
}
