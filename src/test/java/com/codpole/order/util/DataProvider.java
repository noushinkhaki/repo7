package com.codpole.order.util;

import com.codpole.order.model.Item;
import com.codpole.order.model.ItemRequest;
import com.codpole.order.model.Order;
import com.codpole.order.model.OrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    public static final Long ORDER_ID = 100L;
    public static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    public static final String USER_ID_1 = "550e8400-e29b-41d4-a716-446655440001";
    public static final String USER_ID_2 = "550e8400-e29b-41d4-a716-446655440002";
    public static final String USER_ID_3 = "550e8400-e29b-41d4-a716-446655440003";

    public static final String ITEM_ID = "550e8400-e29b-41d4-a716-446655440036";

    public static final String ITEM_ID_1 = "550e8400-e29b-41d4-a716-446655440037";
    public static final String ITEM_ID_2 = "550e8400-e29b-41d4-a716-446655440038";
    public static final String ITEM_ID_3 = "550e8400-e29b-41d4-a716-446655440039";
    public static final String ITEM_ID_4 = "550e8400-e29b-41d4-a716-446655440034";
    public static final String ITEM_ID_5 = "550e8400-e29b-41d4-a716-446655440035";
    public static final String ITEM_NAME = "apple";
    public static final Long QUANTITY = 3L;
    public static final Double UNIT_PRICE = 20.5D;

    public static Order getMockOrder() {
        var mockOrder = new Order();
        mockOrder.setOrderId(ORDER_ID);
        mockOrder.setUserId(USER_ID);
        mockOrder.setOrderDate(Instant.now());
        var mockItem = getMockItem();
        mockOrder.setItems(List.of(mockItem));
        return mockOrder;
    }

    public static Item getMockItem() {
        var mockItem = new Item();
        mockItem.setItemId(ITEM_ID);
        mockItem.setItemName(ITEM_NAME);
        mockItem.setQuantity(QUANTITY);
        mockItem.setUnitPrice(UNIT_PRICE);
        mockItem.setOrderId(ORDER_ID);
        return mockItem;
    }

    public static OrderRequest getMockOrderRequest() {
        var mockOrderRequest = new OrderRequest();
        var mockItemRequest = getMockItemRequest();
        mockOrderRequest.setItems(List.of(mockItemRequest));
        return mockOrderRequest;
    }

    public static ItemRequest getMockItemRequest() {
        var mockItemRequest = new ItemRequest();
        mockItemRequest.setItemId(ITEM_ID);
        mockItemRequest.setItemName(ITEM_NAME);
        mockItemRequest.setQuantity(String.valueOf(QUANTITY));
        mockItemRequest.setUnitPrice(String.valueOf(UNIT_PRICE));
        return mockItemRequest;
    }

    public static List<Order> getMockOrders() {
        var mockOrders = new ArrayList<Order>();
        var mockOrder = new Order();
        mockOrder.setOrderId(DataProvider.ORDER_ID);
        mockOrder.setUserId(DataProvider.USER_ID);
        mockOrder.setOrderDate(Instant.now());
        mockOrders.add(mockOrder);
        return mockOrders;
    }

    public static List<Order> getMockUserOrders() {
        var mockOrder = DataProvider.getMockOrder();
        return List.of(mockOrder);
    }

    public static String getOrderRequest() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(getMockOrderRequest());
    }

    public static String getOrderRequest1() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var mockOrderRequest = getMockOrderRequest();
        mockOrderRequest.getItems().get(0).setItemId(ITEM_ID_1);
        return ow.writeValueAsString(mockOrderRequest);
    }

    public static String getOrderRequest2() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var mockOrderRequest = getMockOrderRequest();
        mockOrderRequest.getItems().get(0).setItemId(ITEM_ID_2);
        return ow.writeValueAsString(mockOrderRequest);
    }

    public static String getOrderRequest3() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var mockOrderRequest = getMockOrderRequest();
        mockOrderRequest.getItems().get(0).setItemId(ITEM_ID_3);
        return ow.writeValueAsString(mockOrderRequest);
    }

    public static String getOrderRequest4() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var mockOrderRequest = getMockOrderRequest();
        mockOrderRequest.getItems().get(0).setItemId(ITEM_ID_4);
        mockOrderRequest.getItems().get(0).setQuantity("5");
        return ow.writeValueAsString(mockOrderRequest);
    }

    public static String getOrderRequest5() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var mockOrderRequest = getMockOrderRequest();
        mockOrderRequest.getItems().get(0).setItemId(ITEM_ID_5);
        mockOrderRequest.getItems().get(0).setQuantity("11");
        return ow.writeValueAsString(mockOrderRequest);
    }
}
