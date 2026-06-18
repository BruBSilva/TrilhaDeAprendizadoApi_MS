package br.edu.ifg.trilhadeaprendizagemapims.trilha.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "modulos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EModulo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "trilha_id")
    private ETrilha trilha;

    @NotNull
    @Positive
    private int ordem;

    @NotNull
    @Lob
    private String conteudo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conquista_id", unique = true)
    private EConquista conquista;
}
