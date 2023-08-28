package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final WebClient.Builder webClientBuilder;
    private final OrderRepository ory;
    public void placeOrder(OrderRequest or){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> oli= or.getOldList().stream().map(old->mapToDto(old)).toList();
        order.setOli(oli);
        List<String> skuCodes = order.getOli().stream()
                                .map(orderLineItems -> orderLineItems.getSkuCode())
                                .toList();
        //call inventory service, and place order if product is in stock
        InventoryResponse[] inventoryResponseArray=webClientBuilder.build().get().uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                 .retrieve()
                 .bodyToMono(InventoryResponse[].class)
                 .block();
        boolean allProductsIsInStock=Arrays.stream(inventoryResponseArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());
        if(allProductsIsInStock)
        {ory.save(order);}
        else{
           throw new IllegalArgumentException("product is not in stock,please try again");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto old) {
        OrderLineItems oli=new OrderLineItems();
        oli.setPrice(old.getPrice());
        oli.setQuantity(old.getQuantity());
        oli.setSkuCode(old.getSkuCode());
        return oli;
    }
}
