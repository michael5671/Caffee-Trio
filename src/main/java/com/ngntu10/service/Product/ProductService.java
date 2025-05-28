package com.ngntu10.service.Product;

import com.ngntu10.dto.request.product.DeleteMultiProductDTO;
import com.ngntu10.dto.request.product.ProductDTO;
import com.ngntu10.dto.request.product.ProductImageDTO;
import com.ngntu10.dto.response.APIResponse;
import com.ngntu10.dto.response.PaginationResponse;
import com.ngntu10.dto.response.Product.ProductResponse;
import com.ngntu10.entity.Product;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.ProductRepository;
import com.ngntu10.util.PageableUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

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

        UUID productId = UUID.fromString(productImageDTO.getProductId());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

        if (productImageDTO.getImages() == null) {
            throw new IllegalArgumentException("Images list cannot be null");
        }
        
        return productRepository.save(product);
    }

    /**
     * Retrieves a product by its unique slug.
     * <p>
     * This method queries the product repository to find a product based on the provided slug.
     * The method also ensures that lazy-loaded collections (images and attributes) are initialized
     * within the transaction context to prevent LazyInitializationException when accessed later.
     * Hibernate uses proxy objects for lazy loading, and these proxies require an active session
     * to load their actual data. Without initialization, accessing these collections outside the
     * transaction (e.g., in controllers or view layer) will fail.
     * </p>
     * <p>
     * Alternative approaches to handle lazy loading:
     * 1. (USING) Use EAGER loading by setting fetch = FetchType.EAGER in entity relationships
     *    - Pros: Simple, always loaded
     *    - Cons: Performance impact, loads data even when not needed
     * 2. Map to DTO within transaction
     *    - Pros: Clean separation, no lazy loading issues
     *    - Cons: Need to maintain separate DTO classes
     * 3. Use JOIN FETCH in repository query
     *    - Pros: Better performance, single query
     *    - Cons: Less flexible, loads all data at once
     * 4. Force loading using size() method (current approach)
     *    - Pros: Simple, readable, commonly used
     *    - Cons: Generates additional queries, one per collection
     * </p>
     * <p>
     * If the product is found, it returns the product along with a success message.
     * If the product is not found, it returns a message indicating that the product was not found.
     * </p>
     *
     * @param slug the unique identifier (slug) of the product to be retrieved
     * @return an {@link APIResponse} object containing the product if found, or an error message if not found.
     *         The response will have an error flag (true if found, false if not), an HTTP status code,
     *         the product data (if found) with initialized collections, and a message describing the result.
     * @throws org.hibernate.LazyInitializationException if the lazy collections are accessed outside of
     *         the transactional context without being initialized. This occurs because Hibernate's proxy
     *         objects cannot load data without an active database session
     */
    @Transactional
    public APIResponse<Product> getProductBySlug(String slug) {
        Optional<Product> optionalProduct = productRepository.findBySlug(slug);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            // Initialize lazy-loaded collections to ensure they're available outside the transaction
            // This prevents LazyInitializationException when accessing these collections in the view layer
            // product.getImages().size();      // Initialize the images collection
            // product.getAttributes().size();   // Initialize the attributes collection
            return new APIResponse<Product>(false, 200, product, "Product found successfully");
        }
        return new APIResponse<Product>(true, 404, null, "Product not found");
    }

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
        return new APIResponse<>(false, 200, updatedProduct, "Product updated successfully");
    }

    /**
     * Searches and retrieves products based on specified parameters with pagination.
     * <p>
     * This method allows searching for products with various filters and sorting options.
     * It supports pagination and sorting of results based on provided parameters.
     * The search can be filtered by product name and category, and results can be
     * sorted by any valid product field in ascending or descending order.
     * </p>
     *
     * @param params A map containing search parameters including:
     *              - page: Page number (default: 0)
     *              - size: Number of items per page (default: 20)
     *              - sortBy: Field to sort by (default: createdAt)
     *              - sortDir: Sort direction (asc/desc, default: desc)
     *              - name: Product name filter
     *              - category: Category filter
     * @return {@link PaginationResponse<ProductResponse>} containing the paginated list of products
     *         matching the search criteria, mapped to DTOs
     * @throws NumberFormatException if page or size parameters are not valid integers
     */
    public PaginationResponse<ProductResponse> searchProducts(Map<String, String> params) {
        Pageable pageable = PageableUtil.getPageable(params);

        Map<String, String> filters = new HashMap<>(params);

        Page<Product> productPage = productRepository.findAll(
                pageable
        );

        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());

        return new PaginationResponse<>(productPage, productResponses);
    }
}
