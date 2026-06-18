package br.edu.ifg.trilhadeaprendizagemapims.trilha.repository;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.EModulo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuloRepository extends JpaRepository<EModulo, Long> {
}
