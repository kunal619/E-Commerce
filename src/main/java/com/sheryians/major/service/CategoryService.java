package com.sheryians.major.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sheryians.major.model.Category;
import com.sheryians.major.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepo;
	
	public void addCategory(Category category) {
		categoryRepo.save(category);
	}
	
	public List<Category> getAllCategories(){
		return categoryRepo.findAll();
	}
	
	public void deleteCategoryById(int id) {
		categoryRepo.deleteById(id);
	}
	
	public Optional<Category> getCategoryById(int id){
		return categoryRepo.findById(id);
	}
	
}
