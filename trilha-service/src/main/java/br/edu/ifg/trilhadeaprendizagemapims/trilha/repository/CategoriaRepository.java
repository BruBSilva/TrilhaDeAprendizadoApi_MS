package br.edu.ifg.trilhadeaprendizagemapims.trilha.repository;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.ECategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<ECategoria, Long> {
}
