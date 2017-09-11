package com.sbnz.project.controller;

import com.sbnz.project.model.BaseUser;
import com.sbnz.project.model.Buyer;
import com.sbnz.project.model.UserConstants;
import com.sbnz.project.services.BuyerServices;
import com.sbnz.project.services.ReceiptServices;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/profile")
@RestController
public class ProfileController {

	@Autowired
	private BuyerServices buyerServices;
	
	@Autowired
	private ReceiptServices receiptServices;
	
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
			map.put(ConstantsController.MAP_KEY_RECEIPTS_FOR_USER, receiptServices.getReceiptsForUser(buyer.getId()));
			
		}else if(user.getRole() == UserConstants.USER_ROLE_MANAGER){
		}else{
		}
		
		return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
	}
}
