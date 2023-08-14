package com.example.bookwonders.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@Tag(name = "ShoppingCart", description = "API for managing shopping carts")
public class ShoppingCartController {
}
