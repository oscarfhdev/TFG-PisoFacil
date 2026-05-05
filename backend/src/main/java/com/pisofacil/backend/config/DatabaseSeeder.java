package com.pisofacil.backend.config;

import com.pisofacil.backend.model.Usuario;
import com.pisofacil.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@pisofacil.com");
            admin.setPassword(passwordEncoder.encode("admin1234"));
            admin.setEsAdmin(true);
            admin.setFechaRegistro(LocalDateTime.now());
            usuarioRepository.save(admin);

            Usuario user = new Usuario();
            user.setNombre("Usuario Normal");
            user.setEmail("user@pisofacil.com");
            user.setPassword(passwordEncoder.encode("user1234"));
            user.setEsAdmin(false);
            user.setFechaRegistro(LocalDateTime.now());
            usuarioRepository.save(user);

            System.out.println("Data Loader: Usuarios por defecto creados.");
        } else {
            System.out.println("Data Loader: La base de datos ya contiene usuarios.");
        }
    }
}
