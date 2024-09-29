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
@RequestMapping("/inventory")
public class InventoryUserController {

    private final ManagerServiceImpl managerService;

    @Autowired
    public InventoryUserController(ManagerServiceImpl managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/actives")
    public Mono<ResponseEntity<Flux<Manager>>> getAllActiveInventory(@RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("Inventario"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    return Mono.just(ResponseEntity.ok(
                            managerService.findByRole("Inventario").filter(manager -> manager.getStatus().equalsIgnoreCase("ACTIVO"))
                    ));
                });
    }

    @GetMapping("/inactives")
    public Mono<ResponseEntity<Flux<Manager>>> getAllInactiveInventory(@RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("Inventario"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    return Mono.just(ResponseEntity.ok(
                            managerService.findByRole("Inventario").filter(manager -> manager.getStatus().equalsIgnoreCase("INACTIVO"))
                    ));
                });
    }

    @GetMapping("/document/{documentNumber}")
    public Mono<ResponseEntity<Manager>> getInventoryByDocumentNumber(@PathVariable String documentNumber, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("Inventario"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    return managerService.findByDocumentNumber(documentNumber)
                            .filter(manager -> manager.getRole().equalsIgnoreCase("Inventario"))
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                });
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<Manager>> getInventoryByEmail(@PathVariable String email, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("Inventario"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    return managerService.findByEmail(email)
                            .filter(manager -> manager.getRole().equalsIgnoreCase("Inventario"))
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Manager>> getInventoryById(@PathVariable String id, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("Inventario"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    return managerService.findById(id)
                            .filter(manager -> manager.getRole().equalsIgnoreCase("Inventario"))
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                });
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<Manager>> createInventory(@RequestBody Manager manager, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    if (!manager.getRole().equalsIgnoreCase("Inventario")) {
                        return Mono.just(ResponseEntity.badRequest().body(null));
                    }
                    return managerService.createManager(manager).map(ResponseEntity::ok);
                });
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Manager>> updateInventory(@PathVariable String id, @RequestBody Manager managerDetails, @RequestHeader("Authorization") String token) {
        return managerService.validateTokenAndRoles(token.substring(7), List.of("ADMIN"))
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                    if (!managerDetails.getRole().equalsIgnoreCase("Inventario")) {
                        return Mono.just(ResponseEntity.badRequest().body(null));
                    }
                    return managerService.updateManager(id, managerDetails)
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                });
    }

    @PatchMapping("/updatePassword/{id}")
    public Mono<ResponseEntity<Manager>> updateInventoryPassword(@PathVariable String id, @RequestBody String newPassword, @RequestHeader("Authorization") String token) {
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

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Object>> deleteInventory(@PathVariable String id, @RequestHeader("Authorization") String token) {
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
