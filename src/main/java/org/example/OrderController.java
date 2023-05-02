package org.example;

import org.example.common.ApiCredentials;
import org.example.common.OrderDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final HeaderService headerService;
    private final OrderService orderService;

    public OrderController(HeaderService headerService, OrderService orderService) {
        this.headerService = headerService;
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public Mono<OrderDetails> getOrderById(@PathVariable long orderId, @RequestHeader("Authorization") String authorization) {
        final ApiCredentials apiCredentials = headerService.parseAuthorizationHeader(authorization);

        return orderService.fetchOrderDetails(orderId, apiCredentials);
    }

}
