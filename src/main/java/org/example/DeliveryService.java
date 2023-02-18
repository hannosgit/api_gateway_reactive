package org.example;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DeliveryService {
    private final WebClient webClient;

    public DeliveryService() {
        this.webClient = WebClient.create();
    }

    public Mono<Delivery> fetchDelivery(long id) {
        return this.webClient
                .get()
                .uri("http://localhost:3000/")
                .retrieve()
                .bodyToMono(Delivery.class);
    }
}
