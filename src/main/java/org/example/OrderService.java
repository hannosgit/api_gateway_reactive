package org.example;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final AccountingService accountingService;

    private final DeliveryService deliveryService;

    public OrderService(AccountingService accountingService, DeliveryService deliveryService) {
        this.accountingService = accountingService;
        this.deliveryService = deliveryService;
    }


    public Mono<OrderDetails> fetchOrderDetails(long orderId) {
        final Mono<Delivery> deliveryMono = deliveryService.fetchDelivery(orderId);
        final Mono<BillInfo> billInfoMono = accountingService.fetchBillInfoForOrder(orderId);

        return Mono.zip(deliveryMono, billInfoMono, (delivery, billInfo) -> new OrderDetails(orderId, delivery, billInfo));
    }

}
