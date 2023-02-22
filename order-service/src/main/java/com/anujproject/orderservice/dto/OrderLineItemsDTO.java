package com.anujproject.orderservice.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemsDTO {

    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
