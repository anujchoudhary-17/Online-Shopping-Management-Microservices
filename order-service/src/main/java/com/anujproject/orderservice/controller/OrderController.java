package com.anujproject.orderservice.controller;

import com.anujproject.orderservice.dto.OrderReponse;
import com.anujproject.orderservice.dto.OrderRequest;
import com.anujproject.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        log.info("Hello Sleuth");
        return CompletableFuture.supplyAsync(()->  orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Sorry!!! Something went wrong, Please order after sometime!");
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderReponse> getOrders(){
        return orderService.getOrders();
    }
}
