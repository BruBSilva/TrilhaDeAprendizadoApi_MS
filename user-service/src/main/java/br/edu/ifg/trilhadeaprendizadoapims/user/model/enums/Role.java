package br.edu.ifg.trilhadeaprendizadoapims.user.model.enums;

public enum Role {
    ADMIN("admin"),
    ALUNO("aluno");

    private String nome;

    Role(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
