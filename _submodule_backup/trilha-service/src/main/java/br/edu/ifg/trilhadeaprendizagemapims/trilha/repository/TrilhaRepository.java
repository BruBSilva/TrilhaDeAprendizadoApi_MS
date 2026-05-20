package br.edu.ifg.trilhadeaprendizagemapims.trilha.repository;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ETrilha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrilhaRepository extends JpaRepository<ETrilha, Long> {
}
