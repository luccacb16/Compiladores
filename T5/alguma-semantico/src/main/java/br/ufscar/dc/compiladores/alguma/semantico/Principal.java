package br.ufscar.dc.compiladores.alguma.semantico;

import br.ufscar.dc.compiladores.alguma.semantico.LAParser.ProgramaContext;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Principal {
    public static void main(String args[]) throws IOException {
        // Lexer
        CharStream cs = CharStreams.fromFileName(args[0]);
        LALexer lexer = new LALexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Parser
        LAParser parser = new LAParser(tokens);
        ProgramaContext arvore = parser.programa();
        
        // Semântico
        LASemantico as = new LASemantico();
        as.visitPrograma(arvore);
        Auxiliar.errosSemanticos.forEach((s) -> System.out.println(s));
        
        // Gerador
        if(Auxiliar.errosSemanticos.isEmpty()) {
            Gerador agc = new Gerador();
            agc.visitPrograma(arvore);
                try(PrintWriter pw = new PrintWriter(args[1])) {
                pw.print(agc.saida.toString());
            }
        }

    }
}