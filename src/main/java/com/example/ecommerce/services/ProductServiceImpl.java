package com.example.ecommerce.services;

import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.Conflict;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.ProductRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * This class contains the methods that are called by the ProductController, and implemented from
 * the ProductService interface.
 */
@Service
public class ProductServiceImpl implements ProductService {

  private final Logger logger = LoggerFactory.getLogger(ProductService.class);

  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<Product> queryProducts(Product product) {
    try {
      if (product.isEmpty()) {
        List<Product> products = productRepository.findAll();
        products.sort(Comparator.comparing(Product::getId));
        return products;
      } else {
        Example<Product> productExample = Example.of(product);
        return productRepository.findAll(productExample);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public Product getProductById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Product productLookUpResult;
    try {
      productLookUpResult = productRepository.findById(id).orElse(null);
      if (productLookUpResult != null) {
        return productLookUpResult;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e.getMessage());
    }
    throw new ResourceNotFound(NOT_FOUND + " product with id " + id);
  }

  @Override
  public Product addProduct(Product product) {
    boolean productWithMatchingSku = productRepository.existsBySku(product.getSku());
    if (productWithMatchingSku) {
      throw new Conflict("SKU is already in use by another product!");
    }
    try {
      return productRepository.save(product);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public Product updateProductById(Long id, Product product) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    boolean productWithMatchingSku = productRepository.existsBySku(product.getSku());
    if (productWithMatchingSku) {
      throw new Conflict("SKU is already in use by another product!");
    }
    Product updatedProduct = null;
    if (!productRepository.existsById(id)) {
      throw new ResourceNotFound(NOT_FOUND + "product with id " + id);
    }
    try {
      product.setId(id);
      updatedProduct = productRepository.save(product);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    return updatedProduct;
  }

  @Override
  public void deleteProductById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    getProductById(id);
    try {
      productRepository.deleteById(id);
    } catch (Exception e) {
      throw new ServiceUnavailable("Something went wrong");
    }
  }
}
