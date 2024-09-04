package br.ufscar.dc.compiladores.alguma.semantico;

import java.io.File;
import java.io.PrintWriter;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import java.util.Iterator;

public class Principal {
    public static void main(String[] args) {
        try (PrintWriter p = new PrintWriter(new File(args[1]))) {
            // Lexer
            CharStream cs = CharStreams.fromFileName(args[0]);
            LALexer lexer = new LALexer(cs);

            // Parser
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LAParser parser = new LAParser(tokens);

            // Parsing do programa, obtendo a árvore de contexto
            LAParser.ProgramaContext arvore = parser.programa();

            // Visita a árvore de contexto para realizar a análise semântica
            LASemantico as = new LASemantico();
            as.visitPrograma(arvore);

            // Escreve cada erro no arquivo de saída
            Iterator<String> iterator = Auxiliar.errosSemanticos.iterator();
            while (iterator.hasNext()) {
                String err = iterator.next();
                p.println(err);
            }

            p.println("Fim da compilacao");
            p.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}