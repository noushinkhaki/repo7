package com.codpole.order.service;

import com.codpole.order.model.Item;
import com.codpole.order.model.ItemRequest;
import com.codpole.order.model.Order;
import com.codpole.order.repository.ItemRepository;
import com.codpole.order.repository.OrderRepository;
import com.codpole.order.util.DataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void testSaveOrder() {
        var mockOrder = DataProvider.getMockOrder();
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        var mockOrderRequest = DataProvider.getMockOrderRequest();
        var orderId = orderService.saveOrder(DataProvider.USER_ID, mockOrderRequest);
        verify(orderRepository, times(1)).save(any(Order.class));
        assertEquals(String.valueOf(mockOrder.getOrderId()), orderId);
    }

    @Test
    void testGetItem() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getItemMethod = OrderService.class.getDeclaredMethod("getItem", ItemRequest.class, Order.class);
        getItemMethod.setAccessible(true);
        var mockItemRequest = DataProvider.getMockItemRequest();
        var mockOrder = DataProvider.getMockOrder();
        var item1 = (Item) getItemMethod.invoke(orderService, mockItemRequest, mockOrder);
        assertEquals(mockItemRequest.getItemId(), item1.getItemId());
        assertEquals(mockItemRequest.getItemName(), item1.getItemName());
        assertEquals(mockItemRequest.getQuantity(), String.valueOf(item1.getQuantity()));
        assertEquals(mockItemRequest.getUnitPrice(), String.valueOf(item1.getUnitPrice()));
        // test if 5 or more of the same items in the order
        mockItemRequest.setQuantity(String.valueOf(5));
        var item2 = (Item) getItemMethod.invoke(orderService, mockItemRequest, mockOrder);
        assertEquals(Double.parseDouble(mockItemRequest.getUnitPrice())*0.9, item2.getUnitPrice());
        // test if more than 10 of the same items in the order
        mockItemRequest.setQuantity(String.valueOf(11));
        var item3 = (Item) getItemMethod.invoke(orderService, mockItemRequest, mockOrder);
        assertEquals(Double.parseDouble(mockItemRequest.getUnitPrice())*0.85, item3.getUnitPrice());
    }

    @Test
    void testGetOrders() {
        var mokUserOrders = DataProvider.getMockUserOrders();
        when(orderRepository.findByUserId(anyString())).thenReturn(mokUserOrders);
        var orders = orderService.getOrders(DataProvider.USER_ID);
        verify(orderRepository, times(1)).findByUserId(anyString());
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(DataProvider.USER_ID, orders.get(0).getUserId());
        assertEquals(DataProvider.ORDER_ID, orders.get(0).getOrderId());
        assertEquals(DataProvider.ITEM_ID, orders.get(0).getItems().get(0).getItemId());
    }

    @Test
    void testGetTotalPrice() {
        var fromStr = "2024-03-01T00:00:00Z";
        var toStr = "2124-03-01T00:00:00Z";
        var mokUserOrders = DataProvider.getMockUserOrders();
        when(orderRepository.findByUserId(anyString())).thenReturn(mokUserOrders);
        var totalPrice = orderService.getTotalPrice(DataProvider.USER_ID, fromStr, toStr);
        verify(orderRepository, times(1)).findByUserId(anyString());
        assertNotNull(totalPrice);
        assertEquals(DataProvider.UNIT_PRICE*DataProvider.QUANTITY, totalPrice);
    }
}
