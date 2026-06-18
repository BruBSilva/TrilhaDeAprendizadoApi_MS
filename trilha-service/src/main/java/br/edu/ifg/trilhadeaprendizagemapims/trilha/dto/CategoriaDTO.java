package br.edu.ifg.trilhadeaprendizagemapims.trilha.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaDTO {
    private Long id;
    @NotNull
    private String nome;
    @NotNull
    private String descricao;
}
