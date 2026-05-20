package br.edu.ifg.trilhadeaprendizadoapims.learning.model;

public enum StatusProgresso {
//    NAO_INICIADO("nao_iniciado"),
    EM_PROGRESSO("em_progresso"),
    CONCLUIDO("concluido")
//    PAUSADO("pausado")
    ;

    private String nome;

    StatusProgresso(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
