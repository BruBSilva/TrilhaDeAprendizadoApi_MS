package br.edu.ifg.trilhadeaprendizadoapims.learning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "progressos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EProgresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotNull
    @Column(name = "trilha_id")
    private Long trilhaId;

    @ElementCollection
    @CollectionTable(name = "progressos_trilha_modulos", joinColumns = @JoinColumn(name = "progressos_id"))
    @NotNull
    private List<Long> trilha_modulos;

    @NotNull
    private LocalDateTime dataInicio;

    private LocalDateTime dataConclusao;

    @NotNull
    private int xpGanho;

    @NotNull
    private Double percentual = 0.0;

    @NotNull
    private Long moduloAtual_id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusProgresso statusProgresso;

    public void setPercentual(Double percentual) {
        this.percentual += percentual;
    }

    public void setXpGanho(int xp) {
        this.xpGanho += xp;
    }

}
