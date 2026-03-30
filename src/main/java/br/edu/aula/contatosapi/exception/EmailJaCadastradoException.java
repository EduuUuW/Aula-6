package br.edu.aula.contatosapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando se tenta cadastrar ou atualizar um contato
 * com um e-mail que já pertence a outro registro.
 *
 * Retorna HTTP 409 (Conflict).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("Já existe um contato cadastrado com o e-mail: " + email);
    }
}
