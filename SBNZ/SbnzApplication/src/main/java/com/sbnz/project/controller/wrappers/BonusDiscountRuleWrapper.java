package com.sbnz.project.controller.wrappers;

import java.util.ArrayList;

import com.sbnz.project.model.SalesEvent;

public class BonusDiscountRuleWrapper {
	
	public ArrayList<Float> addedEntries = new  ArrayList<Float>();
	public ArrayList<Integer> specialKeys = new ArrayList<>();
	public ArrayList<SalesEvent> salesEvents = new ArrayList<>();
	
	@Override
	public String toString() {
		return "BonusDiscountRuleWrapper [addedEntries=" + addedEntries + ", specialKeys=" + specialKeys
				+ ", salesEvents=" + salesEvents + "]";
	}
	
	
	
	
}
