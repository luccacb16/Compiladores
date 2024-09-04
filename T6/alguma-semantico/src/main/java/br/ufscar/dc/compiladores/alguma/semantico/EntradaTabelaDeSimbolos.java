package br.ufscar.dc.compiladores.alguma.semantico;

public class EntradaTabelaDeSimbolos {
    public enum Tipos {
        REAL, STRING, BOOL, OBJECT, ARRAY, NUMBER, NULL
    }

    public String nome;
    public Tipos tipo;

    public EntradaTabelaDeSimbolos(String nome, Tipos tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }
}
