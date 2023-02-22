package com.anujproject.orderservice.controller;

import com.anujproject.orderservice.dto.OrderReponse;
import com.anujproject.orderservice.dto.OrderRequest;
import com.anujproject.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderReponse> getOrders(){
        return orderService.getOrders();
    }
}
