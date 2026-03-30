package br.edu.aula.contatosapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "contatos",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_contato_email", columnNames = "email")
       })
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    public Contato() {}

    public Contato(Long id, String nome, String email, String telefone, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
    }

    @PrePersist
    public void preenchDataCadastro() {
        this.dataCadastro = LocalDateTime.now();
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

        public Contato build() {
            return new Contato(id, nome, email, telefone, dataCadastro);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contato)) return false;
        return Objects.equals(id, ((Contato) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Contato{id=" + id + ", nome='" + nome + "', email='" + email + "'}";
    }
}
