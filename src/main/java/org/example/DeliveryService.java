package org.example;

import org.example.data.Delivery;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class DeliveryService {

    private static final String DELIVERY_SERVICE_URL = "http://localhost:3000/delivery/";
    private final WebClient webClient;

    public DeliveryService() {
        ConnectionProvider connProvider = ConnectionProvider
                .builder("webclient-conn-pool")
                .maxConnections(2000)
                .maxIdleTime(Duration.ofMillis(10_000))
                .maxLifeTime(Duration.ofMillis(10_000))
                .pendingAcquireMaxCount(1_000)
                .pendingAcquireTimeout(Duration.ofMillis(1_000))
                .build();
        HttpClient httpClient = HttpClient.create(connProvider);
        this.webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public Mono<Delivery> fetchDelivery(long id, String token) {
        return this.webClient
                .get()
                .uri(DELIVERY_SERVICE_URL + id)
                .header("Authorization", "Authorization: Bearer " + token)
                .retrieve()
                .bodyToMono(Delivery.class)
                .retryWhen(Retry.fixedDelay(2, Duration.ofMillis(10)));
    }
}
