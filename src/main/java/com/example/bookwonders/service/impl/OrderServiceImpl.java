package com.example.bookwonders.service.impl;

import com.example.bookwonders.dto.order.OrderItemResponseDto;
import com.example.bookwonders.dto.order.OrderResponseDto;
import com.example.bookwonders.dto.order.PlaceOrderDto;
import com.example.bookwonders.dto.order.UpdateOrderStatusDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.mapper.OrderItemMapper;
import com.example.bookwonders.mapper.OrderMapper;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.Order;
import com.example.bookwonders.model.OrderItem;
import com.example.bookwonders.model.ShoppingCart;
import com.example.bookwonders.repository.book.BookRepository;
import com.example.bookwonders.repository.cart.CartItemRepository;
import com.example.bookwonders.repository.cart.ShoppingCartRepository;
import com.example.bookwonders.repository.order.OrderRepository;
import com.example.bookwonders.service.OrderItemService;
import com.example.bookwonders.service.OrderService;
import com.example.bookwonders.service.ShoppingCartService;
import com.example.bookwonders.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;
    private final ShoppingCartService shoppingCartService;
    private final BookRepository bookRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(PlaceOrderDto placeOrderDto) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartModel();

        Order order = orderMapper.toOrderFromCart(shoppingCart);
        order.setShippingAddress(placeOrderDto.shippingAddress());
        Order savedOrder = orderRepository.save(order);

        Set<OrderItem> orderItems = getOrderItemsFromCart(shoppingCart);
        orderItems.forEach(orderItem -> orderItem.setOrder(savedOrder));
        savedOrder.setOrderItems(getSavedOrderItems(orderItems));
        completePurchase(shoppingCart);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public void updateOrderStatus(Long orderId, UpdateOrderStatusDto updateOrderStatusDto) {
        Order orderById = getOrderById(orderId);
        orderById.setStatus(updateOrderStatusDto.status());
        orderRepository.save(orderById);
    }

    @Override
    public List<OrderResponseDto> getOrderHistory(Pageable pageable) {
        return orderRepository.getAllByUser(pageable, userService.getUser())
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long orderId) {
        return getOrderById(orderId)
                .getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long itemId) {
        return orderItemService.findOrderItemByOrderIdAndId(orderId, itemId);
    }

    @Override
    public void completePurchase(ShoppingCart shoppingCart) {
        //        shoppingCartRepository.delete(shoppingCart);
        //        ShoppingCart shoppingCartNew = new ShoppingCart();
        //        shoppingCartNew.setUser(shoppingCart.getUser());
        //        shoppingCartRepository.save(shoppingCartNew);

        shoppingCart.setCartItems(new HashSet<>());
        cartItemRepository.deleteCartItemByShoppingCartId(shoppingCart.getId());
    }

    private Set<OrderItem> getOrderItemsFromCart(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems()
                .stream()
                .map(cartItem -> orderItemMapper.cartItemToOrderItem(cartItem,
                        getBookFromCartItem(cartItem)))
                .collect(Collectors.toSet());
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("can't find order by id: " + orderId));
    }

    private Set<OrderItem> getSavedOrderItems(Set<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(orderItemService::save)
                .collect(Collectors.toSet());
    }

    private Book getBookFromCartItem(CartItem cartItem) {
        return bookRepository.findById(cartItem.getBook().getId()).orElseThrow(() ->
                new EntityNotFoundException("can't find book by id: "
                        + cartItem.getBook().getId()));
    }
}
