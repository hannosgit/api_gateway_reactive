package org.example;

import org.example.common.ApiCredentials;
import org.example.common.BillInfo;
import org.example.common.Delivery;
import org.example.common.OrderDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final AuthService authService;

    private final AccountingService accountingService;

    private final DeliveryService deliveryService;

    public OrderService(AuthService authService, AccountingService accountingService, DeliveryService deliveryService) {
        this.authService = authService;
        this.accountingService = accountingService;
        this.deliveryService = deliveryService;
    }


    public Mono<OrderDetails> fetchOrderDetails(long orderId, ApiCredentials apiCredentials) {
        return fetchOrderDetails(orderId, apiCredentials.username(), apiCredentials.password());
    }

    private Mono<OrderDetails> fetchOrderDetails(long orderId, String username, String password) {
        final Mono<String> tokenMono = authService.fetchToken(username, password);

        return tokenMono.flatMap(token -> {
            final Mono<Delivery> deliveryMono = deliveryService.fetchDelivery(orderId, token);
            final Mono<BillInfo> billInfoMono = accountingService.fetchBillInfoForOrder(orderId, token);

            return Mono.zip(deliveryMono, billInfoMono, (delivery, billInfo) -> new OrderDetails(orderId, delivery, billInfo));
        });
    }
}
