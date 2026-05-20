package br.edu.ifg.trilhadeaprendizagemapims.trilha.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trilhas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ETrilha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private ECategoria categoria;

    @OneToMany(mappedBy = "trilha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EModulo> modulos = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conquista_id", unique = true)
    private EConquista conquista;
}
