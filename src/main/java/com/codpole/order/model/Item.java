package com.codpole.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @Column(name="item_id")
    private String itemId;
    @Column(name="item_name")
    private String itemName;
    @Column(name="unit_price")
    private Double unitPrice;
    @Column(name="quantity")
    private Long quantity;
    @Column(name="order_id")
    private Long orderId;
}
