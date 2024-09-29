package com.inventary.enriqueta.application.webClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.inventary.enriqueta.domain.dto.TokenValidationResponse;
import com.google.common.net.HttpHeaders;

@Component
public class AuthServiceClient {

    private final WebClient webClient;

    @Autowired
    public AuthServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://vg-ms-user-production-49bd.up.railway.app/firebase-users").build();
    }

    public Mono<TokenValidationResponse> validateToken(String token) {
        return webClient.get()
                .uri("/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(TokenValidationResponse.class);
    }
}