package br.edu.ifg.trilhadeaprendizadoapims.learning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_conquistas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EUsuarioConquista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long conquistaId;

    @NotBlank
    private String conquistaNome;

    @NotBlank
    private String conquistaDescricao;

    @NotBlank
    private String conquistaTipo;

    private String conquistaModulo;

    private String conquistaTrilha;

    @NotNull
    private int conquistaXpGanho;

    @NotNull
    private LocalDateTime dataConquista;
}
