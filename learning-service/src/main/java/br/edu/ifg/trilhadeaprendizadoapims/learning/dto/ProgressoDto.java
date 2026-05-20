package br.edu.ifg.trilhadeaprendizadoapims.learning.dto;

import br.edu.ifg.trilhadeaprendizadoapims.learning.model.StatusProgresso;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProgressoDto {
    @NotNull
    private Long id;
    @NotNull
    private Long usuarioId;
    @NotNull
    private Long trilhaId;
    @NotNull
    private List<Long> trilha_modulos;
    @NotNull
    private LocalDateTime dataInicio;

    private LocalDateTime dataConclusao;
    @NotNull
    private int xpGanho;
    @NotNull
    private Double percentual;
    @NotNull
    private Long moduloAtual_id;
    @NotNull
    private StatusProgresso statusProgresso;

}
