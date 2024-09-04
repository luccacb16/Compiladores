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

    // Adiciona um erro semântico
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
    }

    // Determina o tipo de uma expressão 
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.ExpressaoContext ctx) {
        EntradaTabelaDeSimbolos.Tipos retTipo = null;
        int i = 0;
        while (i < ctx.termo_logico().size()) {
            Termo_logicoContext tl = ctx.termo_logico(i);
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, tl);
            if (retTipo == null) {
                retTipo = aux;
            } else if (retTipo != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
            i++;
        }
        return retTipo;
    }

    // Determina o tipo de um termo lógico
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Termo_logicoContext ctx) {
        EntradaTabelaDeSimbolos.Tipos retTipo = null;
        int i = 0;
        while (i < ctx.fator_logico().size()) {
            Fator_logicoContext tl = ctx.fator_logico(i);
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, tl);
            if (retTipo == null) {
                retTipo = aux;
            } else if (retTipo != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
            i++;
        }
        return retTipo;
    }

    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Fator_logicoContext ctx) {
        return verificarTipo(escopos, ctx.parcela_logica());
    }

    // Determina o tipo de uma parcela lógica
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Parcela_logicaContext ctx) {
        EntradaTabelaDeSimbolos.Tipos retTipo = null;
        if (ctx.exp_relacional() != null) {
            retTipo = verificarTipo(escopos, ctx.exp_relacional());
        } else {
            retTipo = EntradaTabelaDeSimbolos.Tipos.LOGICO;
        }

        return retTipo;
    }

    // Determina o tipo de uma expressão relacional
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Exp_relacionalContext ctx) {
        EntradaTabelaDeSimbolos.Tipos retTipo = null;
        if (ctx.op_relacional() != null) {
            int i = 0;
            while (i < ctx.exp_aritmetica().size()) {
                Exp_aritmeticaContext ta = ctx.exp_aritmetica(i);
                EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, ta);
                Boolean auxNumeric = aux == EntradaTabelaDeSimbolos.Tipos.REAL || aux == EntradaTabelaDeSimbolos.Tipos.INT;
                Boolean retNumeric = retTipo == EntradaTabelaDeSimbolos.Tipos.REAL || retTipo == EntradaTabelaDeSimbolos.Tipos.INT;

                if (retTipo == null) {
                    retTipo = aux;
                } else if (!(auxNumeric && retNumeric) && aux != retTipo) {
                    retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
                }
                i++;
            }
            if (retTipo != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                retTipo = EntradaTabelaDeSimbolos.Tipos.LOGICO;
            }
        } else {
            retTipo = verificarTipo(escopos, ctx.exp_aritmetica(0));
        }

        return retTipo;
    }

    // Verifica o tipo de uma expressão aritmética
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Exp_aritmeticaContext ctx) {
        EntradaTabelaDeSimbolos.Tipos retTipo = null;
        int i = 0;
        while (i < ctx.termo().size()) {
            TermoContext ta = ctx.termo(i);
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, ta);
            if (retTipo == null) {
                retTipo = aux;
            } else if (retTipo != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
            i++;
        }

        return retTipo;
    }

    // Verifica o tipo de um termo em uma expressão
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.TermoContext ctx) {
        EntradaTabelaDeSimbolos.Tipos retTipo = null;

        int i = 0;
        while (i < ctx.fator().size()) {
            FatorContext fa = ctx.fator(i);
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, fa);
            Boolean auxNumeric = aux == EntradaTabelaDeSimbolos.Tipos.REAL || aux == EntradaTabelaDeSimbolos.Tipos.INT; 
            Boolean retNumeric = retTipo == EntradaTabelaDeSimbolos.Tipos.REAL || retTipo == EntradaTabelaDeSimbolos.Tipos.INT;
            if (retTipo == null) {
                retTipo = aux;
            } else if (!(auxNumeric && retNumeric) && aux != retTipo) {
                retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
            i++;
        }
        return retTipo;
    }

    // Verifica o tipo de um fator em uma expressão
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.FatorContext ctx) {
        EntradaTabelaDeSimbolos.Tipos retTipo = null;
    
        int i = 0;
        while (i < ctx.parcela().size()) {
            ParcelaContext fa = ctx.parcela(i);
            EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, fa);
            if (retTipo == null) {
                retTipo = aux;
            } else if (retTipo != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
            }
            i++;
        }
        return retTipo;
    }
    

    // Verifica o tipo de uma parcela em uma expressão
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.ParcelaContext ctx) {
        EntradaTabelaDeSimbolos.Tipos retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;

        if (ctx.parcela_nao_unario() != null) {
            retTipo = verificarTipo(escopos, ctx.parcela_nao_unario());
        } else {
            retTipo = verificarTipo(escopos, ctx.parcela_unario());
        }
        return retTipo;
    }

    // Verifica o tipo de uma parcela não unária
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Parcela_nao_unarioContext ctx) {
        if (ctx.identificador() != null) {
            return verificarTipo(escopos, ctx.identificador());
        }
        return EntradaTabelaDeSimbolos.Tipos.CADEIA;
    }

    // Verifica o tipo de um identificador
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.IdentificadorContext ctx) {
        String Var = "";
        EntradaTabelaDeSimbolos.Tipos retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
        int i = 0;
        while (i < ctx.IDENT().size()) {
            Var += ctx.IDENT(i).getText();
            if (i != ctx.IDENT().size() - 1) {
                Var += ".";
            }
            i++;
        }
        for (TabelaDeSimbolos tabela : escopos.percorrerEscoposAninhados()) {
            if (tabela.possui(Var)) {
                retTipo = verificarTipo(escopos, Var);
            }
        }
        return retTipo;
    }

    // Verifica o tipo de uma parcela unária
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, LAParser.Parcela_unarioContext ctx) {
        if (ctx.identificador() != null) {
            return verificarTipo(escopos, ctx.identificador());
        }
        if (ctx.NUM_REAL() != null) {
            return EntradaTabelaDeSimbolos.Tipos.REAL;
        }
        if (ctx.NUM_INT() != null) {
            return EntradaTabelaDeSimbolos.Tipos.INT;
        }
        if (ctx.IDENT() != null) {
            return verificarTipo(escopos, ctx.IDENT().getText());
        } else {
            EntradaTabelaDeSimbolos.Tipos retTipo = null;
            int i = 0;
            while (i < ctx.expressao().size()) {
                ExpressaoContext fa = ctx.expressao(i);
                EntradaTabelaDeSimbolos.Tipos aux = verificarTipo(escopos, fa);
                if (retTipo == null) {
                    retTipo = aux;
                } else if (retTipo != aux && aux != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                    retTipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
                }
                i++;
            }
            return retTipo;
        }
    }

    // Verifica o tipo de uma variável
    public static EntradaTabelaDeSimbolos.Tipos verificarTipo(Escopo escopos, String Var) {
        EntradaTabelaDeSimbolos.Tipos tipo = EntradaTabelaDeSimbolos.Tipos.INVALIDO;
        int i = 0;
        while (i < escopos.percorrerEscoposAninhados().size()) {
            TabelaDeSimbolos tabela = escopos.percorrerEscoposAninhados().get(i);
            if (tabela.possui(Var)) {
                return tabela.verificar(Var);
            }
            i++;
        }

        return tipo;
    }

    // Verifica o tipo de uma variável
    public static EntradaTabelaDeSimbolos.Tipos obterTipo(String val) {
        EntradaTabelaDeSimbolos.Tipos tipo = null;
        switch (val) {
            case "real":
                tipo = EntradaTabelaDeSimbolos.Tipos.REAL;
                break;
            case "inteiro":
                tipo = EntradaTabelaDeSimbolos.Tipos.INT;
                break;
            case "logico":
                tipo = EntradaTabelaDeSimbolos.Tipos.LOGICO;
                break;
            case "literal":
                tipo = EntradaTabelaDeSimbolos.Tipos.CADEIA;
                break;
            default:
                break;
        }
        return tipo;
    }

}
