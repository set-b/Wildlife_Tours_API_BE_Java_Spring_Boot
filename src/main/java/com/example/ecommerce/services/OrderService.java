package com.example.ecommerce.services;

import com.example.ecommerce.models.Order;
import java.util.List;

/**
 * This interface contains the methods which are implemented in the OrderServiceImpl class.
 */
public interface OrderService {

  List<Order> queryOrders(Order order);

  Order getOrderById(Long id);

  Order addOrder(Order order);

  Order updateOrderById(Long id, Order order);

  void deleteOrderById(Long id);

}
