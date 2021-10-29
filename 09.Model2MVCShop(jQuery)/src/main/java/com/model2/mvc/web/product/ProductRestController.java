package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> 회원관리 RestController
@RestController
@RequestMapping("/product/*")
public class ProductRestController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductRestController(){
		System.out.println(this.getClass());
	}
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	
	
	@RequestMapping( value="json/addProduct", method=RequestMethod.POST )
	public Product addProduct(	@RequestBody Product product,
								@RequestParam("fileName1") MultipartFile file,
									HttpSession session ) throws Exception{
	
		System.out.println("/product/json/addProduct : POST");
		//Business Logic
		System.out.println("::"+product);
		product.setFileName(file.toString());
		productService.addProduct(product);
		
		
		return product;
	}
	
	
	@RequestMapping( value="json/updateProduct/{prodNo}", method=RequestMethod.GET )
	public Product updateProductget(	 @PathVariable int prodNo ,
									HttpSession session ) throws Exception{
	
		System.out.println("/product/json/updateProduct : GET");
		//Business Logic
		System.out.println("::"+prodNo);
		Product product = productService.getProduct(prodNo);
		
		
		return product;
	}
	
	
	@RequestMapping( value="json/updateProduct", method=RequestMethod.POST )
	public Product updateProductpost(	 @RequestBody Product product,
									HttpSession session ) throws Exception{
	
		System.out.println("/product/json/updateProduct : POST");
		//Business Logic
		System.out.println("::"+product);
		
		productService.updateProduct(product);
		Product rtProduct = productService.getProduct(product.getProdNo());
		
		
		return rtProduct;
	}
	
	@RequestMapping( value="json/getProduct/{prodNo}", method=RequestMethod.GET )
	public Product getProduct(	 @PathVariable int prodNo ,
									HttpSession session ) throws Exception{
	
		System.out.println("/product/json/updateProduct : GET");
		//Business Logic
		System.out.println("::"+prodNo);
		Product product = productService.getProduct(prodNo);
		
		
		return product;
	}
	
	@RequestMapping( value="json/listProduct" )
	public Map listProduct(	@RequestBody Search search,
									HttpSession session ) throws Exception{

	
		System.out.println("/product/json/listProduct ");
		//Business Logic
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		
		return map;
	}
}