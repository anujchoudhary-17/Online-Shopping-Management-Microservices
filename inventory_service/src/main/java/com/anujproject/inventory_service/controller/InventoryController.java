package com.anujproject.inventory_service.controller;


import com.anujproject.inventory_service.dto.InventoryResponse;
import com.anujproject.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {


    private final InventoryService inventoryService;
    // http://localhost:8082/api/inventory/iphone-13,iphone-13-red

    // http://localhost:8082/api/inventory?sku-code=iphone-13&sku-code=iphone13-red
    // If we have multiple Skus requested by the Order Microservice to check whether these items are in the stock or not rather than making multiple requests,
    // just make one request with multiple skus sent in it

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
