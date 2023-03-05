package org.example;


import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Service
public class AuthService {

    private static final String AUTH_SERVICE_URL = "http://localhost:3000/auth/authenticate";
    private final WebClient webClient;

    public AuthService() {
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

    public Mono<String> fetchToken(String username, String password) {
        return this.webClient
                .post()
                .uri(AUTH_SERVICE_URL)
                .bodyValue(new AuthenticateRequest(username, password))
                .retrieve()
                .bodyToMono(AuthenticateResponse.class)
                .map(authenticateResponse -> authenticateResponse.token);
    }

    private record AuthenticateRequest(String username, String password) {

    }

    private record AuthenticateResponse(String token) {

    }

}
