package br.edu.ifg.trilhadeaprendizagemapims.trilha.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TrilhaDTO {
    private Long id;
    @NotNull
    private String nome;
    @NotNull
    private String descricao;
    @NotNull
    private CategoriaDTO categoria;
    @NotNull
    private ArrayList<ModuloDTO> modulos;
    @NotNull
    private ConquistaDTO conquista;
}
