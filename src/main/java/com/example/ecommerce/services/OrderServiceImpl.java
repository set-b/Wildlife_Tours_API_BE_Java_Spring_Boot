package com.example.ecommerce.services;

import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.repositories.OrderRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * This class contains the methods that are called by the OrderController, and implemented from the
 * OrderService interface.
 */
@Service
public class OrderServiceImpl implements OrderService {

  private final Logger logger = LoggerFactory.getLogger(OrderService.class);

  @Autowired
  private OrderRepository orderRepository;

  @Override
  public List<Order> queryOrders(Order order) {
    try {
      if (order.isEmpty()) {
        List<Order> orders = orderRepository.findAll();
        orders.sort(Comparator.comparing(Order::getId));
        return orders;
      } else {
        Example<Order> orderExample = Example.of(order);
        return orderRepository.findAll(orderExample);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public Order getOrderById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Order orderLookUpResult;
    try {
      orderLookUpResult = orderRepository.findById(id).orElse(null);
      if (orderLookUpResult != null) {
        return orderLookUpResult;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e.getMessage());
    }
    throw new ResourceNotFound(NOT_FOUND + " order with id " + id);
  }

  @Override
  public Order addOrder(Order order) {
    try {
      return orderRepository.save(order);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public Order updateOrderById(Long id, Order order) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Order updatedOrder = null;
    if (!orderRepository.existsById(id)) {
      throw new ResourceNotFound(NOT_FOUND + "order with id " + id);
    }
    try {
      order.setId(id);
      updatedOrder = orderRepository.save(order);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    return updatedOrder;
  }

  @Override
  public void deleteOrderById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    getOrderById(id);
    try {
      orderRepository.deleteById(id);
    } catch (Exception e) {
      throw new ServiceUnavailable("Something went wrong");
    }
  }
}
