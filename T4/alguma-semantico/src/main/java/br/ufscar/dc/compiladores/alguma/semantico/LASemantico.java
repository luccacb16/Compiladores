package br.ufscar.dc.compiladores.alguma.semantico;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.antlr.v4.runtime.tree.TerminalNode;

import br.ufscar.dc.compiladores.alguma.semantico.LAParser.CmdAtribuicaoContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.CmdRetorneContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Declaracao_constanteContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Declaracao_globalContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Declaracao_tipoContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Declaracao_variavelContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.IdentificadorContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.ParametroContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Parcela_unarioContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.ProgramaContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Tipo_basico_identContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.VariavelContext;

public class LASemantico extends LABaseVisitor<Object> {
    
    Escopo escoposAninhados = new Escopo(EntradaTabelaDeSimbolos.Tipos.VOID);

    @Override
    public Object visitPrograma(ProgramaContext ctx) {  
        return super.visitPrograma(ctx);
    }

    // Verifica se uma constante foi declarada anteriormente
    @Override
    public Object visitDeclaracao_constante(Declaracao_constanteContext ctx) {
        TabelaDeSimbolos escopoAtual = escoposAninhados.obterEscopoAtual();
        
        if (escopoAtual.possui(ctx.IDENT().getText())) {
            Auxiliar.adicionarErroSemantico(ctx.start, "constante" + ctx.IDENT().getText()+ " ja declarado anteriormente");
        } else {
            EntradaTabelaDeSimbolos.Tipos tipo = EntradaTabelaDeSimbolos.Tipos.INT;
            EntradaTabelaDeSimbolos.Tipos aux = Auxiliar.obterTipo(ctx.tipo_basico().getText()) ;
            
            if (aux != null)
                tipo = aux;
            escopoAtual.inserir(ctx.IDENT().getText(), tipo, EntradaTabelaDeSimbolos.Estrutura.CONST);
        }

        return super.visitDeclaracao_constante(ctx);
    }

    // Verifica se um tipo foi declarado anteriormente
    @Override
    public Object visitDeclaracao_tipo(Declaracao_tipoContext ctx) {
        TabelaDeSimbolos escopoAtual = escoposAninhados.obterEscopoAtual();
        
        if (escopoAtual.possui(ctx.IDENT().getText())) {
             Auxiliar.adicionarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText()+ " declarado duas vezes num mesmo escopo");
        } else {
            EntradaTabelaDeSimbolos.Tipos tipo = Auxiliar.obterTipo(ctx.tipo().getText());
            
            if (tipo != null)
                escopoAtual.inserir(ctx.IDENT().getText(), tipo, EntradaTabelaDeSimbolos.Estrutura.TIPO);
            else 
            if (ctx.tipo().registro() != null) {
                ArrayList<EntradaTabelaDeSimbolos> varRegistro = new ArrayList<>();
                Iterator<VariavelContext> varIterator = ctx.tipo().registro().variavel().iterator();
                while (varIterator.hasNext()) {
                    VariavelContext variavel = varIterator.next();
                    EntradaTabelaDeSimbolos.Tipos tipoReg = Auxiliar.obterTipo(variavel.tipo().getText());
                    
                    Iterator<IdentificadorContext> idIterator = variavel.identificador().iterator();
                    while (idIterator.hasNext()) {
                        IdentificadorContext id2 = idIterator.next();
                        varRegistro.add(new EntradaTabelaDeSimbolos(id2.getText(), tipoReg, EntradaTabelaDeSimbolos.Estrutura.TIPO));
                    }
                }
                
                if (escopoAtual.possui(ctx.IDENT().getText())) {
                    Auxiliar.adicionarErroSemantico(ctx.start, "identificador " + ctx.IDENT().getText() + " ja declarado anteriormente");
                }
                else {
                    escopoAtual.inserir(ctx.IDENT().getText(), EntradaTabelaDeSimbolos.Tipos.REG, EntradaTabelaDeSimbolos.Estrutura.TIPO);
                }

                
                for (EntradaTabelaDeSimbolos registro : varRegistro) {
                    String nameVariavel = ctx.IDENT().getText() + '.' + registro.name;
                    
                    if (escopoAtual.possui(nameVariavel)) {
                        Auxiliar.adicionarErroSemantico(ctx.start, "identificador " + nameVariavel + " ja declarado anteriormente");
                    }
                    else {
                        escopoAtual.inserir(registro);
                        escopoAtual.inserir(ctx.IDENT().getText(), registro);
                    }
                }
            }
            EntradaTabelaDeSimbolos.Tipos t =  Auxiliar.obterTipo(ctx.tipo().getText());
            escopoAtual.inserir(ctx.IDENT().getText(), t, EntradaTabelaDeSimbolos.Estrutura.TIPO);
        }   return super.visitDeclaracao_tipo(ctx);
    }

    // Verifica se um tipo foi declarado anteriormente
    @Override
    public Object visitTipo_basico_ident(Tipo_basico_identContext ctx) {
        
        if (ctx.IDENT() != null) {
            boolean possui = false;
            
            for (TabelaDeSimbolos escopo : escoposAninhados.percorrerEscoposAninhados()) {
                
                if (escopo.possui(ctx.IDENT().getText())) {
                    possui = true;
                }
            }
            
            if (!possui) {
                Auxiliar.adicionarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText() + " nao declarado");
            }
        }   return super.visitTipo_basico_ident(ctx);
    }

    // Verifica se uma função ou procedimento foi declarado anteriormente
    @Override
    public Object visitDeclaracao_global(Declaracao_globalContext ctx) {
        TabelaDeSimbolos escopoAtual = escoposAninhados.obterEscopoAtual();
        Object ret;
        
        if (escopoAtual.possui(ctx.IDENT().getText())) {
            Auxiliar.adicionarErroSemantico(ctx.start, ctx.IDENT().getText() + " ja declarado anteriormente");
            ret = super.visitDeclaracao_global(ctx);
        } else {
            EntradaTabelaDeSimbolos.Tipos tipoRetornoFuncao = EntradaTabelaDeSimbolos.Tipos.VOID;
            
            if (ctx.getText().startsWith("funcao")) {
                tipoRetornoFuncao = Auxiliar.obterTipo(ctx.tipo_estendido().getText());
                escopoAtual.inserir(ctx.IDENT().getText(), tipoRetornoFuncao, EntradaTabelaDeSimbolos.Estrutura.FUNC);
            }
            else {
                tipoRetornoFuncao = EntradaTabelaDeSimbolos.Tipos.VOID;
                escopoAtual.inserir(ctx.IDENT().getText(), tipoRetornoFuncao, EntradaTabelaDeSimbolos.Estrutura.PROC);
            }
            escoposAninhados.criarNovoEscopo(tipoRetornoFuncao);
            TabelaDeSimbolos escopoAntigo = escopoAtual;
            escopoAtual = escoposAninhados.obterEscopoAtual();
            
            if (ctx.parametros() != null) {
                
                for (ParametroContext p : ctx.parametros().parametro()) {
                    
                    for (IdentificadorContext id : p.identificador()) {
                        String nomeId = "";
                        int i = 0;
                        
                        for (TerminalNode ident : id.IDENT()) {
                            
                            if (i++ > 0)
                                nomeId += ".";
                            nomeId += ident.getText();
                        }
                        
                        if (escopoAtual.possui(nomeId)) {
                            Auxiliar.adicionarErroSemantico(id.start, "identificador " + nomeId + " ja declarado anteriormente");
                        } else {
                            EntradaTabelaDeSimbolos.Tipos tipo = Auxiliar.obterTipo(p.tipo_estendido().getText());
                            
                            if (tipo != null) {
                                EntradaTabelaDeSimbolos in = new EntradaTabelaDeSimbolos(nomeId, tipo, EntradaTabelaDeSimbolos.Estrutura.VAR);
                                escopoAtual.inserir(in);
                                escopoAntigo.inserir(ctx.IDENT().getText(), in);
                            }
                            else {
                                TerminalNode identTipo = p.tipo_estendido().tipo_basico_ident() != null && p.tipo_estendido().tipo_basico_ident().IDENT() != null ? p.tipo_estendido().tipo_basico_ident().IDENT() : null;
                                
                                if (identTipo != null) {
                                    ArrayList<EntradaTabelaDeSimbolos> regVars = null;
                                    boolean found = false;
                                    
                                    for (TabelaDeSimbolos t: escoposAninhados.percorrerEscoposAninhados()) {
                                        
                                        if (!found) {
                                            
                                            if (t.possui(identTipo.getText())) {
                                                regVars = t.obterPropriedadesTipo(identTipo.getText());
                                                found = true;
                                            }
                                        }
                                    }
                                    
                                    if (escopoAtual.possui(nomeId)) {
                                        Auxiliar.adicionarErroSemantico(id.start, "identificador " + nomeId + " ja declarado anteriormente");
                                    } else {
                                        EntradaTabelaDeSimbolos in = new EntradaTabelaDeSimbolos(nomeId, EntradaTabelaDeSimbolos.Tipos.REG, EntradaTabelaDeSimbolos.Estrutura.VAR);
                                        escopoAtual.inserir(in);
                                        escopoAntigo.inserir(ctx.IDENT().getText(), in);
                                        
                                        for (EntradaTabelaDeSimbolos s: regVars) {
                                            escopoAtual.inserir(nomeId + "." + s.name, s.tipo, EntradaTabelaDeSimbolos.Estrutura.VAR);
                                        }   
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ret = super.visitDeclaracao_global(ctx);
            escoposAninhados.abandonarEscopo();
        }   
        return ret;
    }

    // Verifica se um identificador foi declarado anteriormente
    @Override
    public Object visitIdentificador(IdentificadorContext ctx) {
        String Var = "";
        int i = 0;
        
        for (TerminalNode id : ctx.IDENT()) {
            
            if (i++ > 0)
                Var += ".";
            Var += id.getText();
        }
        boolean erro = true;
        
        for (TabelaDeSimbolos escopo : escoposAninhados.percorrerEscoposAninhados()) {
            
            if (escopo.possui(Var)) {
                erro = false;
            }
        }
        
        if (erro)
            Auxiliar.adicionarErroSemantico(ctx.start, "identificador " + Var + " nao declarado");    return super.visitIdentificador(ctx);
    }

    // Verifica se um identificador foi declarado anteriormente
    @Override
    public Object visitDeclaracao_variavel(Declaracao_variavelContext ctx) {
        TabelaDeSimbolos escopoAtual = escoposAninhados.obterEscopoAtual();
        
        for (IdentificadorContext id : ctx.variavel().identificador()) {
            String nomeId = "";
            int i = 0;
            
            for (TerminalNode ident : id.IDENT()) {
                
                if (i++ > 0)
                    nomeId += ".";
                nomeId += ident.getText();
            }
            
            if (escopoAtual.possui(nomeId)) {
                Auxiliar.adicionarErroSemantico(id.start, "identificador " + nomeId + " ja declarado anteriormente");
            } else {
                EntradaTabelaDeSimbolos.Tipos tipo = Auxiliar.obterTipo(ctx.variavel().tipo().getText());
                
                if (tipo != null)
                    escopoAtual.inserir(nomeId, tipo, EntradaTabelaDeSimbolos.Estrutura.VAR);
                else {
                    TerminalNode identTipo = ctx.variavel().tipo() != null && ctx.variavel().tipo().tipo_estendido() != null && ctx.variavel().tipo().tipo_estendido().tipo_basico_ident() != null && ctx.variavel().tipo().tipo_estendido().tipo_basico_ident().IDENT() != null ? ctx.variavel().tipo().tipo_estendido().tipo_basico_ident().IDENT() : null;
                    
                    if (identTipo != null) {
                        ArrayList<EntradaTabelaDeSimbolos> regVars = null;
                        boolean found = false;
                        
                        for (TabelaDeSimbolos t: escoposAninhados.percorrerEscoposAninhados()) {
                            if (!found) {
                                
                                if (t.possui(identTipo.getText())) {
                                    regVars = t.obterPropriedadesTipo(identTipo.getText());
                                    found = true;
                                }
                            }
                        }
                        
                        if (escopoAtual.possui(nomeId)) {
                            Auxiliar.adicionarErroSemantico(id.start, "identificador " + nomeId
                                        + " ja declarado anteriormente");
                        } else {
                            escopoAtual.inserir(nomeId, EntradaTabelaDeSimbolos.Tipos.REG, EntradaTabelaDeSimbolos.Estrutura.VAR);
                            
                            for (EntradaTabelaDeSimbolos s: regVars) {
                                escopoAtual.inserir(nomeId + "." + s.name, s.tipo, EntradaTabelaDeSimbolos.Estrutura.VAR);
                            }   
                        }
                    }
                    else 
                    if (ctx.variavel().tipo().registro() != null) {
                        ArrayList<EntradaTabelaDeSimbolos> varRegistro = new ArrayList<>();
                        
                        for (VariavelContext variavel : ctx.variavel().tipo().registro().variavel()) {
                            EntradaTabelaDeSimbolos.Tipos tipoReg =  Auxiliar.obterTipo(variavel.tipo().getText());
                            
                            for (IdentificadorContext id2 : variavel.identificador()) {
                                varRegistro.add(new EntradaTabelaDeSimbolos(id2.getText(), tipoReg, EntradaTabelaDeSimbolos.Estrutura.VAR));
                            }
                        }  
                        escopoAtual.inserir(nomeId, EntradaTabelaDeSimbolos.Tipos.REG, EntradaTabelaDeSimbolos.Estrutura.VAR);

                        
                        for (EntradaTabelaDeSimbolos registro : varRegistro) {
                            String nameVariavel = nomeId + '.' + registro.name;
                            
                            if (escopoAtual.possui(nameVariavel)) {
                                Auxiliar.adicionarErroSemantico(id.start, "identificador " + nameVariavel + " ja declarado anteriormente");
                            }
                            else {
                                escopoAtual.inserir(registro);
                                escopoAtual.inserir(nameVariavel, registro.tipo, EntradaTabelaDeSimbolos.Estrutura.VAR);
                            }
                        }
                    }
                    else {
                        escopoAtual.inserir(id.getText(), EntradaTabelaDeSimbolos.Tipos.INT, EntradaTabelaDeSimbolos.Estrutura.VAR);
                    }
                }
            }
        }   
        return super.visitDeclaracao_variavel(ctx);
    }

    // Verifica se o comando retorne é permitido no escopo atual
    @Override
    public Object visitCmdRetorne(CmdRetorneContext ctx) {
        
        if (escoposAninhados.obterEscopoAtual().tipoRetorno == EntradaTabelaDeSimbolos.Tipos.VOID) {
            Auxiliar.adicionarErroSemantico(ctx.start, "comando retorne nao permitido nesse escopo");
        }   return super.visitCmdRetorne(ctx);
    }

    // Verifica se a atribuição é compatível com o tipo da variável
    @Override
    public Object visitCmdAtribuicao(CmdAtribuicaoContext ctx) {
        EntradaTabelaDeSimbolos.Tipos tipoExpressao = Auxiliar.verificarTipo(escoposAninhados, ctx.expressao());
        boolean erro = false;
        String pointerChar = ctx.getText().charAt(0) == '^' ? "^" : "";
        String Var = "";
        int i = 0;
        
        for (TerminalNode id : ctx.identificador().IDENT()) {
            
            if (i++ > 0)
                Var += ".";
            Var += id.getText();
        }
        
        if (tipoExpressao != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
            boolean found = false;
            
            for (TabelaDeSimbolos escopo : escoposAninhados.percorrerEscoposAninhados()) {
                
                if (escopo.possui(Var) && !found)  {
                    found = true;
                    EntradaTabelaDeSimbolos.Tipos tipoVariavel = Auxiliar.verificarTipo(escoposAninhados, Var);
                    Boolean varNumeric = tipoVariavel == EntradaTabelaDeSimbolos.Tipos.REAL || tipoVariavel == EntradaTabelaDeSimbolos.Tipos.INT;
                    Boolean expNumeric = tipoExpressao == EntradaTabelaDeSimbolos.Tipos.REAL || tipoExpressao == EntradaTabelaDeSimbolos.Tipos.INT;
                    if  (!(varNumeric && expNumeric) && tipoVariavel != tipoExpressao && tipoExpressao != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                        erro = true;
                    }
                } 
            }
        } else {
            erro = true;
        }
        
        if (erro) {
            Var = ctx.identificador().getText();
            Auxiliar.adicionarErroSemantico(ctx.identificador().start, "atribuicao nao compativel para " + pointerChar + Var );
        }
        return super.visitCmdAtribuicao(ctx);
    }

    // Verifica se a chamada de função é compatível com a declaração
    @Override
    public Object visitParcela_unario(Parcela_unarioContext ctx) {
        TabelaDeSimbolos escopoAtual = escoposAninhados.obterEscopoAtual();
        
        if (ctx.IDENT() != null) {
            String name = ctx.IDENT().getText();
            
            if (escopoAtual.possui(ctx.IDENT().getText())) {
                List<EntradaTabelaDeSimbolos> params = escopoAtual.obterPropriedadesTipo(name);
                boolean erro = false;
                
                if (params.size() != ctx.expressao().size()) {
                    erro = true;
                } else {
                    
                    for (int i = 0; i < params.size(); i++) {
                        
                        if (params.get(i).tipo != Auxiliar.verificarTipo(escoposAninhados, ctx.expressao().get(i))) {
                            erro = true;
                        }
                    }
                }
                
                if (erro) {
                    Auxiliar.adicionarErroSemantico(ctx.start, "incompatibilidade de parametros na chamada de " + name);
                }
            }
        }
        return super.visitParcela_unario(ctx);
    }
}