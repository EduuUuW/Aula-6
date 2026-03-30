package br.edu.aula.contatosapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação Spring Boot.
 *
 * Após iniciar, acesse:
 *   - Swagger UI  → http://localhost:8080/swagger-ui.html
 *   - H2 Console  → http://localhost:8080/h2-console
 *   - API Docs    → http://localhost:8080/v3/api-docs
 */
@SpringBootApplication
public class ContatosApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContatosApiApplication.class, args);
    }
}
