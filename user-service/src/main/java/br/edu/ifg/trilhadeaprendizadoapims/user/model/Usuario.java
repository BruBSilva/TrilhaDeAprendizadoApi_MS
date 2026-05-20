package br.edu.ifg.trilhadeaprendizadoapims.user.model;

import br.edu.ifg.trilhadeaprendizadoapims.user.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    private String nome;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 100)
    private String senhaHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    protected void definirRoleAutomaticamente() {
        if (getClass().equals(Administrador.class)) {
            this.role = Role.ADMIN;
        } else if (getClass().equals(Aluno.class)) {
            this.role = Role.ALUNO;
        } else if (this.role == null) {
            throw new IllegalStateException("Role não definida para a subclasse: " + getClass().getSimpleName());
        }
    }
}
