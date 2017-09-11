package com.sbnz.project.controller;

import com.sbnz.project.controller.wrappers.AddToCartWrapper;
import com.sbnz.project.controller.wrappers.BonusDiscountRuleWrapper;
import com.sbnz.project.controller.wrappers.ProductSearchWrapper;
import com.sbnz.project.model.BaseUser;
import com.sbnz.project.model.Buyer;
import com.sbnz.project.model.EntryDiscount;
import com.sbnz.project.model.Product;
import com.sbnz.project.model.ProductCategory;
import com.sbnz.project.model.ProductCategoryRelation;
import com.sbnz.project.model.Receipt;
import com.sbnz.project.model.ReceiptEntry;
import com.sbnz.project.model.SalesEvent;
import com.sbnz.project.model.UserConstants;
import com.sbnz.project.services.BuyerServices;
import com.sbnz.project.services.ProductCategoryServices;
import com.sbnz.project.services.ProductServices;
import com.sbnz.project.services.ReceiptServices;
import com.sbnz.project.services.SalesEventServices;
import com.sbnz.project.util.KShortcuts;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/shopping")
@RestController
public class ShoppingController {

	@Autowired
	private BuyerServices buyerServices;
		
	@Autowired
	private ProductServices productServices;
	
	@Autowired
	private ProductCategoryServices productCategoryServices;
	
	@Autowired
	private ReceiptServices receiptServices;
	
	@Autowired
	private SalesEventServices salesEventServices;
	
	@RequestMapping(
			path="/load-resources",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.GET
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
			
			Receipt currentReceipt = (Receipt) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER_RECEIPT);
			if(currentReceipt == null){
				currentReceipt = new Receipt();
				request.getSession().setAttribute(ConstantsController.MAP_KEY_USER_RECEIPT, currentReceipt);
			}
			
		}else if(user.getRole() == UserConstants.USER_ROLE_MANAGER){
		}else{
		}
		
		ArrayList<Product> allProducts = productServices.getAllAvailableProducts();		
		map.put(ConstantsController.MAP_KEY_ALL_AVAILABLE_PRODUCTS, allProducts);		
		
		ArrayList<ProductCategory> allCategories = productCategoryServices.getAllCategories();	
		map.put(ConstantsController.MAP_KEY_ALL_PRODUCT_CATEGORIES, allCategories);
		
		return new ResponseEntity<HashMap<String,Object>>(map,HttpStatus.OK);
	}
	
	@RequestMapping(
			path="/add-item-to-cart",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<Object> addItemToCart(@RequestBody AddToCartWrapper wrapper,@Context HttpServletRequest request){
		
		Receipt currentReceipt = (Receipt) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER_RECEIPT);
		BaseUser buyer = (BaseUser) request.getSession().getAttribute(ConstantsController.MAP_KEY_USER);
		
		currentReceipt.setId(-1);
		if(currentReceipt.getReceiptEntries() == null){
			currentReceipt.setReceiptEntries(new HashSet<ReceiptEntry>());
		}
		
		Product product = productServices.getOneProduct(wrapper.getProductId());
		
		ReceiptEntry receiptEntry = new ReceiptEntry();
		receiptEntry.setEntryDiscounts(new HashSet<EntryDiscount>());
		receiptEntry.setReceipt(currentReceipt);
		
		try{			
			receiptEntry.setEntryNumber(currentReceipt.getReceiptEntries().size() + 1);	
			receiptEntry.setReceiptEntryId(currentReceipt.getReceiptEntries().size() + 1);
			
		}catch(NullPointerException e){
			
			currentReceipt.setReceiptEntries(new HashSet<>());
			receiptEntry.setEntryNumber(currentReceipt.getReceiptEntries().size()+1);
			receiptEntry.setReceiptEntryId(currentReceipt.getReceiptEntries().size() + 1);
			
		}
		
		receiptEntry.setOriginalPrice(product.getPrice());
		receiptEntry.setPriceOfOneOnDay(product.getPrice());
		receiptEntry.setTotalPrice(product.getPrice() * wrapper.getProductAmount());
		
		receiptEntry.setProduct(product);
		receiptEntry.setNumberOfProducts(wrapper.getProductAmount());
		
		EntryDiscount initialDiscount = new EntryDiscount();
		initialDiscount.setInitialDiscount(true);
		initialDiscount.setEntryDiscountId(0);
		initialDiscount.setReceiptEntry(receiptEntry);	
		initialDiscount.setDiscrountPercentage((float) 0);
		
		KieSession session = KShortcuts.getKieSession();
		
		session.getAgenda().getAgendaGroup("OsnovniPopustStavka").setFocus();
		session.insert(initialDiscount);
		session.insert(product);
		session.insert(receiptEntry);
		session.fireAllRules();
		
		session.destroy();

		if(initialDiscount.getDiscrountPercentage() != (float)0){
			receiptEntry.getEntryDiscounts().add(initialDiscount);
		}
		
		ArrayList<Receipt> allReceipts = receiptServices.getReceiptsForUser(buyer.getId());
		
		session = KShortcuts.getKieSession();
		
		BonusDiscountRuleWrapper bonusRuleWrapper = new BonusDiscountRuleWrapper();
		
		Long days15 = new Date().getTime(), days30 = days15.longValue();
		days15 = days15 - (long) (24*60*60*1000) * 14;
		days30 = days30 - (long) (24*60*60*1000) * 30;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2000);
		Long currentTime = c.getTime().getTime();
		
		ProductCategory category = product.getProductCategory();
		for(ProductCategoryRelation relation : category.getRelation()){
			SalesEvent event = relation.getSalesEvent();
			bonusRuleWrapper.salesEvents.add(event);
			System.out.println("ADDED : " + event);
		}
		
		session.getAgenda().getAgendaGroup("DodatniPopustStavka").setFocus();
		session.setGlobal("targetProduct", product);
		session.setGlobal("currentTime", currentTime);
		session.setGlobal("days15", days15);
		session.setGlobal("days30", days30);
		session.insert(bonusRuleWrapper);
		session.insert(allReceipts);
		session.fireAllRules();
		
		for(Float f : bonusRuleWrapper.addedEntries){
			EntryDiscount bonusDiscount = new EntryDiscount();
			bonusDiscount.setInitialDiscount(false);
			bonusDiscount.setDiscrountPercentage(f);
			bonusDiscount.setEntryDiscountId(receiptEntry.getEntryDiscounts().size());
			bonusDiscount.setReceiptEntry(receiptEntry);
			receiptEntry.getEntryDiscounts().add(bonusDiscount);
		}
		
		for(Integer i : bonusRuleWrapper.specialKeys){
			SalesEvent se = salesEventServices.getOneSalesEvent(i);
			EntryDiscount bonusDiscount = new EntryDiscount();
			bonusDiscount.setInitialDiscount(false);
			bonusDiscount.setDiscrountPercentage(se.getEventDiscount());
			bonusDiscount.setEntryDiscountId(receiptEntry.getEntryDiscounts().size());
			bonusDiscount.setReceiptEntry(receiptEntry);
			receiptEntry.getEntryDiscounts().add(bonusDiscount);
		}

		Float fullProductDiscount = (float)0;
		for(EntryDiscount ed : receiptEntry.getEntryDiscounts()){
			fullProductDiscount += ed.getDiscrountPercentage();
		}
		if(fullProductDiscount!=(float)0){
			if(fullProductDiscount >= receiptEntry.getProduct().getProductCategory().getMaxDiscount()){
				fullProductDiscount = receiptEntry.getProduct().getProductCategory().getMaxDiscount()/100;
			}else{
				fullProductDiscount /= 100;
			}
			
			fullProductDiscount = receiptEntry.getProduct().getPrice()*fullProductDiscount;
			receiptEntry.setPriceAfterDiscount(receiptEntry.getProduct().getPrice() - fullProductDiscount);
			receiptEntry.setTotalPrice(receiptEntry.getPriceAfterDiscount()*receiptEntry.getNumberOfProducts());				
		}else{
			receiptEntry.setTotalPrice(receiptEntry.getProduct().getPrice()*receiptEntry.getNumberOfProducts());
			receiptEntry.setPriceAfterDiscount(null);
		}
		currentReceipt.getReceiptEntries().add(receiptEntry);
		
		if(currentReceipt.getInitalPrice() == null){
			currentReceipt.setInitalPrice((float)0);
		}
		
		Float currentPrice = (float) 0;
		for(ReceiptEntry entry : currentReceipt.getReceiptEntries()){
			if(entry.getNumberOfProducts() < 1)
				continue;
			Float price = entry.getProduct().getPrice();
			price = price * entry.getNumberOfProducts();
			currentPrice += price;
		}
		currentReceipt.setInitalPrice(currentPrice);
		
		System.out.println("------------------");
		System.out.println(currentReceipt);
		System.out.println(currentReceipt.getReceiptEntries());
		return null;
		
	}
	
	@RequestMapping(
			path="/search",
			consumes=javax.ws.rs.core.MediaType.APPLICATION_JSON,
			produces=javax.ws.rs.core.MediaType.APPLICATION_JSON,
			method=RequestMethod.POST
			)
	
	public ResponseEntity<ArrayList<Product>> filterProducts(@RequestBody ProductSearchWrapper wrapper, @Context HttpServletRequest request){
		
		ArrayList<Product> products = productServices.getAllAvailableProducts();
		ArrayList<Product> filteredProducts = new ArrayList<Product>();
		KieSession session = KShortcuts.getKieSession();
		
		System.out.println(products);
		
		for(Product prod : products){
			Product p = new Product();
			p.setId(prod.getId());
			p.setName(prod.getName());
			p.setProductCategory(prod.getProductCategory());
			p.setPrice(prod.getPrice());
			
			session.insert(p);
			session.insert(wrapper);

			session.getAgenda().getAgendaGroup("Product Search").setFocus();
			session.fireAllRules();

			if(p.getId()!=-1 && p.getProductCategory() != null){
				filteredProducts.add(prod);
			}
		}
		
		session.destroy();
		
		return new ResponseEntity<ArrayList<Product>>(filteredProducts, HttpStatus.OK);
	}
	
}
