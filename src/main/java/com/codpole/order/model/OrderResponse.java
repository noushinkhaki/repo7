package com.codpole.order.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

    private List<Order> orders;
}
