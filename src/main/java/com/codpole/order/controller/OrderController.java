package com.codpole.order.controller;

import com.codpole.order.model.ErrorResponse;
import com.codpole.order.model.OrderRequest;
import com.codpole.order.model.OrderResponse;
import com.codpole.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/order", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

    @Operation(
            summary = "Create Order REST API",
            description = "REST API to create new order"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @PostMapping
    public ResponseEntity<String> createOrder(
            @RequestHeader
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                    message = "userId should be in UUID format") String userId,
            @Valid @RequestBody OrderRequest orderRequest) {
        var orderId = orderService.saveOrder(userId, orderRequest);
        return new ResponseEntity<String>(String.valueOf(orderId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Order REST API",
            description = "REST API to get all orders for a user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @GetMapping(path = "/all")
    public ResponseEntity<OrderResponse> getOrders(
            @RequestHeader
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                                                           message = "userId should be in UUID format") String userId) {
        var orders = orderService.getOrders(userId);
        var orderResponse = new OrderResponse();
        orderResponse.setOrders(orders);
        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "GET Total Price REST API",
            description = "REST API to get total price of items for a user in a specified time"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @GetMapping(path = "/totalPrice")
    public ResponseEntity<String> getTotalPrice(
            @RequestHeader
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                    message = "userId should be in UUID format") String userId,
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to) {
        var totalPrice = orderService.getTotalPrice(userId, from, to);
        return new ResponseEntity<String>(String.valueOf(totalPrice), HttpStatus.OK);
    }
}
