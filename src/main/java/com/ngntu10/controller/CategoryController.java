package com.ngntu10.controller;

import com.ngntu10.constants.Endpoint;
import com.ngntu10.dto.request.category.CategoryDTO;
import com.ngntu10.dto.response.APIResponse;
import com.ngntu10.dto.response.Category.CategoryResponse;
import com.ngntu10.service.Category.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoint.Category.BASE)
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category API")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<APIResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new APIResponse<>(false, 200, categories, "Get all categories successfully"));
    }

    @GetMapping(Endpoint.Category.PAGE)
    public ResponseEntity<APIResponse<Page<CategoryResponse>>> getAllCategories(Pageable pageable) {
        Page<CategoryResponse> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(new APIResponse<>(false, 200, categories, "Get categories page successfully"));
    }

    @GetMapping(Endpoint.Category.ID)
    public ResponseEntity<APIResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new APIResponse<>(false, 200, category, "Get category successfully"));
    }

    @PostMapping

    public ResponseEntity<APIResponse<CategoryResponse>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryResponse category = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(new APIResponse<>(false, 200, category, "Create category successfully"));
    }

    @PutMapping(Endpoint.Category.ID)

    public ResponseEntity<APIResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO) {
        CategoryResponse category = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(new APIResponse<>(false, 200, category, "Update category successfully"));
    }

    @DeleteMapping(Endpoint.Category.ID)

    public ResponseEntity<APIResponse<?>> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
} 