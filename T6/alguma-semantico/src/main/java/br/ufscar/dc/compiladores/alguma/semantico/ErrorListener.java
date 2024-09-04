package br.ufscar.dc.compiladores.alguma.semantico;

import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class ErrorListener extends BaseErrorListener {

    private FileWriter file;
    public boolean erroReportado = false;

    public ErrorListener(FileWriter file) {
        this.file = file;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        if (!erroReportado) {
            try {
                file.write(String.format("Erro de análise na linha %d, posição %d: ", line, charPositionInLine));
                if (offendingSymbol instanceof Token) {
                    Token t = (Token) offendingSymbol;
                    String text = t.getText();
                    if (text.equals("<EOF>")) {
                        file.write("fim de arquivo inesperado.\n");
                    } else {
                        file.write("erro próximo a '" + text + "'.\n");
                    }
                } else {
                    file.write("erro inesperado.\n");
                }

                if (e != null) {
                    if (e instanceof NoViableAltException) {
                        file.write("Não é possível decidir entre as alternativas.\n");
                    } else if (e instanceof InputMismatchException) {
                        file.write("Símbolo não coincide com a expectativa.\n");
                    } else if (e instanceof FailedPredicateException) {
                        file.write("Falha de predicação: " + ((FailedPredicateException) e).getPredicate() + ".\n");
                    }
                }

                file.write("Detalhes: " + msg + "\n");
                file.write("Fim da compilação.\n");
                file.close();
                erroReportado = true;
            } catch (IOException ex) {
                System.err.println("Erro ao escrever no arquivo: " + ex.getMessage());
            }
        }
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        // Opção para reportar ambiguidades, geralmente não necessário em produção
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        // Reportar tentativas de contexto completo
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        // Reportar sensibilidade de contexto
    }
}
