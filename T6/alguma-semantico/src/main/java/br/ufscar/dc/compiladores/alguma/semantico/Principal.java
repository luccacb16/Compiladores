package br.ufscar.dc.compiladores.alguma.semantico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.*;

import br.ufscar.dc.compiladores.alguma.semantico.JSONParser.JsonContext;

public class Principal {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Uso: java Principal <arquivo de entrada> <arquivo de saída>");
            return;
        }

        CharStream cs = CharStreams.fromFileName(args[0]);
        JSONLexer lexer = new JSONLexer(cs);

        FileWriter fileWriter = new FileWriter(args[1]);
        ErrorListener errorlistener = new ErrorListener(fileWriter);
        
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorlistener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JSONParser parser = new JSONParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorlistener);

        JsonContext arvore = parser.json();

        if (!errorlistener.erroReportado) {
            JSONSemantico as = new JSONSemantico();
            as.visitJson(arvore);

            if (!JSONSemantico.errosSemanticos.isEmpty()) {
                try (PrintWriter out = new PrintWriter(fileWriter)) {
                    JSONSemantico.errosSemanticos.forEach(out::println);
                    out.println("Fim da compilação");
                    out.close();
                }
            }
        }
    }
}
