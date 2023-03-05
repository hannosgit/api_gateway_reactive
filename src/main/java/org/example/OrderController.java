package org.example;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Base64.Decoder DECODER = Base64.getDecoder();
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
    public Mono<OrderDetails> getOrderById(@PathVariable long orderId, @RequestHeader("Authorization") String authorization) {
        final String basic = authorization.substring("Basic".length()).trim();
        final byte[] credDecoded = DECODER.decode(basic);
        final String credentials = new String(credDecoded, StandardCharsets.UTF_8);

        final String[] split = credentials.split(":");
        final String username = split[0];
        final String password = split[1];

        return orderService.fetchOrderDetails(orderId, username, password);
    }

}
