//package com.ngntu10.controller;
//
//
//import com.ngntu10.constants.Endpoint;
//import com.ngntu10.dto.request.product.DeleteMultiProductDTO;
//import com.ngntu10.dto.request.product.ProductDTO;
//import com.ngntu10.dto.request.product.ProductImageDTO;
//import com.ngntu10.dto.response.APIResponse;
//import com.ngntu10.dto.response.PaginationResponse;
//import com.ngntu10.dto.response.Product.ProductResponse;
//import com.ngntu10.entity.Product;
//import com.ngntu10.service.Product.ProductService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping(Endpoint.Product.BASE)
//@RequiredArgsConstructor
//@Tag(name = "Product", description = "Product API")
//@Slf4j
//public class ProductController {
//    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
//    private final ProductService productService;
//    @GetMapping
//    public ResponseEntity<APIResponse<PaginationResponse<ProductResponse>>> searchProducts(
//            @RequestParam Map<String, String> params
//    ) {
//        var products = productService.searchProducts(params);
//        var apiResponse = new APIResponse<PaginationResponse<ProductResponse>>(
//                false, 
//                200,
//                products, 
//                "Get product list successfully");
//        return ResponseEntity.ok(apiResponse);
//    }
//    
//    @GetMapping(Endpoint.Product.SLUG)
//    public ResponseEntity<APIResponse<Product>> getProductBySlug(@PathVariable String slug) {
//        return ResponseEntity.ok(productService.getProductBySlug(slug));
//    }
//
//    @GetMapping(Endpoint.Product.ID)
//    public ResponseEntity<APIResponse<Product>> getProductById(@PathVariable String productId) {
//        return ResponseEntity.ok(productService.getProductById(productId));
//    }
//    
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<APIResponse<Product>> createProduct(@RequestBody ProductDTO createProductDTO) {
//        Product product = productService.createProduct(createProductDTO);
//        return ResponseEntity.ok(new APIResponse<Product>(false, 200, product, "Create new product successfully"));
//    }
//
//    @PostMapping(Endpoint.Product.CHANGE_IMAGES)
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<APIResponse<Product>> uploadProductImage(@RequestBody ProductImageDTO productImageDTO) {
//        Product product = productService.uploadProductImage(productImageDTO);
//        var apiResponse = new APIResponse<Product>(false, 
//                200,
//                product,
//                "Product images updated successfully");
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @DeleteMapping(Endpoint.Product.ID)
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<APIResponse<?>> deleteProductById(@PathVariable String productId) {
//        return ResponseEntity.ok(productService.deleteProductById(productId));
//    }
//
//    @DeleteMapping(Endpoint.Product.DELETE_MANY)
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<APIResponse<?>> deleteProducts(@RequestBody DeleteMultiProductDTO deleteMultiProductDTO) {
//        return ResponseEntity.ok(productService.deleteProducts(deleteMultiProductDTO));
//    }
//
//    @PutMapping(Endpoint.Product.ID)
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<APIResponse<Product>> updateProduct(@PathVariable String productId, @RequestBody ProductDTO updateProductDTO) {
//        return ResponseEntity.ok(productService.updateProductById(productId, updateProductDTO));
//    }
//}
