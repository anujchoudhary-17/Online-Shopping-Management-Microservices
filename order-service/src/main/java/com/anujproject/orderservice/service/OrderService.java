package com.anujproject.orderservice.service;

import com.anujproject.orderservice.config.WebClientConfig;
import com.anujproject.orderservice.dto.InventoryResponse;
import com.anujproject.orderservice.dto.OrderLineItemsDTO;
import com.anujproject.orderservice.dto.OrderReponse;
import com.anujproject.orderservice.dto.OrderRequest;
import com.anujproject.orderservice.event.OrderPlacedEvent;
import com.anujproject.orderservice.model.Order;
import com.anujproject.orderservice.model.OrderLineItems;
import com.anujproject.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList  = orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(orderLineItemsDTO -> mapToDTO(orderLineItemsDTO))
                .toList();


        order.setOrderLineItemsList(orderLineItemsList);
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        // Call Inventory service, we will place order if it is available in the Inventory
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get().uri("http://inventory-service/api/inventory",uriBuilder ->
                        uriBuilder.queryParam("skuCode",skuCodes).build()).
                retrieve().
                bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());
        if (allProductsInStock)
        {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
            return "Order placed Successfully !!!";
        }
        else
            throw new IllegalArgumentException("Product is not in stock, please try again later");
    }


    public List<OrderReponse> getOrders(){


        List<Order> ordersList  = orderRepository.findAll();

        return  ordersList.stream().map(this::mapToOrderResponse).toList();
    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }


    private OrderReponse mapToOrderResponse(Order order){
        return OrderReponse.builder().
                id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(order.getOrderLineItemsList())
                .build();
    }
}
