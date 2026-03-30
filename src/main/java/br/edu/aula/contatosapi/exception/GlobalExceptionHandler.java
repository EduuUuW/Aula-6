package br.edu.aula.contatosapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Tratamento centralizado de exceções para toda a API.
 *
 * @RestControllerAdvice intercepta exceções lançadas em qualquer
 * @RestController e permite retornar respostas HTTP padronizadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ----------------------------------------------------------------
    //  404 - Contato não encontrado
    // ----------------------------------------------------------------
    @ExceptionHandler(ContatoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleContatoNaoEncontrado(
            ContatoNaoEncontradoException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(montarErro(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // ----------------------------------------------------------------
    //  409 - E-mail duplicado
    // ----------------------------------------------------------------
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailDuplicado(
            EmailJaCadastradoException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(montarErro(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // ----------------------------------------------------------------
    //  400 - Erros de validação (Bean Validation)
    //  Disparado quando @Valid falha no @RequestBody
    // ----------------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(
            MethodArgumentNotValidException ex) {

        // Coleta todos os erros de campo em um Map { campo: mensagem }
        Map<String, String> errosCampos = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errosCampos.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> corpo = montarErro(HttpStatus.BAD_REQUEST,
                "Dados inválidos. Verifique os campos informados.");
        corpo.put("campos", errosCampos);

        return ResponseEntity.badRequest().body(corpo);
    }

    // ----------------------------------------------------------------
    //  500 - Qualquer outra exceção não tratada
    // ----------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenerico(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(montarErro(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno. Tente novamente mais tarde."));
    }

    // ----------------------------------------------------------------
    //  Utilitário — monta o corpo padrão de erro
    // ----------------------------------------------------------------
    private Map<String, Object> montarErro(HttpStatus status, String mensagem) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("timestamp", LocalDateTime.now().toString());
        corpo.put("status",    status.value());
        corpo.put("erro",      status.getReasonPhrase());
        corpo.put("mensagem",  mensagem);
        return corpo;
    }
}
