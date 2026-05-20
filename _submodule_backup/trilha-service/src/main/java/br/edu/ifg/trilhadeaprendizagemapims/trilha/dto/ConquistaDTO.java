package br.edu.ifg.trilhadeaprendizagemapims.trilha.dto;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.enums.TipoConquista;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConquistaDTO {
    private Long id;
    @NotNull
    private String nome;
    @NotNull
    private String descricao;
    @NotNull
    private TipoConquista tipo;

    private Long moduloId;

    private Long trilhaId;
    @NotNull
    private int xpGanho;
}
