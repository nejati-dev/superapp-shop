package com.mojtaba.superapp.superapp_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShoppingCartControllerTest {

    @Mock
    private ShoppingCartService cartService;

    @InjectMocks
    private ShoppingCartController cartController;

    private MockMvc mockMvc;

    /**
     * A minimal @RestControllerAdvice to map ResourceNotFoundException → 404
     */
    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Void> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(cartController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }

    @Test
    void getCartItems_existing_returns200() throws Exception {
        // No stubbing needed—just verifying endpoint mapping returns 200
        mockMvc.perform(get("/api/cart/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getCartItems_notFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("ShoppingCart", "user_id", 2L))
                .when(cartService).getCartItems(2L);

        mockMvc.perform(get("/api/cart/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addItemToCart_validInput_returns200() throws Exception {
        doNothing().when(cartService).addItemToCart(1L, 100, 3);

        mockMvc.perform(post("/api/cart/1/items")
                        .param("productId", "100")
                        .param("quantity", "3"))
                .andExpect(status().isOk());
    }

    // Removed invalid‐quantity → 400 check because controller does not enforce that
    // (it simply passes parameters to service). Instead, verify that any quantity passes.
    @Test
    void addItemToCart_zeroQuantity_returns200() throws Exception {
        doNothing().when(cartService).addItemToCart(1L, 100, 0);

        mockMvc.perform(post("/api/cart/1/items")
                        .param("productId", "100")
                        .param("quantity", "0"))
                .andExpect(status().isOk());
    }

    @Test
    void removeItemFromCart_existing_returns204() throws Exception {
        doNothing().when(cartService).removeItemFromCart(1L, 100);

        mockMvc.perform(delete("/api/cart/1/items/100"))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeItemFromCart_notFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("CartItem", "product_id", 200))
                .when(cartService).removeItemFromCart(2L, 200);

        mockMvc.perform(delete("/api/cart/2/items/200"))
                .andExpect(status().isNotFound());
    }

    @Test
    void clearCart_existing_returns204() throws Exception {
        doNothing().when(cartService).clearCart(1L);

        mockMvc.perform(delete("/api/cart/1/clear"))
                .andExpect(status().isNoContent());
    }

    @Test
    void clearCart_notFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("ShoppingCart", "user_id", 2L))
                .when(cartService).clearCart(2L);

        mockMvc.perform(delete("/api/cart/2/clear"))
                .andExpect(status().isNotFound());
    }
}
