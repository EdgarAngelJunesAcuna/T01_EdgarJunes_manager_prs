package com.inventary.enriqueta.application.Service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.inventary.enriqueta.domain.Model.Manager;
import com.inventary.enriqueta.domain.Repository.ManagerRepository;
import com.inventary.enriqueta.application.Service.ManagerService;
import com.inventary.enriqueta.application.webClient.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inventary.enriqueta.application.Util.StatusConstants.ACTIVE;
import static com.inventary.enriqueta.application.Util.StatusConstants.INACTIVE;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final AuthServiceClient authServiceClient; // Cliente para validar tokens
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository, AuthServiceClient authServiceClient) {
        this.managerRepository = managerRepository;
        this.authServiceClient = authServiceClient;
    }

    // Método de bienvenida
    public Mono<String> getWelcomeMessage() {
        return Mono.just("Bienvenidos al microservicio de encargados e inventarios");
    }

    @Override
    public Flux<Manager> listAllActive() {
        return managerRepository.findByStatus(ACTIVE);
    }

    @Override
    public Flux<Manager> listAllInactive() {
        return managerRepository.findByStatus(INACTIVE);
    }

    @Override
    public Mono<Manager> findByDocumentNumber(String documentNumber) {
        return managerRepository.findByDocumentNumber(documentNumber);
    }

    @Override
    public Mono<Manager> findByEmail(String email) {
        return managerRepository.findByEmail(email);
    }

    @Override
    public Mono<Manager> findById(String id) {
        return managerRepository.findById(id);
    }

    @Override
    public Flux<Manager> findByRole(String role) {
        return managerRepository.findByRoleIgnoreCase(role);
    }

    @Override
    public Mono<Manager> createManager(Manager manager) {
        // Validar que el rol sea "Secretario" o "Inventario"
        if (!manager.getRole().equalsIgnoreCase("Secretario") && !manager.getRole().equalsIgnoreCase("Inventario")) {
            return Mono.error(new IllegalArgumentException("Rol no válido. Solo se permiten los roles Secretario o Inventario."));
        }

        Manager newManager = modelMapper.map(manager, Manager.class);
        newManager.setStatus(ACTIVE);
        newManager.setCreatedAt(LocalDateTime.now());
        newManager.setUpdatedAt(LocalDateTime.now());

        // Crear usuario en Firebase
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(manager.getEmail())
                .setPassword(manager.getDocumentNumber())  // Usar el documento como contraseña
                .setDisplayName(manager.getFirstName() + " " + manager.getLastName());

        return Mono.fromCallable(() -> FirebaseAuth.getInstance().createUser(request))
                .flatMap(userRecord -> {
                    // Asignar UID de Firebase al nuevo Manager
                    newManager.setUid(userRecord.getUid());
                    newManager.setPassword(manager.getDocumentNumber()); // Contraseña en base de datos

                    // Asignar los claims personalizados
                    try {
                        Map<String, Object> claims = new HashMap<>();
                        claims.put("role", manager.getRole());
                        FirebaseAuth.getInstance().setCustomUserClaims(userRecord.getUid(), claims);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error setting custom claims: " + e.getMessage()));
                    }

                    // Guardar en la base de datos
                    return managerRepository.save(newManager);
                })
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Error creating user in Firebase: " + e.getMessage()));
                });
    }

    @Override
    public Mono<Manager> deleteManager(String id) {
        return managerRepository.findById(id)
                .flatMap(existingManager -> {
                    // Cambiar el estado del manager a Inactivo
                    existingManager.setStatus(INACTIVE);
                    return managerRepository.save(existingManager);
                })
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Error al cambiar el estado del manager a Inactivo: " + e.getMessage()));
                });
    }

    @Override
    public Mono<Manager> reactivateManager(String id) {
        return managerRepository.findById(id)
                .flatMap(existingManager -> {
                    // Cambiar el estado del manager a Activo
                    existingManager.setStatus(ACTIVE);
                    return managerRepository.save(existingManager);
                })
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Error al cambiar el estado del manager a Activo: " + e.getMessage()));
                });
    }

    @Override
    public Mono<Manager> updateManager(String id, Manager managerDetails) {
        return managerRepository.findById(id)
                .flatMap(existingManager -> {
                    // Actualizar todos los campos
                    existingManager.setFirstName(managerDetails.getFirstName());
                    existingManager.setLastName(managerDetails.getLastName());
                    existingManager.setDocumentType(managerDetails.getDocumentType());
                    existingManager.setDocumentNumber(managerDetails.getDocumentNumber());
                    existingManager.setGender(managerDetails.getGender());
                    existingManager.setAddress(managerDetails.getAddress());
                    existingManager.setBirthPlace(managerDetails.getBirthPlace());
                    existingManager.setEmail(managerDetails.getEmail());
                    existingManager.setRole(managerDetails.getRole());
                    existingManager.setUpdatedAt(LocalDateTime.now());

                    // Actualizar en Firebase el displayName
                    return Mono.fromCallable(() -> FirebaseAuth.getInstance().updateUser(
                            new UserRecord.UpdateRequest(existingManager.getUid())
                                    .setDisplayName(managerDetails.getFirstName() + " " + managerDetails.getLastName())
                    )).flatMap(userRecord -> {
                        return managerRepository.save(existingManager);
                    });
                })
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Error updating manager: " + e.getMessage()));
                });
    }

    @Override
    public Mono<Manager> updatePassword(String id, String newPassword) {
        return managerRepository.findById(id)
                .flatMap(existingManager -> {
                    // Actualizar la contraseña en Firebase
                    return Mono.fromCallable(() -> FirebaseAuth.getInstance().updateUser(
                            new UserRecord.UpdateRequest(existingManager.getUid()).setPassword(newPassword)
                    )).flatMap(userRecord -> {
                        // Actualizar la contraseña en la base de datos
                        existingManager.setPassword(newPassword);
                        return managerRepository.save(existingManager);
                    });
                })
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Error updating password in Firebase: " + e.getMessage()));
                });
    }

    // Método para validar el token y los roles permitidos
    public Mono<Boolean> validateTokenAndRoles(String token, List<String> requiredRoles) {
        return authServiceClient.validateToken(token)
                .flatMap(validationResponse -> {
                    if (validationResponse.isValid() && requiredRoles.contains(validationResponse.getRole())) {
                        return Mono.just(true); // Token válido y rol coincide
                    }
                    return Mono.just(false); // Token no válido o rol no coincide
                });
    }
}
