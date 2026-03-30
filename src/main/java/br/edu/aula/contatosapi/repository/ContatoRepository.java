package br.edu.aula.contatosapi.repository;

import br.edu.aula.contatosapi.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

   
    List<Contato> findByNomeContainingIgnoreCase(String nome);

   
    Optional<Contato> findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT c FROM Contato c WHERE c.telefone IS NULL OR c.telefone = ''")
    List<Contato> findContatosSemTelefone();


    @Query(value = "SELECT * FROM contatos ORDER BY data_cadastro DESC LIMIT 5",
           nativeQuery = true)
    List<Contato> findTop5MaisRecentes();

    
    @Query("SELECT c FROM Contato c WHERE LOWER(c.email) = LOWER(:email)")
    Optional<Contato> buscarPorEmailIgnoreCase(@Param("email") String email);
}
