package com.sbnz.project.controller;

import com.sbnz.project.controller.wrappers.ProductCategoryWrapper;
import com.sbnz.project.controller.wrappers.SalesEventWrapper;
import com.sbnz.project.controller.wrappers.UserWrapper;
import com.sbnz.project.model.BaseUser;
import com.sbnz.project.model.Buyer;
import com.sbnz.project.model.BuyerCategory;
import com.sbnz.project.model.ProductCategory;
import com.sbnz.project.model.SalesEvent;
import com.sbnz.project.model.UserConstants;
import com.sbnz.project.services.BuyerCategoryServices;
import com.sbnz.project.services.BuyerServices;
import com.sbnz.project.services.ProductCategoryServices;
import com.sbnz.project.services.SalesEventServices;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin-control-panel")
@RestController
public class AdminController {
	
	@Autowired
	private BuyerServices buyerServices;

	@Autowired
	private BuyerCategoryServices buyerCategoryService;
	
	@Autowired
	private ProductCategoryServices productCategoryService;
	
	@Autowired
	private SalesEventServices salesEventService;
	
	@RequestMapping(
			path="/load-resources",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON
			)
	
	public ResponseEntity<HashMap<String,Object>> loadResources(@Context HttpServletRequest request){
		HashMap<String,Object> map = new HashMap<>();
		
		Object o = request.getSession().getAttribute(ConstantsController.SESSION_USER_KEYWORD);
		if(o == null){
			map.put(ConstantsController.MAP_KEY_STATUS, false);
			return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
		}
		
		BaseUser user = (BaseUser) o;
		
		map.put(ConstantsController.MAP_KEY_STATUS, true);
		map.put(ConstantsController.MAP_KEY_USER, user);
		
		if(user.getRole() == UserConstants.USER_ROLE_BUYER){
			Buyer buyer = buyerServices.getBuyerById(user.getId());
			map.put(ConstantsController.MAP_KEY_USER_ROLE_BUYER , buyer);
			
			return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
		}else if(user.getRole() == UserConstants.USER_ROLE_MANAGER){
		}else{
		}
		
		ArrayList<BuyerCategory> buyerCategories = buyerCategoryService.getAllCategories();
		ArrayList<ProductCategory> productCategories = productCategoryService.getAllCategories();
		ArrayList<SalesEvent> salesEvents = salesEventService.getAllSalesEvents();
		
		map.put(ConstantsController.MAP_KEY_ALL_BUYER_CATS, buyerCategories);
		map.put(ConstantsController.MAP_KEY_ALL_PRODUCT_CATEGORIES, productCategories);
		map.put(ConstantsController.MAP_KEY_ALL_SALES_EVENTS, salesEvents);
		
		return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/edit-user-category",
			consumes=MediaType.APPLICATION_JSON,
			produces=MediaType.APPLICATION_JSON,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<Object> editUserCategory(@RequestBody UserWrapper wrapper){
		
		System.out.println(wrapper);	
		this.buyerCategoryService.editBuyerCategory(wrapper.getId(), wrapper.getPerc(), wrapper.getMax(), wrapper.getMin());	
		ArrayList<BuyerCategory> buyerCategories = buyerCategoryService.getAllCategories();
		
		return new ResponseEntity<Object>(buyerCategories,HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/new-product-category",
			consumes=MediaType.APPLICATION_JSON,
			produces=MediaType.APPLICATION_JSON,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<Object> newProductCategory(@RequestBody ProductCategoryWrapper wrapper){
		
		productCategoryService.createNewProductCategory(wrapper.getName(), wrapper.getPerc(), wrapper.getParent());
		ArrayList<ProductCategory> productCategories = productCategoryService.getAllCategories();
		return new ResponseEntity<Object>(productCategories,HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/edit-product-category",
			consumes=MediaType.APPLICATION_JSON,
			produces=MediaType.APPLICATION_JSON,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<Object> editProductCategory(@RequestBody ProductCategoryWrapper wrapper){
		if(wrapper.getId() == null){
			return null;
		}
		
		productCategoryService.updateProductCategory(wrapper.getName(), wrapper.getPerc(), wrapper.getParent(), wrapper.getId());	
		ArrayList<ProductCategory> productCategories = productCategoryService.getAllCategories();
		return new ResponseEntity<>(productCategories, HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/new-sales-event",
			consumes=MediaType.APPLICATION_JSON,
			produces=MediaType.APPLICATION_JSON,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<Object> newSalesEvent(@RequestBody SalesEventWrapper wrapper){
	
		System.out.println(wrapper);	
		this.salesEventService.insertNewSalesEvent(wrapper.getEndDate(), wrapper.getDiscount(), wrapper.getName(), wrapper.getStartDate());
		ArrayList<SalesEvent> salesEvents = salesEventService.getAllSalesEvents();	
		return new ResponseEntity<Object>(salesEvents, HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/edit-sales-event",
			consumes=MediaType.APPLICATION_JSON,
			produces=MediaType.APPLICATION_JSON,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<Object> editSalesEvent(@RequestBody SalesEventWrapper wrapper){
		if(wrapper.getId() == null){
			return null;			
		}
		
		salesEventService.killAllRelationsWithSalesEvent(wrapper.getId());	
		salesEventService.editSalesEvent(wrapper.getEndDate(), wrapper.getDiscount(), wrapper.getName(), wrapper.getStartDate(), wrapper.getId());		
		
		for(Integer id : wrapper.getValidForCategories()){
			salesEventService.insertProductCategoryRelation(wrapper.getId(), id);
		}
		
		ArrayList<SalesEvent> salesEvents = salesEventService.getAllSalesEvents();		
		return new ResponseEntity<Object>(salesEvents,HttpStatus.OK);
	}
}
