package com.ngntu10.controller;

import com.ngntu10.constants.Endpoint;
import com.ngntu10.dto.request.product.DeleteMultiProductDTO;
import com.ngntu10.dto.request.product.ProductDTO;
import com.ngntu10.dto.response.APIResponse;
import com.ngntu10.dto.response.PaginationResponse;
import com.ngntu10.dto.response.Product.ProductResponse;
import com.ngntu10.entity.Product;
import com.ngntu10.service.Product.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Endpoint.Product.BASE)
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product API")
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<APIResponse<PaginationResponse<ProductResponse>>> searchProducts(
            @RequestParam Map<String, String> params,
            Pageable pageable
    ) {
        var products = productService.searchProducts(params, pageable);
        return ResponseEntity.ok(new APIResponse<>(
                false,
                200,
                products,
                "Get product list successfully"
        ));
    }

    @GetMapping(Endpoint.Product.ID)
    public ResponseEntity<APIResponse<ProductResponse>> getProductById(@PathVariable String productId) {
        var product = productService.getProductById(productId);
        return ResponseEntity.ok(new APIResponse<>(
                false,
                200,
                product,
                "Get product successfully"
        ));
    }

    @PostMapping

    public ResponseEntity<APIResponse<ProductResponse>> createProduct(@RequestBody ProductDTO productDTO) {
        var product = productService.createProduct(productDTO);
        return ResponseEntity.ok(new APIResponse<>(
                false,
                200,
                product,
                "Create product successfully"
        ));
    }

    @PutMapping(Endpoint.Product.ID)

    public ResponseEntity<APIResponse<ProductResponse>> updateProduct(
            @PathVariable String productId,
            @RequestBody ProductDTO productDTO
    ) {
        var product = productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok(new APIResponse<>(
                false,
                200,
                product,
                "Update product successfully"
        ));
    }

    @DeleteMapping(Endpoint.Product.ID)

    public ResponseEntity<APIResponse<?>> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new APIResponse<>(false, 200, null, "Delete product successfully"));
    }

    @DeleteMapping(Endpoint.Product.DELETE_MANY)

    public ResponseEntity<APIResponse<?>> deleteProducts(@RequestBody DeleteMultiProductDTO deleteMultiProductDTO) {
        productService.deleteProducts(deleteMultiProductDTO);
        return ResponseEntity.ok(new APIResponse<>(false, 200, null, "Delete products successfully"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<APIResponse<PaginationResponse<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId,
            Pageable pageable
    ) {
        var products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(new APIResponse<>(
                false,
                200,
                products,
                "Get products by category successfully"
        ));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<PaginationResponse<ProductResponse>>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable
    ) {
        var products = productService.searchProducts(name, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(new APIResponse<>(
                false,
                200,
                products,
                "Search products successfully"
        ));
    }
    @GetMapping("/must-try")
    public ResponseEntity<List<ProductResponse>> getMustTryProducts() {
        return ResponseEntity.ok(productService.getMustTryProducts());
    }


}
