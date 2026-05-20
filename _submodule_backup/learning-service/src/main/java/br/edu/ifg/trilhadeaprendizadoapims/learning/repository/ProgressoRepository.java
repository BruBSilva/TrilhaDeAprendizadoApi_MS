package br.edu.ifg.trilhadeaprendizadoapims.learning.repository;


import br.edu.ifg.trilhadeaprendizadoapims.learning.model.EProgresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressoRepository extends JpaRepository<EProgresso, Long> {

    Optional<EProgresso> findByUsuarioIdAndTrilhaId(Long usuarioId, Long trilhaId);
    
    List<EProgresso> findAllByUsuarioIdAndTrilhaIdOrderByDataInicioDesc(Long usuarioId, Long trilhaId);
}
