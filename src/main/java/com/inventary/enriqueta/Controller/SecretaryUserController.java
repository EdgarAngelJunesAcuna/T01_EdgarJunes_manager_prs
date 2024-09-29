package com.inventary.enriqueta.Controller;

import com.inventary.enriqueta.domain.Model.Manager;
import com.inventary.enriqueta.application.Service.impl.ManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/secretary")
public class SecretaryUserController {

    private final ManagerServiceImpl managerService;

    @Autowired
    public SecretaryUserController(ManagerServiceImpl managerService) {
        this.managerService = managerService;
    }

    // Obtener todos los secretarios activos con verificación de JWT
    @GetMapping("/actives")
    public Mono<ResponseEntity<Flux<Manager>>> getAllActiveSecretaries(@RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN", "SECRETARIO"))
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok(
                                managerService.findByRole("Secretario").filter(manager -> manager.getStatus().equalsIgnoreCase("ACTIVO"))
                        ));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build()); // Acceso denegado
                    }
                });
    }

    // Obtener todos los secretarios inactivos con verificación de JWT
    @GetMapping("/inactives")
    public Mono<ResponseEntity<Flux<Manager>>> getAllInactiveSecretaries(@RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN", "SECRETARIO"))
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok(
                                managerService.findByRole("Secretario").filter(manager -> manager.getStatus().equalsIgnoreCase("INACTIVO"))
                        ));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build()); // Acceso denegado
                    }
                });
    }

    // Obtener secretario por número de documento con verificación de JWT
    @GetMapping("/document/{documentNumber}")
    public Mono<ResponseEntity<Manager>> getSecretaryByDocumentNumber(@PathVariable String documentNumber, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN", "SECRETARIO"))
                .flatMap(isValid -> {
                    if (isValid) {
                        return managerService.findByDocumentNumber(documentNumber)
                                .filter(manager -> manager.getRole().equalsIgnoreCase("Secretario"))
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
                    } else {
                        return Mono.just(ResponseEntity.status(403).build()); // Acceso denegado
                    }
                });
    }

    // Obtener secretario por email con verificación de JWT
    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<Manager>> getSecretaryByEmail(@PathVariable String email, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN", "SECRETARIO"))
                .flatMap(isValid -> {
                    if (isValid) {
                        return managerService.findByEmail(email)
                                .filter(manager -> manager.getRole().equalsIgnoreCase("Secretario"))
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
                    } else {
                        return Mono.just(ResponseEntity.status(403).build()); // Acceso denegado
                    }
                });
    }

    // Obtener secretario por ID con verificación de JWT
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Manager>> getSecretaryById(@PathVariable String id, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN", "SECRETARIO"))
                .flatMap(isValid -> {
                    if (isValid) {
                        return managerService.findById(id)
                                .filter(manager -> manager.getRole().equalsIgnoreCase("Secretario"))
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
                    } else {
                        return Mono.just(ResponseEntity.status(403).build()); // Acceso denegado
                    }
                });
    }

    // Crear nuevo secretario con verificación de JWT
    @PostMapping("/create")
    public Mono<ResponseEntity<Manager>> createSecretary(@RequestBody Manager manager, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    if (!manager.getRole().equalsIgnoreCase("Secretario")) {
                        return Mono.just(ResponseEntity.badRequest().body(null));
                    }
                    return managerService.createManager(manager).map(ResponseEntity::ok);
                });
    }

    // Actualizar secretario con verificación de JWT
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Manager>> updateSecretary(@PathVariable String id, @RequestBody Manager managerDetails, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    if (!managerDetails.getRole().equalsIgnoreCase("Secretario")) {
                        return Mono.just(ResponseEntity.badRequest().body(null));
                    }
                    return managerService.updateManager(id, managerDetails)
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                });
    }

    // Actualizar contraseña de secretario con verificación de JWT
    @PatchMapping("/updatePassword/{id}")
    public Mono<ResponseEntity<Manager>> updateSecretaryPassword(@PathVariable String id, @RequestBody String newPassword, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    return managerService.updatePassword(id, newPassword)
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                });
    }

    // Eliminar secretario con verificación de JWT
    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Object>> deleteSecretary(@PathVariable String id, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    return managerService.deleteManager(id)
                            .map(deleted -> ResponseEntity.noContent().build())
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                });
    }
}
