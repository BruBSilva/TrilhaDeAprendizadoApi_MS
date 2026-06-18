package br.edu.ifg.trilhadeaprendizadoapims.learning.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressoCreateDto {
    private Long id;
    @NotNull
    private Long usuarioId;
    @NotNull
    private Long trilhaId;
}
