package br.edu.aula.contatosapi.service;

import br.edu.aula.contatosapi.dto.ContatoDTO;
import br.edu.aula.contatosapi.exception.ContatoNaoEncontradoException;
import br.edu.aula.contatosapi.exception.EmailJaCadastradoException;
import br.edu.aula.contatosapi.model.Contato;
import br.edu.aula.contatosapi.repository.ContatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContatoService - Testes Unitários")
class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private ContatoService contatoService;

    private Contato contatoExistente;
    private ContatoDTO dtoEntrada;

    @BeforeEach
    void setUp() {
        contatoExistente = Contato.builder()
                .id(1L)
                .nome("Ana Souza")
                .email("ana@email.com")
                .telefone("(11) 91234-5678")
                .dataCadastro(LocalDateTime.now())
                .build();

        dtoEntrada = ContatoDTO.builder()
                .nome("Ana Souza")
                .email("ana@email.com")
                .telefone("(11) 91234-5678")
                .build();
    }

    @Test
    @DisplayName("listarTodos: deve retornar lista com todos os contatos")
    void listarTodos_deveRetornarLista() {
        when(contatoRepository.findAll()).thenReturn(List.of(contatoExistente));

        List<ContatoDTO> resultado = contatoService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("ana@email.com");
        verify(contatoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId: deve retornar contato quando ID existe")
    void buscarPorId_quandoIdExiste_deveRetornarContato() {
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contatoExistente));

        ContatoDTO resultado = contatoService.buscarPorId(1L);

        assertThat(resultado.getNome()).isEqualTo("Ana Souza");
        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("buscarPorId: deve lançar exceção quando ID não existe")
    void buscarPorId_quandoIdNaoExiste_deveLancarExcecao() {
        when(contatoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contatoService.buscarPorId(99L))
                .isInstanceOf(ContatoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("criar: deve salvar e retornar o novo contato")
    void criar_comDadosValidos_deveSalvarContato() {
        when(contatoRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(contatoRepository.save(any(Contato.class))).thenReturn(contatoExistente);

        ContatoDTO resultado = contatoService.criar(dtoEntrada);

        assertThat(resultado.getEmail()).isEqualTo("ana@email.com");
        verify(contatoRepository).save(any(Contato.class));
    }

    @Test
    @DisplayName("criar: deve lançar exceção se e-mail já cadastrado")
    void criar_comEmailDuplicado_deveLancarExcecao() {
        when(contatoRepository.findByEmail("ana@email.com"))
                .thenReturn(Optional.of(contatoExistente));

        assertThatThrownBy(() -> contatoService.criar(dtoEntrada))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining("ana@email.com");

        verify(contatoRepository, never()).save(any());
    }

    @Test
    @DisplayName("deletar: deve lançar exceção quando contato não existe")
    void deletar_quandoIdNaoExiste_deveLancarExcecao() {
        when(contatoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> contatoService.deletar(99L))
                .isInstanceOf(ContatoNaoEncontradoException.class);

        verify(contatoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("pesquisarPorNome: deve retornar contatos que contêm o termo")
    void pesquisarPorNome_deveRetornarContatos() {
        when(contatoRepository.findByNomeContainingIgnoreCase("ana"))
                .thenReturn(List.of(contatoExistente));

        List<ContatoDTO> resultado = contatoService.pesquisarPorNome("ana");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).containsIgnoringCase("ana");
    }
}
