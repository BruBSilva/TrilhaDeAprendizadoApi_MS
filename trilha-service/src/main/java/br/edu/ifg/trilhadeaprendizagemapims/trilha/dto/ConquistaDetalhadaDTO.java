package br.edu.ifg.trilhadeaprendizagemapims.trilha.dto;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.enums.TipoConquista;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConquistaDetalhadaDTO {
    @NotNull
    private Long id;
    @NotNull
    private String nome;
    @NotNull
    private String descricao;
    @NotNull
    private TipoConquista tipo;

    private String modulo_nome;

    private String trilha_nome;
    @NotNull
    private int xpGanho;
}
