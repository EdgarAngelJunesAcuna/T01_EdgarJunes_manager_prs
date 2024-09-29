package com.inventary.enriqueta.application.Service;

import com.inventary.enriqueta.domain.Model.Manager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ManagerService {
    Flux<Manager> listAllActive();
    Flux<Manager> listAllInactive();
    Mono<Manager> findByDocumentNumber(String documentNumber);
    Mono<Manager> findByEmail(String email);
    Mono<Manager> findById(String id);
    Mono<Manager> createManager(Manager manager);  // Crear un nuevo manager (Secretario o Inventario)
    Mono<Manager> deleteManager(String id);        // Cambiar el estado a inactivo
    Mono<Manager> reactivateManager(String id);    // Reactivar un manager cambiando el estado a activo
    Mono<Manager> updateManager(String id, Manager managerDetails);  // Actualizar la información del manager
    Mono<Manager> updatePassword(String id, String newPassword);     // Actualizar la contraseña del manager
    Flux<Manager> findByRole(String role);  // Buscar managers por rol
}
