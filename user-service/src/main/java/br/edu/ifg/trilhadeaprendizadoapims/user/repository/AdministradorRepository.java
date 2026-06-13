package br.edu.ifg.trilhadeaprendizadoapims.user.repository;

import br.edu.ifg.trilhadeaprendizadoapims.user.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
     Optional<Administrador> findByEmail(String email);
}
