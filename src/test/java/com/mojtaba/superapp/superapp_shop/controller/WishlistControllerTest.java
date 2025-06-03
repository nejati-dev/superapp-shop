package com.mojtaba.superapp.superapp_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojtaba.superapp.superapp_shop.dto.WishlistDto;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.WishlistService;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Void> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(wishlistController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }

    @Test
    void getWishlistItems_existing_returnsList() throws Exception {
        WishlistDto dto = new WishlistDto();
        dto.setWishlistId(15L);
        dto.setProductId(200);
        dto.setProductName("SKU200");
        dto.setProductPrice(new BigDecimal("75.00"));
        dto.setAddedAt(Instant.now());

        when(wishlistService.getWishlistItems(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/wishlist/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(200))
                .andExpect(jsonPath("$[0].productPrice").value(75.00));
    }

    @Test
    void getWishlistItems_notFound_returns404() throws Exception {
        when(wishlistService.getWishlistItems(2L))
                .thenThrow(new ResourceNotFoundException("User", "id", 2L));

        mockMvc.perform(get("/api/wishlist/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addProductToWishlist_successful_returns200() throws Exception {
        doNothing().when(wishlistService).addProductToWishlist(1L, 200);

        mockMvc.perform(post("/api/wishlist/1/products/200"))
                .andExpect(status().isOk());
    }

    @Test
    void addProductToWishlist_notFoundUser_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("User", "id", 3L))
                .when(wishlistService).addProductToWishlist(3L, 300);

        mockMvc.perform(post("/api/wishlist/3/products/300"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeProductFromWishlist_successful_returns204() throws Exception {
        doNothing().when(wishlistService).removeProductFromWishlist(1L, 200);

        mockMvc.perform(delete("/api/wishlist/1/products/200"))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeProductFromWishlist_notFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Wishlist", "combination user_id & product_id", "2-200"))
                .when(wishlistService).removeProductFromWishlist(2L, 200);

        mockMvc.perform(delete("/api/wishlist/2/products/200"))
                .andExpect(status().isNotFound());
    }
}
