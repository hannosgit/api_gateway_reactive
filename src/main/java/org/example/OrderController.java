package org.example;

import org.example.data.ApiCredentials;
import org.example.data.OrderDetails;
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

//    @ExceptionHandler(FetchException.class)
//    @ResponseStatus(HttpStatus.BAD_GATEWAY)
//    public ErrorResponse fetchException(FetchException fetchException) {
//        return new ErrorResponse(fetchException.getMessage());
//    }

    @GetMapping("/{orderId}")
    public Mono<OrderDetails> getOrderById(@PathVariable long orderId, @RequestHeader("Authorization") String authorization) {
        final ApiCredentials apiCredentials = headerService.parseAuthorizationHeader(authorization);

        return orderService.fetchOrderDetails(orderId, apiCredentials);
    }

}
