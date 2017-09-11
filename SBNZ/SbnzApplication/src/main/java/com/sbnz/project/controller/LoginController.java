package com.sbnz.project.controller;

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

import com.sbnz.project.model.BaseUser;
import com.sbnz.project.model.Buyer;
import com.sbnz.project.services.BaseUserServices;
import com.sbnz.project.services.BuyerCategoryServices;
import com.sbnz.project.services.BuyerServices;
import com.sbnz.project.util.KShortcuts;

@RequestMapping("/login-register")
@RestController
public class LoginController {
	
	@Autowired
	private BaseUserServices baseUserServices;
	
	@Autowired
	private BuyerServices buyerServices;
	
	@Autowired
	private BuyerCategoryServices buyerCategoryServices;
	
	@RequestMapping(
			path="/load-resources",
			produces=MediaType.APPLICATION_JSON,
			method=RequestMethod.GET
			)
	
	public ResponseEntity<HashMap<String,Object>> loadResources(@Context HttpServletRequest request){
		HashMap<String,Object> map = new HashMap<>();
		
		if(request.getSession().getAttribute(ConstantsController.SESSION_USER_KEYWORD) != null){
			map.put(ConstantsController.MAP_KEY_STATUS, false);
			return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
		}
		
		KShortcuts.getKieSession().dispose();	
		map.put(ConstantsController.MAP_KEY_STATUS, true);
		return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/register",
			consumes=MediaType.APPLICATION_JSON,
			method=RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON
			)
	
	public ResponseEntity<HashMap<String,Object>> register(@RequestBody BaseUser user,@Context HttpServletRequest context){
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		ArrayList<BaseUser> users  = baseUserServices.getAllUsers();
		for(BaseUser u : users){
			if(u.getUsername().equals(user.getUsername())){
				map.put(ConstantsController.MAP_KEY_STATUS, false);
				map.put(ConstantsController.MAP_KEY_MESSAGE,"Selected username already exists");
				return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
			}
		}
		
		user = baseUserServices.save(user);	
		Buyer buyer = new Buyer();
		buyer.setAddress("Not defined");
		buyer.setBuyerCategory(buyerCategoryServices.getBuyer(1));
		buyer.setId(user.getId());
		buyer.setRewardPoints(0);
		buyer.setUser(user);
		
		buyer = buyerServices.save(buyer);		
		context.getSession().setAttribute(ConstantsController.SESSION_USER_KEYWORD, user);
		context.getSession().setAttribute(ConstantsController.MAP_KEY_USER_ROLE_BUYER, buyer);	
		map.put(ConstantsController.MAP_KEY_STATUS, true);
		
		return new ResponseEntity<HashMap<String,Object>>(map, HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/login",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON,
			produces=MediaType.APPLICATION_JSON
			)
	
	public ResponseEntity<HashMap<String,Object>> login(@RequestBody BaseUser user, @Context HttpServletRequest context){
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		for(BaseUser u : baseUserServices.getAllUsers()){
			if(u.getUsername().equals(user.getUsername())){
				if(u.getPassword().equals(user.getPassword())){
					Buyer b = buyerServices.getBuyerById(u.getId());
					context.getSession().setAttribute(ConstantsController.MAP_KEY_USER_ROLE_BUYER, b);
					context.getSession().setAttribute(ConstantsController.SESSION_USER_KEYWORD, u);
					map.put(ConstantsController.MAP_KEY_STATUS, true);
					return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
				}
				map.put(ConstantsController.MAP_KEY_STATUS, false);
				map.put(ConstantsController.MAP_KEY_MESSAGE, "Password is incorrect");
				return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
			}
		}
		map.put(ConstantsController.MAP_KEY_STATUS, false);
		map.put(ConstantsController.MAP_KEY_MESSAGE, "User does not exist");
		return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
		
	}
	
}
