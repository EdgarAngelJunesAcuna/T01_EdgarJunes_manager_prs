package com.inventary.enriqueta.domain.Repository;

import com.inventary.enriqueta.domain.Model.Manager;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ManagerRepository extends ReactiveMongoRepository<Manager, String> {
    Flux<Manager> findByStatus(String status);
    Mono<Manager> findByDocumentNumber(String documentNumber);
    Mono<Manager> findByEmail(String email);
    Flux<Manager> findByRoleIgnoreCase(String role);
}
