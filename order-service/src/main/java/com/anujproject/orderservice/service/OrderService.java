package com.anujproject.orderservice.service;

import com.anujproject.orderservice.dto.OrderLineItemsDTO;
import com.anujproject.orderservice.dto.OrderReponse;
import com.anujproject.orderservice.dto.OrderRequest;
import com.anujproject.orderservice.model.Order;
import com.anujproject.orderservice.model.OrderLineItems;
import com.anujproject.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList  = orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(orderLineItemsDTO -> mapToDTO(orderLineItemsDTO))
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        // Call Inventory service, we will place order if it is available in the Inventory

        orderRepository.save(order);
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
