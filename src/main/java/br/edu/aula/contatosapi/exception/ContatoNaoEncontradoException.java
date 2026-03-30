package br.edu.aula.contatosapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um Contato não é encontrado no banco de dados.
 *
 * A anotação @ResponseStatus faz o Spring retornar automaticamente
 * HTTP 404 (Not Found) caso esta exceção não seja tratada por um
 * @ExceptionHandler ou @ControllerAdvice.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContatoNaoEncontradoException extends RuntimeException {

    public ContatoNaoEncontradoException(Long id) {
        super("Contato não encontrado com ID: " + id);
    }

    public ContatoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
