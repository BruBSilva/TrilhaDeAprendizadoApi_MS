package br.edu.ifg.trilhadeaprendizadoapims.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlunoDto extends UsuarioDto{

    @NotNull
    private int xpTotal;
    @NotNull
    private int nivel;
    @NotNull
    private LocalDateTime dataCadastro;

}
