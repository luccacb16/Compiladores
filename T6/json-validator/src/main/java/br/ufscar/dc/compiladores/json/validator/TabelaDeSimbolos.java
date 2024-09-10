package br.ufscar.dc.compiladores.json.validator;

import java.util.HashMap;

public class TabelaDeSimbolos {

    private HashMap<String, EntradaTabelaDeSimbolos> tabelaDeSimbolos;

    public TabelaDeSimbolos() {
        tabelaDeSimbolos = new HashMap<>();
    }

    public void inserir(String nome, EntradaTabelaDeSimbolos.Tipos tipo) {
        EntradaTabelaDeSimbolos entrada = new EntradaTabelaDeSimbolos(nome, tipo);
        tabelaDeSimbolos.put(nome, entrada);
    }

    public EntradaTabelaDeSimbolos verificar(String nome) {
        return tabelaDeSimbolos.get(nome);
    }

    public boolean possui(String name) {
        return tabelaDeSimbolos.containsKey(name); 
    }
}