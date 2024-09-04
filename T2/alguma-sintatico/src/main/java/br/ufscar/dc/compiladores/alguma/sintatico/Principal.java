package br.ufscar.dc.compiladores.alguma.sintatico;

import java.io.FileWriter;
import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Principal {

    public static void main(String[] args) {

        // Escreve a saída no arquivo de destino (args[1])
        try {
            // Lexer
            CharStream cs = CharStreams.fromFileName(args[0]);
            LALexer lexer = new LALexer(cs);
            
            // Parser
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LAParser parser = new LAParser(tokens);

            // Arquivo de destino
            String filename = args[1];
            FileWriter writer = new FileWriter(filename);
            
            // Classe que implementa a interface ANTLRErrorListener para customizar a saída de erros
            CustomError mcel = new CustomError(writer);

            // Remove o tratamento de erros padrão e adiciona o customizado
            parser.removeErrorListeners();
            parser.addErrorListener(mcel);

            // Inicia o parser na regra inicial
            parser.programa();

            writer.close();

        } catch (IOException ex) {}
    }
}
