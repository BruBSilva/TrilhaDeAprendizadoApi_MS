package br.edu.ifg.trilhadeaprendizadoapims.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aluno extends Usuario {

    @NotNull
    private int xpTotal;

    @NotNull
    private int nivel;

    @NotNull
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCadastro;

}
