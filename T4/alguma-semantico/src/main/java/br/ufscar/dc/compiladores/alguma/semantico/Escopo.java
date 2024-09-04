package br.ufscar.dc.compiladores.alguma.semantico;

import java.util.LinkedList;
import java.util.List;

public class Escopo {
    private LinkedList<TabelaDeSimbolos> pilha;

    public Escopo(EntradaTabelaDeSimbolos.Tipos tipoRetorno) {
        pilha = new LinkedList<>();
        criarNovoEscopo(tipoRetorno);
    }

    public void criarNovoEscopo(EntradaTabelaDeSimbolos.Tipos tipoRetorno) {
        pilha.push(new TabelaDeSimbolos(tipoRetorno));
    }

    public TabelaDeSimbolos obterEscopoAtual() {
        return pilha.peek();
    }

    public List<TabelaDeSimbolos> percorrerEscoposAninhados() {
        return pilha;
    }

    public void abandonarEscopo() {
        pilha.pop();
    }
}
