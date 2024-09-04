package br.ufscar.dc.compiladores.alguma.sintatico;

import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

// Classe que implementa a interface ANTLRErrorListener para customizar a saída de erros
public class CustomError implements ANTLRErrorListener {

    // Realiza a escrita no arquivo na classe, não mais em Principal como no T1
    private FileWriter file;
    public CustomError(FileWriter file) {
        this.file = file;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        try {
            Token t = (Token) offendingSymbol;
            String nomeToken = LALexer.VOCABULARY.getDisplayName(t.getType());
            
            // Tratamento de erros específicos
            if ("ERRO".equals(nomeToken)) {
                file.write("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado\n");

            } else if ("COMENTARIO_NAO_FECHADO".equals(nomeToken)) {
                file.write("Linha " + t.getLine() + ": comentario nao fechado\n");

            } else if ("CADEIA_NAO_FECHADA".equals(nomeToken)) {
                file.write("Linha " + t.getLine() + ": cadeia literal nao fechada\n");

            } else if ("EOF".equals(nomeToken)) {
                file.write("Linha " + t.getLine() + ": erro sintatico proximo a EOF\n");
            
            // Tratamento de erros genéricos
            } else {
                file.write("Linha " + t.getLine() + ": erro sintatico proximo a " + t.getText() + "\n");
            }
            
            file.write("Fim da compilacao\n");
            file.close(); // Fechar o arquivo evita que seja escrito mais de um erro no arquivo de saída
            
        } catch (IOException ex) {}
    }

    // Não são necessários no T2
    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {}

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {}

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {}
}
