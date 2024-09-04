package br.ufscar.dc.compiladores.alguma.semantico;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

public class JSONSemantico extends JSONBaseVisitor<EntradaTabelaDeSimbolos.Tipos> {
    Escopos escoposAninhados = new Escopos();

    public static List<String> errosSemanticos = new ArrayList<>();
    
    // Adiciona um erro semântico à lista com a linha e a mensagem de erro
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
    }

    @Override
    public EntradaTabelaDeSimbolos.Tipos visitJson(JSONParser.JsonContext ctx) {
        return visit(ctx.value()); // Agora retorna o tipo corretamente
    }

    @Override
    public EntradaTabelaDeSimbolos.Tipos visitObj(JSONParser.ObjContext ctx) {
        escoposAninhados.criarNovoEscopo();
        for (JSONParser.Par_chave_valorContext par : ctx.par_chave_valor()) {
            visit(par); // Visitando pares chave-valor
        }
        escoposAninhados.abandonarEscopo();
        return EntradaTabelaDeSimbolos.Tipos.OBJECT; // Retorna OBJECT para objetos
    }

    @Override
    public EntradaTabelaDeSimbolos.Tipos visitPar_chave_valor(JSONParser.Par_chave_valorContext ctx) {
        String chave = ctx.STRING().getText().replace("\"", "");
        EntradaTabelaDeSimbolos.Tipos tipoValor = visit(ctx.value()); // visit agora retorna Tipos

        if (escoposAninhados.obterEscopoAtual().possui(chave)) {
            adicionarErroSemantico(ctx.STRING().getSymbol(), "Chave " + chave + " duplicada");
        } else {
            escoposAninhados.obterEscopoAtual().inserir(chave, tipoValor);
        }

        return null; // Pode retornar null, pois esse método lida com inserção na tabela
    }

    @Override
    public EntradaTabelaDeSimbolos.Tipos visitArray(JSONParser.ArrayContext ctx) {
        EntradaTabelaDeSimbolos.Tipos tipoAnterior = null;

        for (JSONParser.ValueContext valorCtx : ctx.value()) {
            EntradaTabelaDeSimbolos.Tipos tipoAtual = visit(valorCtx);

            if (tipoAnterior != null && tipoAtual != tipoAnterior) {
                adicionarErroSemantico(ctx.getStart(), "Arrays devem ter todos os elementos do mesmo tipo");
            }
            tipoAnterior = tipoAtual;
        }

        return EntradaTabelaDeSimbolos.Tipos.ARRAY;
    }

    @Override
    public EntradaTabelaDeSimbolos.Tipos visitValue(JSONParser.ValueContext ctx) {
        if (ctx.STRING() != null) {
            return EntradaTabelaDeSimbolos.Tipos.STRING;
        } else if (ctx.NUMBER() != null) {
            return EntradaTabelaDeSimbolos.Tipos.NUMBER;
        } else if (ctx.obj() != null) {
            return visitObj(ctx.obj());
        } else if (ctx.array() != null) {
            return visitArray(ctx.array());
        } else if (ctx.TRUE() != null || ctx.FALSE() != null) {
            return EntradaTabelaDeSimbolos.Tipos.BOOL;
        } else if (ctx.NULL() != null) {
            return EntradaTabelaDeSimbolos.Tipos.NULL;
        }

        return EntradaTabelaDeSimbolos.Tipos.NULL;
    }
}
