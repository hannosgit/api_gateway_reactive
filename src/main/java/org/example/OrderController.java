package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @ExceptionHandler(FetchException.class)
//    @ResponseStatus(HttpStatus.BAD_GATEWAY)
//    public ErrorResponse fetchException(FetchException fetchException) {
//        System.err.println(fetchException);
//        return new ErrorResponse(fetchException.getMessage());
//    }

    @GetMapping("/{orderId}")
    public Mono<OrderDetails> getOrderById(@PathVariable long orderId) {
        return orderService.fetchOrderDetails(orderId);
    }

}
