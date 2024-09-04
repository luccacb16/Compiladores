package br.ufscar.dc.compiladores.alguma.semantico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import br.ufscar.dc.compiladores.alguma.semantico.JSONParser.JsonContext;

public class Principal {
    public static void main(String args[]) throws IOException {
        // Verifica se os parâmetros de entrada e saída foram fornecidos
        if (args.length < 2) {
            System.err.println("Uso: java Principal <arquivo de entrada> <arquivo de saída>");
            return;
        }

        // Lexer
        CharStream cs = CharStreams.fromFileName(args[0]);
        JSONLexer lexer = new JSONLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Parser
        JSONParser parser = new JSONParser(tokens);
        JsonContext arvore = parser.json();
        
        // Semântico
        JSONSemantico as = new JSONSemantico();
        as.visitJson(arvore);

        // Escreve os erros semânticos no arquivo de saída
        try (FileWriter fw = new FileWriter(args[1]);
             PrintWriter out = new PrintWriter(fw)) {
            if (JSONSemantico.errosSemanticos.isEmpty()) {
                out.println("Nenhum erro semântico encontrado.");
            } else {
                JSONSemantico.errosSemanticos.forEach(out::println);
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de saída: " + e.getMessage());
        }
    }
}