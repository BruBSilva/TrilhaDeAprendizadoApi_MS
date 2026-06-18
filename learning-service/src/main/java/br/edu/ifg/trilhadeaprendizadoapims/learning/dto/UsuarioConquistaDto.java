package br.edu.ifg.trilhadeaprendizadoapims.learning.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioConquistaDto {
    private Long id;
    @NotNull
    private Long usuarioId;
    @NotNull
    private Long conquistaId;

    //Conquista detalhada
    @NotNull
    private String conquistaNome;
    @NotNull
    private String conquistaDescricao;
    @NotNull
    private String conquistaTipo;

    private String conquistaModulo;

    private String conquistaTrilha;

    @NotNull
    private int conquistaXpGanho;

    @NotNull
    private LocalDateTime dataConquista;
}
