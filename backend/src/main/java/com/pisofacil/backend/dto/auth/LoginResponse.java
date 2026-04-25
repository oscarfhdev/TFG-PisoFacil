package com.pisofacil.backend.dto.auth;

import com.pisofacil.backend.dto.UsuarioDTO;

public record LoginResponse(String email, String token, UsuarioDTO usuario) {}
