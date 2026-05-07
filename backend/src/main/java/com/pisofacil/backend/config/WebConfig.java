package com.pisofacil.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Ruta absoluta al directorio de uploads, configurada en application.properties
    @Value("${app.uploads.dir}")
    private String uploadsDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Normalizar: asegurarse de que termina con separador de directorio
        String location = uploadsDir.endsWith("/") ? uploadsDir : uploadsDir + "/";

        registry.addResourceHandler("/uploads/fotos/**")
                .addResourceLocations("file:" + location);
    }
}
