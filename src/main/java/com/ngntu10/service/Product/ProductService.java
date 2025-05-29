package com.ngntu10.service.Product;

import com.ngntu10.dto.request.product.DeleteMultiProductDTO;
import com.ngntu10.dto.request.product.ProductDTO;
import com.ngntu10.dto.request.product.ProductImageDTO;
import com.ngntu10.dto.response.APIResponse;
import com.ngntu10.dto.response.PaginationResponse;
import com.ngntu10.dto.response.Product.ProductResponse;
import com.ngntu10.entity.Category;
import com.ngntu10.entity.Product;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.CategoryRepository;
import com.ngntu10.repository.ProductRepository;
import com.ngntu10.util.PageableUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;


    public ProductResponse createProduct(ProductDTO createProductDTO) {
        Product product = new Product();
        product.setName(createProductDTO.getName());
        product.setPrice(createProductDTO.getPrice());
        product.setDescription(createProductDTO.getDescription());
        product.setImageUrl(createProductDTO.getImageUrl());

        Category category = categoryRepository.findById(createProductDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + createProductDTO.getCategoryId()));

        product.setCategory(category);

        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    public ProductResponse updateProduct(String productId, ProductDTO updateProductDTO) {
        Product existingProduct = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
        modelMapper.map(updateProductDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductResponse.class);
    }

    public PaginationResponse<ProductResponse> searchProducts(Map<String, String> params, Pageable pageable) {
        Pageable realPageable = pageable != null ? pageable : PageableUtil.getPageable(params);
        Page<Product> productPage = productRepository.findAll(realPageable);
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
        return new PaginationResponse<>(productPage, productResponses);
    }

    public PaginationResponse<ProductResponse> searchProducts(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);
        if (name != null && !name.isEmpty()) {
            final String searchName = name.toLowerCase();
            spec = spec.and((root, query, cb) -> {
                return cb.like(cb.lower(root.get("name")), "%" + searchName + "%");
            });
        }
        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId));
        }
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
        return new PaginationResponse<>(productPage, productResponses);
    }


    public PaginationResponse<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
        return new PaginationResponse<>(productPage, productResponses);
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
        return modelMapper.map(product, ProductResponse.class);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
        productRepository.delete(product);
    }

    public void deleteProducts(DeleteMultiProductDTO deleteMultiProductDTO) {
        for (String id : deleteMultiProductDTO.getProductIds()) {
            deleteProduct(id);
        }
    }

    public List<ProductResponse> getMustTryProducts() {
        List<Product> topProducts = productRepository.findTop5ByOrderByCreatedAtDesc();
        return topProducts.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

}
