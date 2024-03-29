package org.example;

import org.example.common.Delivery;
import org.example.common.ServiceAddressConfigProperty;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Service
public class DeliveryService {

    private final WebClient webClient;

    private final String uri;

    public DeliveryService(ServiceAddressConfigProperty serviceAddressConfigProperty) {
        ConnectionProvider connProvider = ConnectionProvider
                .builder("webclient-conn-pool")
                .maxConnections(10_000)
                .maxIdleTime(Duration.ofMillis(10_000))
                .maxLifeTime(Duration.ofMillis(10_000))
                .pendingAcquireMaxCount(10_000)
                .pendingAcquireTimeout(Duration.ofMillis(10_000))
                .build();
        HttpClient httpClient = HttpClient.create(connProvider);
        this.webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        this.uri = "http://" + serviceAddressConfigProperty.delivery() + "/delivery/";
    }

    public Mono<Delivery> fetchDelivery(long id, String token) {
        return this.webClient
                .get()
                .uri(this.uri + id)
                .header("Authorization", "Authorization: Bearer " + token)
                .retrieve()
                .bodyToMono(Delivery.class);
    }
}
