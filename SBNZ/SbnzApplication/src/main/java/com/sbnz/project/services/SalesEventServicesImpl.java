package com.sbnz.project.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.project.model.SalesEvent;
import com.sbnz.project.repository.SalesEventRepository;

@Service
public class SalesEventServicesImpl implements SalesEventServices{

	@Autowired
	private SalesEventRepository repository;
	
	@Override
	public ArrayList<SalesEvent> getAllSalesEvents() {
		return (ArrayList<SalesEvent>) this.repository.findAll();
	}

	@Override
	public void killAllRelationsWithSalesEvent(Integer seId) {
		repository.killAllRelationsWithSalesEvent(seId);
	}

	@Override
	public void insertProductCategoryRelation(Integer e_id, Integer p_id) {
		repository.insertProductCategoryRelation(e_id, p_id);
	}

	@Override
	public Integer insertNewSalesEvent(Long endDate, Float eventDiscount, String eventName, Long startDate) {
		return repository.insertNewSalesEvent(endDate, eventDiscount, eventName, startDate);
	}

	@Override
	public void editSalesEvent(Long endDate, Float eventDiscount, String eventName, Long startDate, Integer id) {
		repository.editSalesEvent(endDate, eventDiscount, eventName, startDate, id);
	}

	@Override
	public SalesEvent getOneSalesEvent(Integer id) {
		return repository.findOne(id);
	}
	
}
