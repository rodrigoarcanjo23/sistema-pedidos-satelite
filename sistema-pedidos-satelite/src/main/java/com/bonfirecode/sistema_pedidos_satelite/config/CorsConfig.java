package com.bonfirecode.sistema_pedidos_satelite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a regra para todas as nossas rotas (api/pedidos...)
                .allowedOrigins("*") // Permite acesso de qualquer lugar (na hora de colocar em produção, trocaríamos pelo domínio real)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Libera os verbos que usamos
                .allowedHeaders("*"); // Permite que o frontend mande qualquer cabeçalho
    }
}