package br.edu.aula.contatosapi.service;

import br.edu.aula.contatosapi.dto.ContatoDTO;
import br.edu.aula.contatosapi.exception.ContatoNaoEncontradoException;
import br.edu.aula.contatosapi.exception.EmailJaCadastradoException;
import br.edu.aula.contatosapi.model.Contato;
import br.edu.aula.contatosapi.repository.ContatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContatoService {

    private static final Logger log = LoggerFactory.getLogger(ContatoService.class);

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    @Transactional(readOnly = true)
    public List<ContatoDTO> listarTodos() {
        log.info("Listando todos os contatos");
        return contatoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContatoDTO buscarPorId(Long id) {
        log.info("Buscando contato com ID: {}", id);
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ContatoNaoEncontradoException(id));
        return toDTO(contato);
    }

    @Transactional
    public ContatoDTO criar(ContatoDTO dto) {
        log.info("Criando contato com e-mail: {}", dto.getEmail());
        contatoRepository.findByEmail(dto.getEmail())
                .ifPresent(c -> { throw new EmailJaCadastradoException(dto.getEmail()); });
        Contato contato = toEntity(dto);
        Contato salvo = contatoRepository.save(contato);
        log.info("Contato criado com ID: {}", salvo.getId());
        return toDTO(salvo);
    }

    @Transactional
    public ContatoDTO atualizar(Long id, ContatoDTO dto) {
        log.info("Atualizando contato com ID: {}", id);
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ContatoNaoEncontradoException(id));
        if (contatoRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }
        contato.setNome(dto.getNome());
        contato.setEmail(dto.getEmail());
        contato.setTelefone(dto.getTelefone());
        Contato atualizado = contatoRepository.save(contato);
        log.info("Contato ID {} atualizado com sucesso", id);
        return toDTO(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando contato com ID: {}", id);
        if (!contatoRepository.existsById(id)) {
            throw new ContatoNaoEncontradoException(id);
        }
        contatoRepository.deleteById(id);
        log.info("Contato ID {} deletado com sucesso", id);
    }

    @Transactional(readOnly = true)
    public List<ContatoDTO> pesquisarPorNome(String nome) {
        log.info("Pesquisando contatos com nome contendo: '{}'", nome);
        return contatoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ContatoDTO toDTO(Contato contato) {
        return ContatoDTO.builder()
                .id(contato.getId())
                .nome(contato.getNome())
                .email(contato.getEmail())
                .telefone(contato.getTelefone())
                .dataCadastro(contato.getDataCadastro())
                .build();
    }

    private Contato toEntity(ContatoDTO dto) {
        return Contato.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .build();
    }
}
