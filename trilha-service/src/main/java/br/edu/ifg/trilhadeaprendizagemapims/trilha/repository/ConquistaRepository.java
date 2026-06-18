package br.edu.ifg.trilhadeaprendizagemapims.trilha.repository;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.EConquista;
import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.enums.TipoConquista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConquistaRepository extends JpaRepository<EConquista, Long> {
    Optional<EConquista> findByTipo(TipoConquista tipo);
}
