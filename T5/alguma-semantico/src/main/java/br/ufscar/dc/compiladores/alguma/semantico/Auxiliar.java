package br.ufscar.dc.compiladores.alguma.semantico;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;

import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Exp_aritmeticaContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.ExpressaoContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.FatorContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Fator_logicoContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.ParcelaContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.TermoContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Termo_logicoContext;

public class Auxiliar {
    public static List<String> errosSemanticos = new ArrayList<>();
    
    // Adiciona um erro semântico à lista com a linha e a mensagem de erro
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
    }
    
    // Verifica o tipo de uma expressão, garantindo que os termos sejam compatíveis
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.ExpressaoContext ctx) {
        EntradaTabelaDeSimbolos.Tipos ret = null;
        for (Termo_logicoContext ta : ctx.termo_logico()) {
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, ta);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
        }

        return ret;
    }

    // Verifica o tipo de um termo lógico, garantindo que os fatores sejam compatíveis
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Termo_logicoContext ctx) {
        EntradaTabelaDeSimbolos.Tipos ret = null;
        for (Fator_logicoContext ta : ctx.fator_logico()) {
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, ta);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
        }

        return ret;
    }

    // Verifica o tipo de um fator lógico
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Fator_logicoContext ctx) {
        return verificarTipo(escopos, ctx.parcela_logica());
    }

    // Verifica o tipo de uma parcela lógica
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Parcela_logicaContext ctx) {
        EntradaTabelaDeSimbolos.Tipos ret = null;
        if (ctx.exp_relacional() != null) {
            ret = verificarTipo(escopos, ctx.exp_relacional());
        } else {
            ret = EntradaTabelaDeSimbolos.Tipos.LOGICO;
        }

        return ret;
    }

    // Verifica o tipo de uma expressão relacional
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Exp_relacionalContext ctx) {
        EntradaTabelaDeSimbolos.Tipos ret = null;
        if (ctx.op_relacional() != null) {
            for (Exp_aritmeticaContext ta : ctx.exp_aritmetica()) {
                EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, ta);
                Boolean auxNumeric = aux == EntradaTabelaDeSimbolos.Tipos.REAL || aux == EntradaTabelaDeSimbolos.Tipos.INT;
                Boolean retNumeric = ret == EntradaTabelaDeSimbolos.Tipos.REAL || ret == EntradaTabelaDeSimbolos.Tipos.INT;
                if (ret == null) {
                    ret = aux;
                } else if (!(auxNumeric && retNumeric) && aux != ret) {
                    ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
                }
            }
            if (ret != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                ret = EntradaTabelaDeSimbolos.Tipos.LOGICO;
            }
        } else {
            ret = verificarTipo(escopos, ctx.exp_aritmetica(0));
        }

        return ret;
    }

    // Verifica o tipo de uma expressão aritmética
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Exp_aritmeticaContext ctx) {
        EntradaTabelaDeSimbolos.Tipos ret = null;
        for (TermoContext ta : ctx.termo()) {
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, ta);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
        }

        return ret;
    }

    // Verifica o tipo de um termo aritmético
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.TermoContext ctx) {
        EntradaTabelaDeSimbolos.Tipos ret = null;

        for (FatorContext fa : ctx.fator()) {
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, fa);
            Boolean auxNumeric = aux == EntradaTabelaDeSimbolos.Tipos.REAL || aux == EntradaTabelaDeSimbolos.Tipos.INT;
            Boolean retNumeric = ret == EntradaTabelaDeSimbolos.Tipos.REAL || ret == EntradaTabelaDeSimbolos.Tipos.INT;
            if (ret == null) {
                ret = aux;
            } else if (!(auxNumeric && retNumeric) && aux != ret) {
                ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
        }

        return ret;
    }

    // Verifica o tipo de um fator aritmético
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.FatorContext ctx) {
        EntradaTabelaDeSimbolos.Tipos ret = null;

        for (ParcelaContext fa : ctx.parcela()) {
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, fa);
            if (ret == null) {
                ret = aux;
            } else if (ret != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
        }

        return ret;
    }

    // Verifica o tipo de uma parcela, considerando os diferentes tipos de parcela
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.ParcelaContext ctx) {
        EntradaTabelaDeSimbolos.Tipos ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;

        if (ctx.parcela_nao_unario() != null) {
            ret = verificarTipo(escopos, ctx.parcela_nao_unario());
        } else {
            ret = verificarTipo(escopos, ctx.parcela_unario());
        }

        return ret;
    }

    // Verifica o tipo de uma parcela não-unária
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Parcela_nao_unarioContext ctx) {
        if (ctx.identificador() != null) {
            return verificarTipo(escopos, ctx.identificador());
        }
        return EntradaTabelaDeSimbolos.Tipos.CADEIA;
    }

    // Verifica o tipo de um identificador
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.IdentificadorContext ctx) {
        String nomeVar = "";
        EntradaTabelaDeSimbolos.Tipos ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
        for (int i = 0; i < ctx.IDENT().size(); i++) {
            nomeVar += ctx.IDENT(i).getText();
            if (i != ctx.IDENT().size() - 1) {
                nomeVar += ".";
            }
        }
        for (TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()) {
            if (tabela.possui(nomeVar)) {
                ret = verificarTipo(escopos, nomeVar);
            }
        }

        return ret;
    }
    
    // Verifica o tipo de uma parcela unária
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Parcela_unarioContext ctx) {
        if (ctx.NUM_INT() != null) {
            return EntradaTabelaDeSimbolos.Tipos.INT;
        }
        if (ctx.NUM_REAL() != null) {
            return EntradaTabelaDeSimbolos.Tipos.REAL;
        }
        if (ctx.identificador() != null) {
            return verificarTipo(escopos, ctx.identificador());
        }
        if (ctx.IDENT() != null) {
            return verificarTipo(escopos, ctx.IDENT().getText());
        } else {
            EntradaTabelaDeSimbolos.Tipos ret = null;
            for (ExpressaoContext fa : ctx.expressao()) {
                EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, fa);
                if (ret == null) {
                    ret = aux;
                } else if (ret != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                    ret = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
                }
            }
            return ret;
        }
    }
    
    // Verifica o tipo de uma variável a partir de seu nome
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, String nomeVar) {
        EntradaTabelaDeSimbolos.Tipos type = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
        for (TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()) {
            if (tabela.possui(nomeVar)) {
                return tabela.verificar(nomeVar);
            }
        }

        return type;
    }

    // Retorna o tipo de uma variável baseado em uma string
    public static EntradaTabelaDeSimbolos.Tipos getTipo(String val) {
        EntradaTabelaDeSimbolos.Tipos tipo = null;
        switch (val) {
            case "literal": 
                tipo = EntradaTabelaDeSimbolos.Tipos.CADEIA;
                break;
            case "inteiro": 
                tipo = EntradaTabelaDeSimbolos.Tipos.INT;
                break;
            case "real": 
                tipo = EntradaTabelaDeSimbolos.Tipos.REAL;
                break;
            case "logico": 
                tipo = EntradaTabelaDeSimbolos.Tipos.LOGICO;
                break;
            default:
                break;
        }
        return tipo;
    }

    // Retorna o tipo em C baseado em uma string
    public static String getCType(String val) {
        String tipo = null;
        switch (val) {
            case "literal": 
                tipo = "char";
                break;
            case "inteiro": 
                tipo = "int";
                break;
            case "real": 
                tipo = "float";
                break;
            default:
                break;
        }
        return tipo;
    }

    // Retorna o símbolo de tipo em C baseado em um tipo
    public static String getCTypeSymbol(EntradaTabelaDeSimbolos.Tipos tipo) {
        String type = null;
        switch (tipo) {
            case CADEIA: 
                type = "s";
                break;
            case INT: 
                type = "d";
                break;
            case REAL: 
                type = "f";
                break;
            default:
                break;
        }
        return type;
    }
}