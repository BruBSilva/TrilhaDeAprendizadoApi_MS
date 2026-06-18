package br.edu.ifg.trilhadeaprendizadoapims.user.dto;

import br.edu.ifg.trilhadeaprendizadoapims.user.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDto {

    @NotNull
    private Long id;
    @NotNull
    private String nome;
    @NotNull
    private String email;
    @NotNull
    private Role role;

}
