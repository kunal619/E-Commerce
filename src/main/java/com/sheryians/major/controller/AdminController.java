package com.sheryians.major.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sheryians.major.dto.ProductDTO;
import com.sheryians.major.model.Category;
import com.sheryians.major.model.Product;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductService;

@Controller
public class AdminController {
	@Autowired
	CategoryService categoryService;

//Admin Section----------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/admin")
	public String adminHome() {
		return "adminHome";
	}
	
	@GetMapping("/admin/categories")
	public String getCategories(Model model) {
		model.addAttribute("categories",categoryService.getAllCategories());
		return "categories";
	}
	@GetMapping("/admin/categories/add")
	public String getAddCategories(Model model) {
		model.addAttribute("category", new Category());
		return "categoriesAdd";
	}
	@PostMapping("/admin/categories/add")
	public String postAddCategories(@ModelAttribute("category") Category category) {
		categoryService.addCategory(category);
		return "redirect:/admin/categories";
	}
	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCategoryById(@PathVariable int id) {
		categoryService.deleteCategoryById(id);
		return "redirect:/admin/categories";
	}
	 @GetMapping("/admin/categories/update/{id}")
	 public String updateCategoryById(@PathVariable int id,Model model) {
		 Optional<Category> cat = categoryService.getCategoryById(id);
		 if(cat.isPresent()) {
			 model.addAttribute("category",cat.get());
			 return "categoriesAdd";
		}
		 else
			 return "404";
	 }
//--------------------------------------------------------EOD--------------------------------------------------------------------------

	 
//Product Start----------------------------------------------------------------------------------------------------------------------------------
	 @Autowired
	 ProductService productService;
	 
	// public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
	 public String uploadDir = "src/main/resources/static/productImages"; 
	 @GetMapping("/admin/products")
	 public String getAllProducts(Model model) {
		 model.addAttribute("products", productService.getAllProducts());
		 return "products";
	 }

	 @GetMapping("/admin/products/add")
	 public String addProductsGet(Model model) {
		 model.addAttribute("productDTO", new ProductDTO());
		 model.addAttribute("categories", categoryService.getAllCategories());
		 return "productsAdd";
	 }
	 
	 @PostMapping("/admin/products/add")
	 public String addProductsPost(@ModelAttribute("productDTO")ProductDTO productDTO, 
			 						@RequestParam("productImage")MultipartFile file, 
			 						@RequestParam("imgName")String imgName ) throws IOException {
		 
		 Product product = new Product();
		 product.setId(productDTO.getId());
		 product.setName(productDTO.getName());
		 product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
		 product.setDescription(productDTO.getDescription());
		 product.setPrice(productDTO.getPrice());
		 product.setWeight(productDTO.getWeight());
		 
		 String imageUUID;
		 if(!file.isEmpty()){
			 imageUUID = file.getOriginalFilename();
			 Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
			 Files.write(fileNameAndPath, file.getBytes());
		 }else {
			 imageUUID = imgName;
		 }
		 product.setImageName(imageUUID);
		 productService.addProducts(product);
		 return "redirect:/admin/products";
	 }
	 
		@GetMapping("/admin/product/delete/{id}")
		public String deleteProductById(@PathVariable long id) {
			productService.deleteProductById(id);
			return "redirect:/admin/products";
		}
		 @GetMapping("/admin/product/update/{id}")
		 public String updateProductById(@PathVariable long id,Model model) {
			 Product product = productService.getProductById(id).get();
			 ProductDTO productDTO = new ProductDTO();
			 productDTO.setId(product.getId());
			 productDTO.setName(product.getName());
			 productDTO.setCategoryId(product.getCategory().getId());
			 productDTO.setPrice(product.getPrice());
			 productDTO.setWeight(product.getWeight());
			 productDTO.setDescription(product.getDescription());
			 productDTO.setImageName(product.getImageName());
			 
			 model.addAttribute("categories",categoryService.getAllCategories());
			 model.addAttribute("productDTO",productDTO);
			 
			 return "productsAdd";
		 }	
	 
}




