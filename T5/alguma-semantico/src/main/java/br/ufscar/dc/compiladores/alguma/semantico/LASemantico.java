package br.ufscar.dc.compiladores.alguma.semantico;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;

import br.ufscar.dc.compiladores.alguma.semantico.LAParser.CmdAtribuicaoContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Declaracao_constanteContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Declaracao_globalContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Declaracao_tipoContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Declaracao_variavelContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.IdentificadorContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.ProgramaContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Tipo_basico_identContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.VariavelContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.ParametroContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.CmdRetorneContext;
import br.ufscar.dc.compiladores.alguma.semantico.LAParser.Parcela_unarioContext;

public class LASemantico extends LABaseVisitor<Object> {
    Escopo escopos = new Escopo(EntradaTabelaDeSimbolos.Tipos.VOID);

    @Override
    public Object visitPrograma(ProgramaContext ctx) {
        return super.visitPrograma(ctx);
    }
    
    // Verifica e insere a declaração de uma constante na tabela de símbolos
    @Override
    public Object visitDeclaracao_constante(Declaracao_constanteContext ctx) {
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        if (escopoAtual.possui(ctx.IDENT().getText())) {
            Auxiliar.adicionarErroSemantico(ctx.start, "constante " + ctx.IDENT().getText()
                    + " já declarado anteriormente");
        } else {
            EntradaTabelaDeSimbolos.Tipos tipo = EntradaTabelaDeSimbolos.Tipos.INT;
            EntradaTabelaDeSimbolos.Tipos aux = Auxiliar.getTipo(ctx.tipo_basico().getText());
            if (aux != null) {
                tipo = aux;
            }
            escopoAtual.inserir(ctx.IDENT().getText(), tipo, EntradaTabelaDeSimbolos.Estrutura.CONST);
        }

        return super.visitDeclaracao_constante(ctx);
    }

    // Verifica e insere a declaração de um tipo na tabela de símbolos
    @Override
    public Object visitDeclaracao_tipo(Declaracao_tipoContext ctx) {
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        if (escopoAtual.possui(ctx.IDENT().getText())) {
            Auxiliar.adicionarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText()
                    + " declarado duas vezes num mesmo escopo");
        } else {
            EntradaTabelaDeSimbolos.Tipos tipo = Auxiliar.getTipo(ctx.tipo().getText());
            if (tipo != null) {
                escopoAtual.inserir(ctx.IDENT().getText(), tipo, EntradaTabelaDeSimbolos.Estrutura.TIPO);
            } else if (ctx.tipo().registro() != null) {
                ArrayList<EntradaTabelaDeSimbolos> varReg = new ArrayList<>();
                for (VariavelContext va : ctx.tipo().registro().variavel()) {
                    EntradaTabelaDeSimbolos.Tipos tipoReg = Auxiliar.getTipo(va.tipo().getText());
                    for (IdentificadorContext id2 : va.identificador()) {
                        varReg.add(new EntradaTabelaDeSimbolos(id2.getText(), tipoReg, EntradaTabelaDeSimbolos.Estrutura.TIPO));
                    }
                }

                if (escopoAtual.possui(ctx.IDENT().getText())) {
                    Auxiliar.adicionarErroSemantico(ctx.start, "identificador " + ctx.IDENT().getText()
                            + " já declarado anteriormente");
                } else {
                    escopoAtual.inserir(ctx.IDENT().getText(), EntradaTabelaDeSimbolos.Tipos.REG, EntradaTabelaDeSimbolos.Estrutura.TIPO);
                }

                for (EntradaTabelaDeSimbolos re : varReg) {
                    String nameVar = ctx.IDENT().getText() + '.' + re.getNome();
                    if (escopoAtual.possui(nameVar)) {
                        Auxiliar.adicionarErroSemantico(ctx.start, "identificador " + nameVar
                                + " já declarado anteriormente");
                    } else {
                        escopoAtual.inserir(re);
                        escopoAtual.inserir(ctx.IDENT().getText(), re);
                    }
                }
            }
            EntradaTabelaDeSimbolos.Tipos t = Auxiliar.getTipo(ctx.tipo().getText());
            escopoAtual.inserir(ctx.IDENT().getText(), t, EntradaTabelaDeSimbolos.Estrutura.TIPO);
        }
        return super.visitDeclaracao_tipo(ctx);
    }

    // Verifica e insere a declaração de uma variável na tabela de símbolos
    @Override
    public Object visitDeclaracao_variavel(Declaracao_variavelContext ctx) {
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        for (IdentificadorContext id : ctx.variavel().identificador()) {
            String nomeId = "";
            int i = 0;
            for (TerminalNode ident : id.IDENT()) {
                if (i++ > 0)
                    nomeId += ".";
                nomeId += ident.getText();
            }
            if (escopoAtual.possui(nomeId)) {
                Auxiliar.adicionarErroSemantico(id.start, "identificador " + nomeId
                        + " já declarado anteriormente");
            } else {
                EntradaTabelaDeSimbolos.Tipos tipo = Auxiliar.getTipo(ctx.variavel().tipo().getText());
                if (tipo != null) {
                    escopoAtual.inserir(nomeId, tipo, EntradaTabelaDeSimbolos.Estrutura.VAR);
                } else {
                    TerminalNode identTipo = ctx.variavel().tipo() != null
                            && ctx.variavel().tipo().tipo_estendido() != null
                            && ctx.variavel().tipo().tipo_estendido().tipo_basico_ident() != null
                            && ctx.variavel().tipo().tipo_estendido().tipo_basico_ident().IDENT() != null
                                    ? ctx.variavel().tipo().tipo_estendido().tipo_basico_ident().IDENT()
                                    : null;
                    if (identTipo != null) {
                        ArrayList<EntradaTabelaDeSimbolos> regVars = null;
                        boolean found = false;
                        for (TabelaDeSimbolos t : escopos.percorrerEscoposAninhados()) {
                            if (!found) {
                                if (t.possui(identTipo.getText())) {
                                    regVars = t.obterPropriedadesTipo(identTipo.getText());
                                    found = true;
                                }
                            }
                        }
                        if (escopoAtual.possui(nomeId)) {
                            Auxiliar.adicionarErroSemantico(id.start, "identificador " + nomeId
                                    + " já declarado anteriormente");
                        } else {
                            escopoAtual.inserir(nomeId, EntradaTabelaDeSimbolos.Tipos.REG, EntradaTabelaDeSimbolos.Estrutura.VAR);
                            for (EntradaTabelaDeSimbolos s : regVars) {
                                escopoAtual.inserir(nomeId + "." + s.getNome(), s.getTipo(), EntradaTabelaDeSimbolos.Estrutura.VAR);
                            }
                        }
                    } else if (ctx.variavel().tipo().registro() != null) {
                        ArrayList<EntradaTabelaDeSimbolos> varReg = new ArrayList<>();
                        for (VariavelContext va : ctx.variavel().tipo().registro().variavel()) {
                            EntradaTabelaDeSimbolos.Tipos tipoReg = Auxiliar.getTipo(va.tipo().getText());
                            for (IdentificadorContext id2 : va.identificador()) {
                                varReg.add(new EntradaTabelaDeSimbolos(id2.getText(), tipoReg, EntradaTabelaDeSimbolos.Estrutura.VAR));
                            }
                        }
                        escopoAtual.inserir(nomeId, EntradaTabelaDeSimbolos.Tipos.REG, EntradaTabelaDeSimbolos.Estrutura.VAR);

                        for (EntradaTabelaDeSimbolos re : varReg) {
                            String nameVar = nomeId + '.' + re.getNome();
                            if (escopoAtual.possui(nameVar)) {
                                Auxiliar.adicionarErroSemantico(id.start, "identificador " + nameVar
                                        + " já declarado anteriormente");
                            } else {
                                escopoAtual.inserir(re);
                                escopoAtual.inserir(nameVar, re.getTipo(), EntradaTabelaDeSimbolos.Estrutura.VAR);
                            }
                        }

                    } else { // tipo registro estendido
                        escopoAtual.inserir(id.getText(), EntradaTabelaDeSimbolos.Tipos.INT, EntradaTabelaDeSimbolos.Estrutura.VAR);
                    }
                }
            }
        }
        return super.visitDeclaracao_variavel(ctx);
    }

    // Verifica e insere a declaração global (função ou procedimento) na tabela de símbolos
    @Override
    public Object visitDeclaracao_global(Declaracao_globalContext ctx) {
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        Object ret;
        if (escopoAtual.possui(ctx.IDENT().getText())) {
            Auxiliar.adicionarErroSemantico(ctx.start, ctx.IDENT().getText()
                    + " já declarado anteriormente");
            ret = super.visitDeclaracao_global(ctx);
        } else {
            EntradaTabelaDeSimbolos.Tipos returnTypeFunc = EntradaTabelaDeSimbolos.Tipos.VOID;
            if (ctx.getText().startsWith("funcao")) {
                returnTypeFunc = Auxiliar.getTipo(ctx.tipo_estendido().getText());
                escopoAtual.inserir(ctx.IDENT().getText(), returnTypeFunc, EntradaTabelaDeSimbolos.Estrutura.FUNC);
            } else {
                returnTypeFunc = EntradaTabelaDeSimbolos.Tipos.VOID;
                escopoAtual.inserir(ctx.IDENT().getText(), returnTypeFunc, EntradaTabelaDeSimbolos.Estrutura.PROC);
            }
            escopos.criarNovoEscopo(returnTypeFunc);
            TabelaDeSimbolos escopoAntigo = escopoAtual;
            escopoAtual = escopos.obterEscopoAtual();
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
                            Auxiliar.adicionarErroSemantico(id.start, "identificador " + nomeId
                                    + " já declarado anteriormente");
                        } else {
                            EntradaTabelaDeSimbolos.Tipos tipo = Auxiliar.getTipo(p.tipo_estendido().getText());
                            if (tipo != null) {
                                EntradaTabelaDeSimbolos in = new EntradaTabelaDeSimbolos(nomeId, tipo, EntradaTabelaDeSimbolos.Estrutura.VAR);
                                escopoAtual.inserir(in);
                                escopoAntigo.inserir(ctx.IDENT().getText(), in);
                            } else {
                                TerminalNode identTipo = p.tipo_estendido().tipo_basico_ident() != null
                                        && p.tipo_estendido().tipo_basico_ident().IDENT() != null
                                                ? p.tipo_estendido().tipo_basico_ident().IDENT()
                                                : null;
                                if (identTipo != null) {
                                    ArrayList<EntradaTabelaDeSimbolos> regVars = null;
                                    boolean found = false;
                                    for (TabelaDeSimbolos t : escopos.percorrerEscoposAninhados()) {
                                        if (!found) {
                                            if (t.possui(identTipo.getText())) {
                                                regVars = t.obterPropriedadesTipo(identTipo.getText());
                                                found = true;
                                            }
                                        }
                                    }
                                    if (escopoAtual.possui(nomeId)) {
                                        Auxiliar.adicionarErroSemantico(id.start, "identificador " + nomeId
                                                + " já declarado anteriormente");
                                    } else {
                                        EntradaTabelaDeSimbolos in = new EntradaTabelaDeSimbolos(nomeId, EntradaTabelaDeSimbolos.Tipos.REG, EntradaTabelaDeSimbolos.Estrutura.VAR);
                                        escopoAtual.inserir(in);
                                        escopoAntigo.inserir(ctx.IDENT().getText(), in);

                                        for (EntradaTabelaDeSimbolos s : regVars) {
                                            escopoAtual.inserir(nomeId + "." + s.getNome(), s.getTipo(), EntradaTabelaDeSimbolos.Estrutura.VAR);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ret = super.visitDeclaracao_global(ctx);
            escopos.abandonarEscopo();

        }

        return ret;
    }

    // Verifica o tipo básico de uma identificação
    @Override
    public Object visitTipo_basico_ident(Tipo_basico_identContext ctx) {
        if (ctx.IDENT() != null) {
            boolean exists = false;
            for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
                if (escopo.possui(ctx.IDENT().getText())) {
                    exists = true;
                }
            }
            if (!exists) {
                Auxiliar.adicionarErroSemantico(ctx.start, "tipo " + ctx.IDENT().getText()
                        + " não declarado");
            }
        }
        return super.visitTipo_basico_ident(ctx);
    }

    // Verifica se o identificador foi declarado corretamente
    @Override
    public Object visitIdentificador(IdentificadorContext ctx) {
        String nomeVar = "";
        int i = 0;
        for (TerminalNode id : ctx.IDENT()) {
            if (i++ > 0)
                nomeVar += ".";
            nomeVar += id.getText();
        }
        boolean erro = true;
        for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
            if (escopo.possui(nomeVar)) {
                erro = false;
            }
        }
        if (erro)
            Auxiliar.adicionarErroSemantico(ctx.start, "identificador " + nomeVar + " não declarado");
        return super.visitIdentificador(ctx);
    }

    // Verifica a compatibilidade do tipo de atribuição
    @Override
    public Object visitCmdAtribuicao(CmdAtribuicaoContext ctx) {
        EntradaTabelaDeSimbolos.Tipos tipoExpressao = Auxiliar.verificarTipo(escopos, ctx.expressao());
        boolean error = false;
        String pointerChar = ctx.getText().charAt(0) == '^' ? "^" : "";
        String nomeVar = "";
        int i = 0;
        for (TerminalNode id : ctx.identificador().IDENT()) {
            if (i++ > 0)
                nomeVar += ".";
            nomeVar += id.getText();
        }
        if (tipoExpressao != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
            boolean found = false;
            for (TabelaDeSimbolos escopo : escopos.percorrerEscoposAninhados()) {
                if (escopo.possui(nomeVar) && !found) {
                    found = true;
                    EntradaTabelaDeSimbolos.Tipos tipoVariavel = Auxiliar.verificarTipo(escopos, nomeVar);
                    Boolean varNumeric = tipoVariavel == EntradaTabelaDeSimbolos.Tipos.REAL || tipoVariavel == EntradaTabelaDeSimbolos.Tipos.INT;
                    Boolean expNumeric = tipoExpressao == EntradaTabelaDeSimbolos.Tipos.REAL || tipoExpressao == EntradaTabelaDeSimbolos.Tipos.INT;
                    if (!(varNumeric && expNumeric) && tipoVariavel != tipoExpressao && tipoExpressao != EntradaTabelaDeSimbolos.Tipos.INVALIDO) {
                        error = true;
                    }
                }
            }
        } else {
            error = true;
        }

        if (error) {
            nomeVar = ctx.identificador().getText();
            Auxiliar.adicionarErroSemantico(ctx.identificador().start, "atribuição não compatível para " + pointerChar + nomeVar);
        }

        return super.visitCmdAtribuicao(ctx);
    }

    // Verifica se o comando 'retorne' é permitido no escopo atual
    @Override
    public Object visitCmdRetorne(CmdRetorneContext ctx) {
        if (escopos.obterEscopoAtual().tipoRetorno == EntradaTabelaDeSimbolos.Tipos.VOID) {
            Auxiliar.adicionarErroSemantico(ctx.start, "comando retorne não permitido nesse escopo");
        }

        return super.visitCmdRetorne(ctx);
    }

    // Verifica se a chamada de função ou procedimento é compatível com os parâmetros
    @Override
    public Object visitParcela_unario(Parcela_unarioContext ctx) {
        TabelaDeSimbolos escopoAtual = escopos.obterEscopoAtual();
        if (ctx.IDENT() != null) {
            String name = ctx.IDENT().getText();
            if (escopoAtual.possui(ctx.IDENT().getText())) {
                List<EntradaTabelaDeSimbolos> params = escopoAtual.obterPropriedadesTipo(name);
                boolean error = false;
                if (params.size() != ctx.expressao().size()) {
                    error = true;
                } else {
                    for (int i = 0; i < params.size(); i++) {
                        if (params.get(i).getTipo() != Auxiliar.verificarTipo(escopos, ctx.expressao().get(i))) {
                            error = true;
                        }
                    }
                }
                if (error) {
                    Auxiliar.adicionarErroSemantico(ctx.start, "incompatibilidade de parâmetros na chamada de " + name);
                }
            }
        }

        return super.visitParcela_unario(ctx);
    }
}