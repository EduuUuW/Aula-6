package br.edu.aula.contatosapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "Dados de um contato pessoal")
public class ContatoDTO {

    @Schema(description = "Identificador único do contato (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    @Schema(description = "Nome completo do contato", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    @Schema(description = "Endereço de e-mail único do contato", example = "maria@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Número de telefone (opcional)", example = "(11) 91234-5678")
    private String telefone;

    @Schema(description = "Data e hora do cadastro (preenchida automaticamente)", example = "2024-03-20T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataCadastro;

    public ContatoDTO() {}

    public ContatoDTO(Long id, String nome, String email, String telefone, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String nome;
        private String email;
        private String telefone;
        private LocalDateTime dataCadastro;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nome(String nome) { this.nome = nome; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder telefone(String telefone) { this.telefone = telefone; return this; }
        public Builder dataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; return this; }

        public ContatoDTO build() {
            return new ContatoDTO(id, nome, email, telefone, dataCadastro);
        }
    }
}
