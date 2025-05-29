package com.ngntu10.service.Product;

import com.ngntu10.dto.request.product.DeleteMultiProductDTO;
import com.ngntu10.dto.request.product.ProductDTO;
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
import org.springframework.data.domain.PageRequest;
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

    /**
     * Creates a new product and persists it in the database.
     * <p>
     * This method maps the input DTO {@link ProductDTO} to a {@link Product} entity,
     * processes associated images and product attributes, and saves the product along with
     * its related entities (images and attributes) into the database.
     * </p>
     *
     * @param createProductDTO the data transfer object containing the product information,
     *                         including the images and attributes associated with the product
     * @return the persisted {@link Product} entity after it has been saved in the database
     * @throws IllegalArgumentException if the provided DTO is invalid or any required fields are missing
     */
     public Product createProduct(ProductDTO createProductDTO) {
         Product product = modelMapper.map(createProductDTO, Product.class);
         return productRepository.save(product);
     }
     
    /**
     * Uploads and associates images with an existing product.
     *
     * @param productImageDTO DTO containing product ID and list of images to upload
     * @return the updated Product with new images
     * @throws NotFoundException if the product with given ID doesn't exist
     * @throws IllegalArgumentException if the productImageDTO or its contents are invalid
     */
    @Transactional 
    public Product uploadProductImage(ProductImageDTO productImageDTO) {
        if (productImageDTO == null || productImageDTO.getProductId() == null) {
            throw new IllegalArgumentException("Product image DTO and product ID cannot be null");
        }

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
    /**
     * Retrieves a product by its unique slug.
     * <p>
     * This method queries the product repository to find a product based on the provided slug.
     * If the product is found, it returns the product along with a success message.
     * If the product is not found, it returns a message indicating that the product was not found.
     * </p>
     *
     * @param id the unique identifier (id) of the product to be retrieved
     * @return an {@link APIResponse} object containing the product if found, or an error message if not found.
     *         The response will have an error flag (true if found, false if not), an HTTP status code,
     *         the product data (if found) with initialized collections, and a message describing the result.
     * @throws org.hibernate.LazyInitializationException related to getProductBySlug function, read for more details
     */
    @Transactional
    public APIResponse<Product> getProductById(String id) {
        Optional<Product> optionalProduct = productRepository.findById(UUID.fromString(id));
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return new APIResponse<Product>(false, 200, product, "Product found successfully");
        }
        return new APIResponse<Product>(true, 404, null, "Product not found");
    }

    /**
     * Deletes a product from the database by its ID.
     * <p>
     * This method attempts to delete a product using the provided ID.
     * </p>
     *
     * @param id The unique identifier of the product to delete (UUID as String)
     * @return {@link APIResponse}
     * @throws NotFoundException if no product exists with the given ID
     * @throws IllegalArgumentException if the provided ID is not a valid UUID
     */
    @Transactional
    public APIResponse<Product> deleteProductById(String id) {
        Optional<Product> optionalProduct = productRepository.findById(UUID.fromString(id));
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            productRepository.delete(product);
        } else throw new NotFoundException("Product not found with ID: " + id);
        return new APIResponse<Product>(false, 200, null, "Product deleted successfully");
    }

    /**
     * Deletes multiple products from the database by their IDs.
     * <p>
     * This method attempts to delete multiple products using the provided list of IDs.
     * If any product is not found, the process will be stopped and throw an exception.
     * </p>
     *
     * @param deleteMultiProductDTO  Data transfer object contain list of product unique identifiers to delete (List of UUID as String)
     * @return {@link APIResponse} containing deletion result
     * @throws NotFoundException if any product in the list doesn't exist
     * @throws IllegalArgumentException if any provided ID is not a valid UUID
     */
    @Transactional
    public APIResponse<Product> deleteProducts(DeleteMultiProductDTO deleteMultiProductDTO) {
        List<String> productIds = deleteMultiProductDTO.getProductIds();
        for (String id : productIds) {
            Optional<Product> optionalProduct = productRepository.findById(UUID.fromString(id));
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                productRepository.delete(product);
            } else {
                throw new NotFoundException("Product not found with ID: " + id);
            }
        }
        return new APIResponse<>(false, 200, null, "Products deleted successfully");
    }

    /**
     * Updates a product in the database by its ID.
     * <p>
     * This method attempts to update a product using the provided ID and DTO data.
     * If the product is not found, the process will be stopped and throw an exception.
     * </p>
     *
     * @param productId Product unique identifier to update
     * @param updateProductDTO Product Data Transfer Object containing updated data
     * @return {@link APIResponse<Product>} containing the updated product
     * @throws NotFoundException if product doesn't exist
     * @throws IllegalArgumentException if provided ID is not a valid UUID
     */
    @Transactional
    public APIResponse<Product> updateProductById(String productId, ProductDTO updateProductDTO) {
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
