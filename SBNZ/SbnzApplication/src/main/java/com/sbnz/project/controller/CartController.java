package com.sbnz.project.controller;

import com.sbnz.project.controller.wrappers.PointsWrapper;
import com.sbnz.project.controller.wrappers.ReceiptDiscountWrapper;
import com.sbnz.project.model.BaseUser;
import com.sbnz.project.model.Buyer;
import com.sbnz.project.model.EntryDiscount;
import com.sbnz.project.model.FullDiscount;
import com.sbnz.project.model.Receipt;
import com.sbnz.project.model.ReceiptConstants;
import com.sbnz.project.model.ReceiptEntry;
import com.sbnz.project.model.UserConstants;
import com.sbnz.project.services.BuyerServices;
import com.sbnz.project.services.EntryDiscountServices;
import com.sbnz.project.services.FullDiscountServices;
import com.sbnz.project.services.ReceiptEntryServices;
import com.sbnz.project.services.ReceiptServices;
import com.sbnz.project.util.KShortcuts;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/shopping-cart")
@RestController
public class CartController {

	@Autowired
	private BuyerServices buyerServices;
	
	@Autowired
	private ReceiptServices receiptServices;
	
	@Autowired
	private ReceiptEntryServices receiptEntryServices;
	
	@Autowired
	private EntryDiscountServices entryDiscountServices;
	
	@Autowired
	private FullDiscountServices fullDiscountServices;
	
	@RequestMapping(
			path="/load-resources",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.GET
			)
	
	public ResponseEntity<HashMap<String,Object>> loadResources(@Context HttpServletRequest request){
		HashMap<String,Object> map = new HashMap<>();
		
		Object o = request.getSession().getAttribute(ConstantsController.SESSION_USER_KEYWORD);
		System.out.println(o);
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
			
			Receipt currentReceipt = (Receipt) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER_RECEIPT);
			if(currentReceipt == null){
				currentReceipt = new Receipt();
				request.getSession().setAttribute(ConstantsController.MAP_KEY_USER_RECEIPT, currentReceipt);
			}
			
			map.put(ConstantsController.MAP_KEY_USER_RECEIPT, currentReceipt);
			
		}else if(user.getRole() == UserConstants.USER_ROLE_MANAGER){
		}else{
		}
		
		return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/remove-entry/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<ArrayList<ReceiptEntry>> removeEntry(@PathVariable("id") Integer entryId,@Context HttpServletRequest request){
		
		Receipt currentReceipt = (Receipt) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER_RECEIPT);
		
		for(ReceiptEntry oneEntry : currentReceipt.getReceiptEntries()){
			if(oneEntry.getReceiptEntryId() == entryId){
				Float killPrice = oneEntry.getProduct().getPrice() * oneEntry.getNumberOfProducts();
				currentReceipt.setInitalPrice(currentReceipt.getInitalPrice() - killPrice);
				oneEntry.setNumberOfProducts(0);
				break;
			}
		}
		
		ArrayList<ReceiptEntry> receiptEntries = new ArrayList<>();
		for(ReceiptEntry oneEntry : currentReceipt.getReceiptEntries()){
			receiptEntries.add(oneEntry);
		}
		
		return new ResponseEntity<ArrayList<ReceiptEntry>>(receiptEntries,HttpStatus.OK);
	}
	
	@RequestMapping(
			
			path="/complete-shopping/{points}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<Object> calculateReceipt(@PathVariable("points") Integer points,@Context HttpServletRequest request){
		
		Receipt currentReceipt = (Receipt) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER_RECEIPT);
		BaseUser user = (BaseUser) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER);
		Buyer buyer = (Buyer) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER_ROLE_BUYER);
		
		buyerServices.updateBuyerPoints(buyer.getId(), -1*points);
		
		currentReceipt = calculateReceiptDiscounts(currentReceipt,user,buyer);
		
		currentReceipt.setSpentPoints(points);
		currentReceipt.setFinalPrice(currentReceipt.getFinalPrice() - currentReceipt.getSpentPoints());
		
		PointsWrapper wrapper = new PointsWrapper();
		wrapper.buyerCategory = buyer.getBuyerCategory();
		wrapper.finalReceiptPrice = currentReceipt.getFinalPrice();
		
		KieSession session = KShortcuts.getKieSession();
		session.getAgenda().getAgendaGroup("PointCalculation").setFocus();
		session.insert(wrapper);
		session.fireAllRules();
		
		currentReceipt.setAquiredPoints(wrapper.achievedPoints);
		
		currentReceipt.setId(null);
		currentReceipt.setDateOfTransaction(new Date().getTime());
		currentReceipt.setBuyer(buyer);
		currentReceipt.setReceiptState(ReceiptConstants.RECEIPT_ORDERED);
		
		HashSet<ReceiptEntry> entries = (HashSet<ReceiptEntry>) currentReceipt.getReceiptEntries();
		HashSet<FullDiscount> fullReceiptDiscounts = (HashSet<FullDiscount>) currentReceipt.getFullDiscounts();
		
		Receipt persistedReceipt = receiptServices.save(currentReceipt);
		
		for(FullDiscount disc : fullReceiptDiscounts){
			disc.setId(null);
			disc.setReceipt(persistedReceipt);
			disc = fullDiscountServices.save(disc);		
			persistedReceipt.getFullDiscounts().add(disc);		
		}
		
		persistedReceipt.setReceiptEntries(new HashSet<ReceiptEntry>());

		for(ReceiptEntry entry : entries){
			if(entry.getNumberOfProducts() < 1)
				continue;
			
			entry.setReceiptEntryId(null);
			entry.setReceipt(persistedReceipt);

			HashSet<EntryDiscount> entryDiscounts = (HashSet<EntryDiscount>) entry.getEntryDiscounts();
			
			entry = receiptEntryServices.save(entry);
			
			for(EntryDiscount entryDiscount : entryDiscounts){
				entryDiscount.setEntryDiscountId(null);
				entryDiscount.setReceiptEntry(entry);
				
				entryDiscountServices.save(entryDiscount);
			}
			
			persistedReceipt.getReceiptEntries().add(entry);
		}
		
		request.getSession().setAttribute(ConstantsController.MAP_KEY_USER_RECEIPT, new Receipt());
		return new ResponseEntity<Object>(persistedReceipt,HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/calculate-receipt",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<Object> calculateReceipt(@Context HttpServletRequest request){
		
		Receipt currentReceipt = (Receipt) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER_RECEIPT);
		BaseUser user = (BaseUser) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER);
		Buyer buyer = (Buyer) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER_ROLE_BUYER);
		
		currentReceipt = calculateReceiptDiscounts(currentReceipt,user,buyer);		
		return new ResponseEntity<Object>(currentReceipt,HttpStatus.OK);
	}
	
	private Receipt calculateReceiptDiscounts(Receipt receipt, BaseUser user,Buyer buyer){
		
		ReceiptDiscountWrapper wrapper = new ReceiptDiscountWrapper();
		
		receipt.setFullDiscounts(new HashSet<FullDiscount>());
		
		double maxPrice = 0;
		for(ReceiptEntry entry : receipt.getReceiptEntries()){
			if(entry.getTotalPrice() > maxPrice){
				maxPrice = entry.getTotalPrice();
			}
		}
		
		wrapper.maxPrice = maxPrice;
		wrapper.productPrice = receipt.getInitalPrice();
		
		System.out.println("Max price : " + wrapper.maxPrice);
		System.out.println("Full price : " + wrapper.productPrice);
		
		wrapper.userLogDate = user.getRegistryDate();
		wrapper.currentDate = new Date().getTime();
		wrapper.years2 = (long)24*60*60*1000*365*2;
		wrapper.userCategoryName = buyer.getBuyerCategory().getCategoryName();
		
		KieSession session = KShortcuts.getKieSession();
		session.getAgenda().getAgendaGroup("FullReceiptDiscountRules").setFocus();
		session.insert(wrapper);
		session.fireAllRules();
		
		session.dispose();
		
		System.out.println(">>> : " + wrapper);
		
		if(wrapper.initialDiscount != null){
			float dp = wrapper.initialDiscount.getDiscountPercentage();
			receipt.setDiscountPercentage(dp);
			receipt.getFullDiscounts().add(wrapper.initialDiscount);
		}else{
			receipt.setDiscountPercentage((float)0);
		}
		
		for(FullDiscount fd : wrapper.bonusDiscounts){
			float dp = fd.getDiscountPercentage();
			receipt.setDiscountPercentage(receipt.getDiscountPercentage() + dp);
			receipt.getFullDiscounts().add(fd);
		}
		
		receipt.setInitalPrice((float)0);
		
		System.out.println("RECEIPT : " + receipt);
		
		for(ReceiptEntry entry : receipt.getReceiptEntries()){
			if(entry.getNumberOfProducts() < 1)
				continue;
			float priceOfEntry = 0;
			if(entry.getPriceAfterDiscount() != null){
				priceOfEntry = entry.getPriceAfterDiscount()*entry.getNumberOfProducts();				
			}else{
				priceOfEntry = entry.getTotalPrice();
			}
			receipt.setInitalPrice(receipt.getInitalPrice()+priceOfEntry);
		}
		
		float discount = receipt.getInitalPrice() * (receipt.getDiscountPercentage()/100);
		
		receipt.setFinalPrice(receipt.getInitalPrice() - discount);
		
		return receipt;
	}
}
