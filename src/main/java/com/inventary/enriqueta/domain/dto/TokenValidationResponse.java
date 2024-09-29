package com.inventary.enriqueta.domain.dto;

import lombok.Data;

@Data
public class TokenValidationResponse {
    private boolean valid;
    private String role;
}
