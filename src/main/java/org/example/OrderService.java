package org.example;

import org.example.data.ApiCredentials;
import org.example.data.BillInfo;
import org.example.data.Delivery;
import org.example.data.OrderDetails;
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

    public Mono<OrderDetails> fetchOrderDetails(long orderId, String username, String password) {
        final Mono<String> tokenMono = authService.fetchToken(username, password);

        return tokenMono.flatMap(token -> {
            final Mono<Delivery> deliveryMono = deliveryService.fetchDelivery(orderId, token);
            final Mono<BillInfo> billInfoMono = accountingService.fetchBillInfoForOrder(orderId, token);

            return Mono.zip(deliveryMono, billInfoMono, (delivery, billInfo) -> new OrderDetails(orderId, delivery, billInfo));
        });
    }
}
