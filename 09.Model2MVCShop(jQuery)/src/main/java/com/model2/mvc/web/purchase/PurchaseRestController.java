package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

import oracle.net.aso.s;


//==> 회원관리 RestController
@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {
	
	///Field

	
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	//setter Method 구현 않음
		
	public PurchaseRestController(){
		System.out.println(this.getClass());
	}
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	
	
	@RequestMapping( value="json/addPurchase", method=RequestMethod.POST )
	public Purchase addPurchase(	@RequestBody Purchase purchase
									 ) throws Exception{
	
		System.out.println("/purchase/json/addPurchase : POST");
		//Business Logic
		System.out.println("::"+purchase);
		
		
		purchaseService.addPurchase(purchase);
		
		
		return purchase;
	}
	
	
	@RequestMapping( value="json/updatePurchase/{tranNo}", method=RequestMethod.GET )
	public Purchase updatePurchaseget(	 @PathVariable int tranNo ,
									HttpSession session ) throws Exception{
	
		System.out.println("/purchase/json/updatePurchase : GET");
		//Business Logic
		System.out.println("::"+tranNo);
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		
		return purchase;
	}
	
	
	@RequestMapping( value="json/updatePurchase", method=RequestMethod.POST )
	public Purchase updatePurchasepost(	 @RequestBody Purchase purchase,
									HttpSession session ) throws Exception{
	
		System.out.println("/purchase/json/updatePurchase : POST");
		//Business Logic
		System.out.println("::"+purchase);
		
		purchaseService.updatePurchase(purchase);
		Purchase rtPurchase = purchaseService.getPurchase(purchase.getTranNo());
		
		
		return rtPurchase;
	}
	
	@RequestMapping( value="json/getPurchase/{prodNo}", method=RequestMethod.GET )
	public Purchase getPurchase(	 @PathVariable int prodNo ,
									HttpSession session ) throws Exception{
	
		System.out.println("/purchase/json/updatePurchase : GET");
		//Business Logic
		System.out.println("::"+prodNo);
		Purchase purchase = purchaseService.getPurchase(prodNo);
		
		
		return purchase;
	}
	
	@RequestMapping( value="json/listPurchase" )
	public Map listPurchase(	@RequestBody String str
									) throws Exception{
		System.out.println("서버진입");
		System.out.println(str);
		JSONObject jsonObject = (JSONObject)JSONValue.parse(str);

		

		ObjectMapper objectMapper = new ObjectMapper();

		Search search = objectMapper.readValue(jsonObject.get("search").toString(),Search.class);
		System.out.println(search);
		Purchase purchase = objectMapper.readValue(jsonObject.get("purchase").toString(),Purchase.class);
		System.out.println(purchase);

		//Business Logic
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		 //Business logic 수행
		Map<String , Object> map=purchaseService.getPurchaseList(search,purchase.getBuyer().getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		
		return map;
	}
}