package br.edu.ifg.trilhadeaprendizagemapims.trilha.model;

import br.edu.ifg.trilhadeaprendizagemapims.trilha.model.enums.TipoConquista;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "conquistas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EConquista implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoConquista tipo;

    @OneToOne(mappedBy = "conquista")
    private EModulo modulo;

    @OneToOne(mappedBy = "conquista")
    private ETrilha trilha;

    @NotNull
    @Positive
    private int xpGanho;

    @PrePersist
    @PreUpdate
    private void definirTipoAutomatico() {
        if (trilha != null && modulo != null) {
            throw new IllegalStateException("Conquista não pode estar ligada a Trilha e Módulo ao mesmo tempo!");
        } else if (trilha != null) {
            this.tipo = TipoConquista.TRILHA;
        } else if (modulo != null) {
            this.tipo = TipoConquista.MODULO;
        } else {
            this.tipo = TipoConquista.GERAL;
        }
    }
}
