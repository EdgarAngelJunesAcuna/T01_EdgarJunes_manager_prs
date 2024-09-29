package com.inventary.enriqueta.domain.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Document(collection = "manager") // Indicamos que esta clase será almacenada en la colección "manager"
public class Manager {

    @Id
    private String id; // Identificador único del manager
    private String uid; // Identificador único en Firebase Authentication
    private String firstName; // Primer nombre del manager
    private String lastName; // Apellido del manager
    private String documentType; // Tipo de documento (DNI, Pasaporte, etc.)
    private String documentNumber; // Número del documento
    private String gender; // Género del manager (M/F)
    private String address; // Dirección del manager
    private String birthPlace; // Lugar de nacimiento
    private String email; // Correo electrónico
    private String role; // Rol asignado (Ej: ADMIN, USER)
    private String password; // Contraseña, guardada temporalmente
    private String status; // Estado (Ej: ACTIVO, INACTIVO)
    private LocalDateTime createdAt; // Fecha y hora de creación del registro
    private LocalDateTime updatedAt; // Fecha y hora de la última actualización

    // Métodos adicionales como validaciones pueden agregarse si es necesario
}
