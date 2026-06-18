package br.edu.ifg.trilhadeaprendizadoapims.user.repository;

import br.edu.ifg.trilhadeaprendizadoapims.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
