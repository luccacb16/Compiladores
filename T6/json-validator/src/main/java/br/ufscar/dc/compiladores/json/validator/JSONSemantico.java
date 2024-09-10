package br.ufscar.dc.compiladores.json.validator;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

public class JSONSemantico extends JSONBaseVisitor<EntradaTabelaDeSimbolos.Tipos> {
    Escopos escoposAninhados = new Escopos();

    public static List<String> errosSemanticos = new ArrayList<>();
    
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
    }

    @Override
    public EntradaTabelaDeSimbolos.Tipos visitJson(JSONParser.JsonContext ctx) {
        return visit(ctx.value());
    }

    @Override
    public EntradaTabelaDeSimbolos.Tipos visitObj(JSONParser.ObjContext ctx) {
        escoposAninhados.criarNovoEscopo();
        for (JSONParser.Par_chave_valorContext par : ctx.par_chave_valor()) {
            visit(par);
        }
        escoposAninhados.abandonarEscopo();
        return EntradaTabelaDeSimbolos.Tipos.OBJECT;
    }

    @Override
    public EntradaTabelaDeSimbolos.Tipos visitPar_chave_valor(JSONParser.Par_chave_valorContext ctx) {
        String chave = ctx.STRING().getText().replace("\"", "");
        
        // Análise Semântica 1: Chave vazia = ""
        if (chave.isEmpty()) {
            adicionarErroSemantico(ctx.STRING().getSymbol(), "A chave não pode ser vazia");
        }
        
        EntradaTabelaDeSimbolos.Tipos tipoValor = visit(ctx.value()); // visit agora retorna Tipos
        
        // Análise Semântica 2: Chave duplicada
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
        boolean tipoInconsistenteReportado = false;
    
        for (JSONParser.ValueContext valorCtx : ctx.value()) {
            EntradaTabelaDeSimbolos.Tipos tipoAtual = visit(valorCtx);
            
            // Análise semântica 3: Array com elementos de tipos diferentes
            if (tipoAnterior != null && tipoAtual != tipoAnterior && !tipoInconsistenteReportado) {
                adicionarErroSemantico(ctx.getStart(), "Arrays devem ter todos os elementos do mesmo tipo");
                tipoInconsistenteReportado = true; // Marca que um erro já foi reportado para este array
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
