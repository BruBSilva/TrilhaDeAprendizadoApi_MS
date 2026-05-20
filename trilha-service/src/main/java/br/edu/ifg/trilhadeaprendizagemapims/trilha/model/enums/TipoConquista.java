package br.edu.ifg.trilhadeaprendizagemapims.trilha.model.enums;

import lombok.Getter;

@Getter
public enum TipoConquista {
    MODULO("Conclusão do Módulo"),
    TRILHA("Conclusão da Trilha"),
    GERAL("Conquista Geral");

    private final String descricao;

    TipoConquista(String descricao) {
        this.descricao = descricao;
    }
}
