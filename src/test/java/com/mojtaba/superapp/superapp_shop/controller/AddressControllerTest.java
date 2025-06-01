package com.mojtaba.superapp.superapp_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojtaba.superapp.superapp_shop.dto.AddressDto;
import com.mojtaba.superapp.superapp_shop.dto.CreateAddressDto;
import com.mojtaba.superapp.superapp_shop.dto.UpdateAddressDto;
import com.mojtaba.superapp.superapp_shop.entity.Address;
import com.mojtaba.superapp.superapp_shop.entity.User;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.service.AddressService;
import com.mojtaba.superapp.superapp_shop.service.UserService;
import com.mojtaba.superapp.superapp_shop.util.AddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    private MockMvc mvc;

    @Mock
    private AddressService addressService;

    @Mock
    private UserService userService;

    @Mock
    private AddressMapper mapper;

    @InjectMocks
    private AddressController addressController;

    private ObjectMapper objectMapper;

    private final GeometryFactory gf = new GeometryFactory();
    private User dummyUser;
    private Address dummyAddress;
    private AddressDto dummyDto;


    @RestControllerAdvice
    static class TestAdvice {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Void> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();


        mvc = MockMvcBuilders
                .standaloneSetup(addressController)
                .setControllerAdvice(new TestAdvice())
                .build();


        dummyUser = new User();
        dummyUser.setUserId(55L);

        dummyAddress = new Address();
        dummyAddress.setAddressId(101L);
        dummyAddress.setUser(dummyUser);
        dummyAddress.setLabel("LabelX");
        dummyAddress.setFullAddress("AddrX");
        dummyAddress.setCity("CityX");
        dummyAddress.setProvince("ProvX");
        dummyAddress.setPostalCode("ZIPX");
        Point p = gf.createPoint(new Coordinate(1.1, 2.2));
        dummyAddress.setLocation(p);

        dummyDto = new AddressDto();
        dummyDto.setAddressId(101L);
        dummyDto.setUserId(55L);
        dummyDto.setLabel("LabelX");
        dummyDto.setFullAddress("AddrX");
        dummyDto.setCity("CityX");
        dummyDto.setProvince("ProvX");
        dummyDto.setPostalCode("ZIPX");
        dummyDto.setLocationWkt("POINT (1.1 2.2)");
    }

    @Test
    void create_validInput_shouldReturn201AndDto() throws Exception {
        CreateAddressDto createDto = new CreateAddressDto();
        createDto.setUserId(dummyUser.getUserId());
        createDto.setLabel("LabelX");
        createDto.setFullAddress("AddrX");
        createDto.setCity("CityX");
        createDto.setProvince("ProvX");
        createDto.setPostalCode("ZIPX");
        createDto.setLocationWkt("POINT(1.1 2.2)");

        when(userService.getUserById(55L)).thenReturn(Optional.of(dummyUser));
        when(mapper.fromCreateDto(eq(createDto), eq(dummyUser))).thenReturn(dummyAddress);
        when(addressService.create(dummyAddress)).thenReturn(dummyAddress);
        when(mapper.toDto(dummyAddress)).thenReturn(dummyDto);

        mvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.addressId").value(101))
                .andExpect(jsonPath("$.userId").value(55))
                .andExpect(jsonPath("$.label").value("LabelX"));

        verify(userService).getUserById(55L);
        verify(mapper).fromCreateDto(eq(createDto), eq(dummyUser));
        verify(addressService).create(dummyAddress);
        verify(mapper).toDto(dummyAddress);
    }

    @Test
    void create_nonExistingUser_shouldReturn404() throws Exception {
        CreateAddressDto createDto = new CreateAddressDto();
        createDto.setUserId(999L);
        createDto.setLabel("L");
        createDto.setFullAddress("F");
        createDto.setCity("C");
        createDto.setProvince("P");
        createDto.setPostalCode("Z");
        createDto.setLocationWkt("POINT(0 0)");

        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        mvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_whenNoAddresses_shouldReturnEmptyList() throws Exception {
        when(addressService.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/addresses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getByUser_whenExists_shouldReturnList() throws Exception {
        when(addressService.findByUserId(55L)).thenReturn(Collections.singletonList(dummyAddress));
        when(mapper.toDto(dummyAddress)).thenReturn(dummyDto);

        mvc.perform(get("/api/addresses/user/55")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].addressId").value(101))
                .andExpect(jsonPath("$[0].label").value("LabelX"));
    }

    @Test
    void getOne_whenExists_shouldReturnDto() throws Exception {
        when(addressService.findById(101L)).thenReturn(Optional.of(dummyAddress));
        when(mapper.toDto(dummyAddress)).thenReturn(dummyDto);

        mvc.perform(get("/api/addresses/101")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(101))
                .andExpect(jsonPath("$.label").value("LabelX"));
    }

    @Test
    void getOne_whenNotFound_shouldReturn404() throws Exception {
        when(addressService.findById(123L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/addresses/123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_whenExists_shouldReturnUpdatedDto() throws Exception {
        UpdateAddressDto updateDto = new UpdateAddressDto();
        updateDto.setLabel("NewLabel");
        updateDto.setFullAddress("NewAddr");

        when(addressService.findById(101L)).thenReturn(Optional.of(dummyAddress));
        doNothing().when(mapper).updateFromDto(dummyAddress, updateDto);

        Address updatedEntity = new Address();
        updatedEntity.setAddressId(101L);
        updatedEntity.setUser(dummyUser);
        updatedEntity.setLabel("NewLabel");
        updatedEntity.setFullAddress("NewAddr");
        updatedEntity.setCity("CityX");
        updatedEntity.setProvince("ProvX");
        updatedEntity.setPostalCode("ZIPX");
        updatedEntity.setLocation(dummyAddress.getLocation());

        when(addressService.update(eq(101L), any(Address.class))).thenReturn(updatedEntity);

        AddressDto updatedDto = new AddressDto();
        updatedDto.setAddressId(101L);
        updatedDto.setUserId(55L);
        updatedDto.setLabel("NewLabel");
        updatedDto.setFullAddress("NewAddr");
        updatedDto.setCity("CityX");
        updatedDto.setProvince("ProvX");
        updatedDto.setPostalCode("ZIPX");
        updatedDto.setLocationWkt("POINT (1.1 2.2)");

        when(mapper.toDto(updatedEntity)).thenReturn(updatedDto);

        mvc.perform(put("/api/addresses/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("NewLabel"))
                .andExpect(jsonPath("$.fullAddress").value("NewAddr"));
    }

    @Test
    void update_whenNotFound_shouldReturn404() throws Exception {
        UpdateAddressDto updateDto = new UpdateAddressDto();
        updateDto.setLabel("L");
        when(addressService.findById(999L)).thenReturn(Optional.empty());

        mvc.perform(put("/api/addresses/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenExists_shouldReturn204() throws Exception {
        doNothing().when(addressService).delete(101L);

        mvc.perform(delete("/api/addresses/101"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenNotFound_shouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Address", "id", 999L))
                .when(addressService).delete(999L);

        mvc.perform(delete("/api/addresses/999"))
                .andExpect(status().isNotFound());
    }
}
