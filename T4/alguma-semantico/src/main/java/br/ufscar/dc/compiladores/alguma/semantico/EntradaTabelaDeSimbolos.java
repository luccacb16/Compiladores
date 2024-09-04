package br.ufscar.dc.compiladores.alguma.semantico;

class EntradaTabelaDeSimbolos {
    public enum Tipos {
        INT, REAL, CADEIA, LOGICO, INVALIDO, REG, VOID
    }

    public enum Estrutura {
        VAR, CONST, PROC, FUNC, TIPO
    }
    
    String name;
    Tipos tipo;
    Estrutura estrutura;

    public EntradaTabelaDeSimbolos(String name, Tipos tipo, Estrutura estrutura) {
        this.name = name;
        this.tipo = tipo;
        this.estrutura = estrutura;
    }
}