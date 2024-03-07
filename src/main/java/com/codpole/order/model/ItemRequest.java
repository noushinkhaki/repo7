package com.codpole.order.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemRequest {

    @NotEmpty(message = "itemId is required")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "itemId should be in UUID format")
    private String itemId;
    @NotEmpty(message = "itemName is required")
    private String itemName;
    @NotEmpty(message = "unitPrice is required")
    @Positive(message = "unitPrice should be a positive number")
    private String unitPrice;
    @NotEmpty(message = "quantity is required")
    @Positive(message = "quantity should be a positive number")
    private String quantity;
}
