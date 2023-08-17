package com.example.bookwonders.controller;

import com.example.bookwonders.dto.order.OrderItemResponseDto;
import com.example.bookwonders.dto.order.OrderResponseDto;
import com.example.bookwonders.dto.order.PlaceOrderDto;
import com.example.bookwonders.dto.order.UpdateOrderStatusDto;
import com.example.bookwonders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "API for managing orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Retrieve user's order history")
    @ApiResponse(content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = OrderResponseDto.class))})
    public List<OrderResponseDto> getOrderHistory(@ParameterObject Pageable pageable) {
        return orderService.getOrderHistory(pageable);
    }

    @PostMapping
    @Operation(summary = "Place an order")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201",
            description = "Order placed successfully",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponseDto.class))})
    public OrderResponseDto placeOrder(PlaceOrderDto placeOrderDto) {
        return orderService.placeOrder(placeOrderDto);
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status. (Only for admin)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponse(responseCode = "202",
            description = "Order status updated successfully")
    public void updateOrderStatus(@PathVariable Long orderId,
                                  UpdateOrderStatusDto updateOrderStatusDto) {
        orderService.updateOrderStatus(orderId, updateOrderStatusDto);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Retrieve all order items for a specific order")
    @ApiResponse(content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderItemResponseDto.class))})
    public List<OrderItemResponseDto> getAllOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Retrieve a specific order item within an order")
    @ApiResponse(content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = OrderItemResponseDto.class))})
    public OrderItemResponseDto getOrderItemById(@PathVariable Long orderId,
                                                 @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }
}
