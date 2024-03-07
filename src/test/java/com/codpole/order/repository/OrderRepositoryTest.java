package com.codpole.order.repository;


import com.codpole.order.model.Order;
import com.codpole.order.util.DataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderRepositoryTest {

    @Mock
    OrderRepository orderRepository;

    @Test
    void testCreateOrder() {
        var mockCreatedOrder = DataProvider.getMockOrder();
        when(orderRepository.save(any(Order.class))).thenReturn(mockCreatedOrder);
        var createdOrder = orderRepository.save(mockCreatedOrder);
        assertEquals(mockCreatedOrder.getOrderId(), createdOrder.getOrderId());
        assertEquals(mockCreatedOrder.getUserId(), createdOrder.getUserId());
        assertEquals(mockCreatedOrder.getOrderDate(), createdOrder.getOrderDate());
    }

    @Test
    void testFindByUserId() {
        var mockOrders = DataProvider.getMockOrders();
        when(orderRepository.findByUserId(anyString())).thenReturn(mockOrders);
        var orders = orderRepository.findByUserId(DataProvider.USER_ID);
        verify(orderRepository, times(1)).findByUserId(DataProvider.USER_ID);
        assertNotNull(orders);
        assertEquals(mockOrders.size(), orders.size());
        assertEquals(mockOrders.get(0).getOrderId(), orders.get(0).getOrderId());
        assertEquals(mockOrders.get(0).getUserId(), orders.get(0).getUserId());
        assertEquals(mockOrders.get(0).getOrderDate(), orders.get(0).getOrderDate());
    }


}
