package com.ngntu10.service.Category;

import com.ngntu10.dto.request.category.CategoryDTO;
import com.ngntu10.dto.response.APIResponse;
import com.ngntu10.dto.response.Category.CategoryResponse;
import com.ngntu10.entity.Category;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CategoryResponse createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists");
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(category -> modelMapper.map(category, CategoryResponse.class));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));

        // Check if new name already exists for other category
        if (!category.getName().equals(categoryDTO.getName()) && 
            categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists");
        }

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Transactional
    public APIResponse<?> deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));

        // Check if category has any products
        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with existing products");
        }

        categoryRepository.delete(category);
        return new APIResponse<>(false, 200, null, "Category deleted successfully");
    }
} 