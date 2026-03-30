package br.edu.aula.contatosapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do Swagger / OpenAPI 3.
 *
 * Esta classe personaliza os metadados exibidos na página do Swagger UI:
 * título, descrição, versão, contato, licença e servidor base.
 *
 * Acesse: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Contatos Pessoais")
                        .description("""
                                Sistema de Cadastro de Contatos desenvolvido com **Spring Boot 3** e **Spring Data JPA**.""")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Prof. Aula de Spring")
                                .email("professor@faculdade.edu.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento Local")
                ));
    }
}
