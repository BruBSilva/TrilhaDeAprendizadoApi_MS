package br.edu.ifg.trilhadeaprendizagemapims.trilha.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuloDTO {
    private Long id;
    @NotNull
    private String titulo;
    @NotNull
    private Long trilhaId;
    @NotNull
    private int ordem;
    @NotNull
    private String conteudo;
    @NotNull
    private ConquistaDTO conquista;
}
