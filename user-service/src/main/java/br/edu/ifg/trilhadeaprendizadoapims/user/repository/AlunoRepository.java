package br.edu.ifg.trilhadeaprendizadoapims.user.repository;

import br.edu.ifg.trilhadeaprendizadoapims.user.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByEmail(String email);
}
