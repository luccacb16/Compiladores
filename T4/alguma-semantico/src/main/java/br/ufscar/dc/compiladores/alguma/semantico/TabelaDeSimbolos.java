package br.ufscar.dc.compiladores.alguma.semantico;

import java.util.ArrayList;
import java.util.HashMap;

public class TabelaDeSimbolos {
    public EntradaTabelaDeSimbolos.Tipos tipoRetorno;

    private HashMap<String, EntradaTabelaDeSimbolos> tabela;
    private HashMap<String, ArrayList<EntradaTabelaDeSimbolos>> tabelaTipos;

    public TabelaDeSimbolos(EntradaTabelaDeSimbolos.Tipos tipoRetorno) {
        tabela = new HashMap<>();
        tabelaTipos = new HashMap<>();
        this.tipoRetorno = tipoRetorno;
    }

    public void inserir(String name, EntradaTabelaDeSimbolos.Tipos tipo, EntradaTabelaDeSimbolos.Estrutura estrutura) {
        EntradaTabelaDeSimbolos input = new EntradaTabelaDeSimbolos(name, tipo, estrutura);
        tabela.put(name, input);
    }

    public void inserir(EntradaTabelaDeSimbolos input) {
        tabela.put(input.name, input);

    }

    public void inserir(String tipoName, EntradaTabelaDeSimbolos input) {
        if (tabelaTipos.containsKey(tipoName)) {
            tabelaTipos.get(tipoName).add(input);
        } else {
            ArrayList<EntradaTabelaDeSimbolos> list = new ArrayList<>();
            list.add(input);
            tabelaTipos.put(tipoName, list);
        }
    }

    public EntradaTabelaDeSimbolos.Tipos verificar(String name) {
        return tabela.get(name).tipo;
    }

    public boolean possui(String name) {
        return tabela.containsKey(name); 
    }

    public ArrayList<EntradaTabelaDeSimbolos> obterPropriedadesTipo(String name) {
        return tabelaTipos.get(name);
    }
}