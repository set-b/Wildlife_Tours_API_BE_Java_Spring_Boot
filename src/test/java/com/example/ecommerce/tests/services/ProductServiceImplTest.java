package com.example.ecommerce.tests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.Conflict;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.services.ProductServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;

/**
 * Contains the unit tests for the ProductServiceImpl.
 */
class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;
  @Mock
  private ProductService productService;
  private AutoCloseable closeable;

  @InjectMocks
  ProductServiceImpl productServiceImpl;

  Product product;
  List<Product> productList = new ArrayList<>();

  @BeforeEach
  public void setUp() throws Exception {
    closeable = MockitoAnnotations.openMocks(this);
    product = new Product("MD5-354", "Women's Fashion", "Wide Brim Sunhat",
        "UPF 50+ Sun Hat to protect against UV rays", "Keeper's", new BigDecimal("15.95"));
  }

  @AfterEach
  public void tearDown() throws Exception {
    closeable.close();
  }

  @Test
  public void queryProductsReturnsCorrectProducts() {
    when(productRepository.findAll()).thenReturn(productList);
    List<Product> result = productServiceImpl.queryProducts(new Product());
    assertEquals(productList, result);
  }

  @Test
  public void queryProductsByExample() {
    when(productRepository.findAll(any(Example.class))).thenReturn(productList);
    List<Product> result = productServiceImpl.queryProducts(product);
    assertEquals(productList, result);
  }

  @Test
  public void queryProductsThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(productRepository).findAll();
    assertThrows(ServiceUnavailable.class,
        () -> productServiceImpl.queryProducts(new Product()));
  }

  @Test
  public void getProductByExistingIdReturnsProduct() {
    when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
    Product result = productServiceImpl.getProductById(1L);
    assertEquals(product, result);
  }

  @Test
  public void getProductByNonExistentIdThrowsNotFound() {
    when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> productServiceImpl.getProductById(999L));
  }

  @Test
  public void getProductByNegativeIdThrowsBadDataResponse() {
    when(productRepository.findById(any(Long.class))).thenThrow(BadDataResponse.class);
    assertThrows(BadDataResponse.class, () -> productServiceImpl.getProductById(-18L));
  }

  @Test
  public void getProductByIdThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(productRepository).findById(any(Long.class));
    assertThrows(ServiceUnavailable.class, () -> productServiceImpl.getProductById(1L));
  }

  @Test
  public void postProductWithValidBodyCreatesSuccessfully() {
    when(productRepository.save(any(Product.class))).thenReturn(product);
    Product result = productServiceImpl.addProduct(new Product());
    assertEquals(product, result);
  }

  @Test
  void postProductWithExistingEmailThrowsConflict409() {
    when(productRepository.existsBySku(any(String.class))).thenReturn(true);
    assertThrows(Conflict.class, () -> productServiceImpl.addProduct(product));
  }

  @Test
  public void postProductThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(productRepository).save(any(Product.class));
    assertThrows(ServiceUnavailable.class,
        () -> productServiceImpl.addProduct(new Product()));
  }

  @Test
  public void updateProductWithValidIdAndBodyReturnsProductSuccessfully() {
    when(productRepository.existsById(any(Long.class))).thenReturn(true);
    when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
    when(productRepository.save(any(Product.class))).thenReturn(product);
    Product result = productServiceImpl.updateProductById(1L, new Product());
    assertEquals(product, result);
  }

  @Test
  public void updateProductWithNonExistentIdThrowsNotFound() {
    when(productRepository.existsById(any(Long.class))).thenReturn(false);
    assertThrows(ResourceNotFound.class,
        () -> productServiceImpl.updateProductById(999L, new Product()));
  }

  @Test
  public void updateProductWithNegativeIdThrowsBadDataResponse() {
    assertThrows(BadDataResponse.class,
        () -> productServiceImpl.updateProductById(-8L, new Product()));
  }

  @Test
  public void updateProductThrowsServiceUnavailable() {
    when(productRepository.existsById(any(Long.class))).thenReturn(true);
    when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
    doThrow(ServiceUnavailable.class).when(productRepository).save(any(Product.class));
    assertThrows(ServiceUnavailable.class,
        () -> productServiceImpl.updateProductById(1L, new Product()));
  }

  @Test
  public void updateProductWithEmailThatAlreadyExistsThrowsConflict409() {
    when(productRepository.existsById(any(Long.class))).thenReturn(true);
    when(productRepository.existsBySku(any(String.class))).thenReturn(true);
    assertThrows(Conflict.class, () -> productServiceImpl.updateProductById(1L, product));
  }

  @Test
  public void deleteProductByExistentIdReturns204NoContent() {
    when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
    productServiceImpl.deleteProductById(1L);
    verify(productRepository).deleteById(any());
  }

  @Test
  public void deleteProductByNonExistentIdThrows404NotFound() {
    when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> productServiceImpl.deleteProductById(999L));
  }

  @Test
  public void deleteProductByNegativeIdThrows400BadDataResponse() {
    assertThrows(BadDataResponse.class, () -> productServiceImpl.deleteProductById(-9L));
  }

  @Test
  public void deleteProductThrowsServiceUnavailable() {
    when(productRepository.existsById(any(Long.class))).thenReturn(true);
    when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
    doThrow(ServiceUnavailable.class).when(productRepository).deleteById(any(Long.class));
    assertThrows(ServiceUnavailable.class, () -> productServiceImpl.deleteProductById(1L));
  }

  @Test
  public void deleteEmptyProductThrowsResourceNotFound() {
    when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> productServiceImpl.deleteProductById(40L));
  }
}