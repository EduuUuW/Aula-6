package br.edu.aula.contatosapi.controller;

import br.edu.aula.contatosapi.dto.ContatoDTO;
import br.edu.aula.contatosapi.service.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contatos")
@Tag(name = "Contatos", description = "Operações de gerenciamento de contatos pessoais")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os contatos", description = "Retorna uma lista com todos os contatos cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<ContatoDTO>> listarTodos() {
        return ResponseEntity.ok(contatoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar contato por ID", description = "Retorna um contato específico a partir do seu identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contato encontrado"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                     content = @Content(schema = @Schema(implementation = Object.class)))
    })
    public ResponseEntity<ContatoDTO> buscarPorId(
            @Parameter(description = "ID do contato", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(contatoService.buscarPorId(id));
    }

    @GetMapping("/pesquisa")
    @Operation(summary = "Pesquisar contatos por nome", description = "Busca contatos cujo nome contenha o termo informado (sem distinção entre maiúsculas e minúsculas).")
    @ApiResponse(responseCode = "200", description = "Resultado da pesquisa retornado")
    public ResponseEntity<List<ContatoDTO>> pesquisarPorNome(
            @Parameter(description = "Trecho do nome a pesquisar", example = "ana")
            @RequestParam String nome) {
        return ResponseEntity.ok(contatoService.pesquisarPorNome(nome));
    }

    @PostMapping
    @Operation(summary = "Criar novo contato", description = "Cadastra um novo contato. O ID e a data de cadastro são gerados automaticamente. O e-mail deve ser único.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Contato criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                     content = @Content(schema = @Schema(implementation = Object.class))),
        @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                     content = @Content(schema = @Schema(implementation = Object.class)))
    })
    public ResponseEntity<ContatoDTO> criar(@Valid @RequestBody ContatoDTO dto) {
        ContatoDTO criado = contatoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar contato", description = "Atualiza nome, e-mail e telefone de um contato existente. A data de cadastro original é preservada.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
        @ApiResponse(responseCode = "409", description = "E-mail já em uso por outro contato")
    })
    public ResponseEntity<ContatoDTO> atualizar(
            @Parameter(description = "ID do contato a atualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ContatoDTO dto) {
        return ResponseEntity.ok(contatoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar contato", description = "Remove permanentemente um contato pelo seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Contato deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do contato a deletar", example = "1")
            @PathVariable Long id) {
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
