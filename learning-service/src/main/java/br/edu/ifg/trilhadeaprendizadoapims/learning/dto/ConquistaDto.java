package br.edu.ifg.trilhadeaprendizadoapims.learning.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConquistaDto {

    @NotNull
    private Long id;
    @NotNull
    private String nome;
    @NotNull
    private String descricao;
    @NotNull
    private String tipo;

    private String modulo_nome;

    private String trilha_nome;
    @NotNull
    private int xpGanho;
}
