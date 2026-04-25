package com.pisofacil.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para cambiar la contraseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambioPasswordRequest {

    private String passwordActual;
    private String passwordNueva;
}
