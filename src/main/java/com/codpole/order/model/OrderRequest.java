package com.codpole.order.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @Valid
    @NotEmpty(message = "Request should contain at least 1 item")
    private List<ItemRequest> items;
}
