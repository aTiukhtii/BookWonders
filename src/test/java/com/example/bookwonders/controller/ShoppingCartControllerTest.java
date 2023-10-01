package com.example.bookwonders.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookwonders.config.TestSecurityConfig;
import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.dto.cart.CartItemResponseDto;
import com.example.bookwonders.dto.cart.ShoppingCartResponseDto;
import com.example.bookwonders.dto.cart.UpdateBookQuantityInCartDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @Sql(scripts = "classpath:database/cart/add-data-for-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/delete-all-from-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get shopping cart by user from @WithMockUser")
    void getCart_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartResponseDto.class);
        ShoppingCartResponseDto expected = getCartResponseDto();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @Sql(scripts = {"classpath:database/cart/add-data-for-cart.sql",
            "classpath:database/cart/add-book-for-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/delete-all-from-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Add kobzar book to cart")
    void addBookToCart_Ok() throws Exception {
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto()
                .setBookId(3L)
                .setQuantity(3);
        String request = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(post("/cart")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartResponseDto.class);
        CartItemResponseDto createdBook = new CartItemResponseDto()
                .setId(3L)
                .setBookId(3L)
                .setQuantity(3)
                .setBookTitle("Created book");
        ShoppingCartResponseDto expected = getCartResponseDto();
        expected.getCartItems().add(createdBook);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @Sql(scripts = {"classpath:database/cart/add-data-for-cart.sql",
            "classpath:database/cart/add-book-for-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/delete-all-from-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Add book to cart with wrong id, expected status not found")
    void addBookToCart_NotOk() throws Exception {
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto()
                .setBookId(999L)
                .setQuantity(3);
        String request = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/cart")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @Sql(scripts = "classpath:database/cart/add-data-for-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/delete-all-from-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update quantity of kobzar book with id 1")
    void updateQuantityOfBook_Ok() throws Exception {
        int updatedQuantity = 55;
        UpdateBookQuantityInCartDto requestDto = new UpdateBookQuantityInCartDto(updatedQuantity);
        String request = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(put("/cart/cart-items/{cartItemId}", 1L)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        Set<CartItemResponseDto> cartItems = new HashSet<>();
        cartItems.add(getKobzarCartIemResponseDto().setQuantity(updatedQuantity));
        cartItems.add(getItCartItemResponseDto());
        ShoppingCartResponseDto expected = new ShoppingCartResponseDto()
                .setUserId(1L)
                .setCartItems(cartItems);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @Sql(scripts = "classpath:database/cart/add-data-for-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/delete-all-from-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update quantity of book with wrong id 999, expected status not found")
    void updateQuantityOfBook_NotOk() throws Exception {
        int updatedQuantity = 55;
        UpdateBookQuantityInCartDto requestDto = new UpdateBookQuantityInCartDto(updatedQuantity);
        String request = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(put("/cart/cart-items/{cartItemId}", 999L)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @Sql(scripts = "classpath:database/cart/add-data-for-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/delete-all-from-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete kobzar book from car with id 1")
    void deleteCartItemById1_Ok() throws Exception {
        mockMvc.perform(
                        delete("/cart/cart-items/{cartItemId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @Sql(scripts = "classpath:database/cart/add-data-for-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/delete-all-from-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete book with wrong id 999, expected status not found")
    void deleteCartItemById999_NotOk() throws Exception {
        mockMvc.perform(
                        delete("/cart/cart-items/{cartItemId}", 999L))
                .andExpect(status().isNotFound());
    }

    private ShoppingCartResponseDto getCartResponseDto() {
        Set<CartItemResponseDto> cartItems = new HashSet<>();
        cartItems.add(getKobzarCartIemResponseDto());
        cartItems.add(getItCartItemResponseDto());

        return new ShoppingCartResponseDto()
                .setUserId(1L)
                .setCartItems(cartItems);
    }

    private CartItemResponseDto getKobzarCartIemResponseDto() {
        return new CartItemResponseDto()
                .setId(1L)
                .setBookId(1L)
                .setBookTitle("Kobzar")
                .setQuantity(2);
    }

    private CartItemResponseDto getItCartItemResponseDto() {
        return new CartItemResponseDto()
                .setId(2L)
                .setBookId(2L)
                .setBookTitle("It")
                .setQuantity(4);
    }
}
