package br.edu.ifg.trilhadeaprendizadoapims.learning.repository;

import br.edu.ifg.trilhadeaprendizadoapims.learning.model.EUsuarioConquista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioConquistaRepository extends JpaRepository<EUsuarioConquista, Long> {

    Page<EUsuarioConquista> findByUsuarioId(Long usuario_id, Pageable paginacao);
}
