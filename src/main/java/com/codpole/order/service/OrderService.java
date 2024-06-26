package com.codpole.order.service;

import com.codpole.order.exception.ItemAlreadyExistsException;
import com.codpole.order.model.Item;
import com.codpole.order.model.ItemRequest;
import com.codpole.order.model.Order;
import com.codpole.order.model.OrderRequest;
import com.codpole.order.repository.ItemRepository;
import com.codpole.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    @Transactional
    public String saveOrder(String userId, OrderRequest orderRequest) {
        var itemsExist = checkIfItemExists(orderRequest.getItems());
        if (itemsExist.size() > 0) {
            for (Optional<Item> opt : itemsExist) {
                if (opt.isPresent()) {
                    throw new ItemAlreadyExistsException("Item already exists, item ID: " + opt.get().getItemId());

                }
            }
        }
        Order order = new Order();
        order.setOrderDate(Instant.now());
        order.setUserId(userId);
        var createdOrder = orderRepository.save(order);
        order.setItems(orderRequest.getItems().stream().map(itemRequest -> getItem(itemRequest, createdOrder)).toList());
        return String.valueOf(createdOrder.getOrderId());
    }

    private List<Optional<Item>> checkIfItemExists(List<ItemRequest> items) {
        return items.stream().map(itemRequest -> itemRepository.findById(itemRequest.getItemId())).toList();
    }

    private Item getItem(ItemRequest itemRequest, Order createdOrder) {
        Item item = new Item();
        item.setItemId(itemRequest.getItemId());
        item.setItemName(itemRequest.getItemName());
        item.setQuantity(Long.parseLong(itemRequest.getQuantity()));
        if (item.getQuantity() > 10)
            item.setUnitPrice(Double.parseDouble(itemRequest.getUnitPrice())*0.85);
        else if (item.getQuantity() >= 5)
            item.setUnitPrice(Double.parseDouble(itemRequest.getUnitPrice())*0.9);
        else
            item.setUnitPrice(Double.parseDouble(itemRequest.getUnitPrice()));
        item.setOrderId(createdOrder.getOrderId());
        return item;
    }

    public List<Order> getOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Double getTotalPrice(String userId, String fromStr, String toStr) {
        var orders = getOrders(userId);
        var from = Instant.parse(fromStr);
        var to = Instant.parse(toStr);
        orders = orders.stream().filter(o -> o.getOrderDate().compareTo(from) >= 0 && o.getOrderDate().compareTo(to) <= 0)
                .toList();

        return orders.stream().flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }
}
