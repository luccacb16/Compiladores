# T6-Compiladores - Validador de JSON

## Integrantes ##

### Lucas Abbiati Pereira, 801572 ###
### Lucca Couto Barberato, 800257 ###

# **Descrição**
Esse trabalho implementa um validador de JSON. O validador identifica erros léxicos, sintáticos e semânticos.

Os erros semânticos são:
1. **Chave vazia**: Uma chave não pode ser ""
2. **Chave duplicada**: Apenas uma chave por escopo
3. **Array com elementos de tipos diferentes**: Um array só deve possuir elementos do mesmo tipo (boolean, string, número e etc)

# **Setup**

Para conseguirmos executar o código é necessário que algumas dependências estejam em uma versão específica.

## Versões ##
+ Java: 1.8
+ Junit: 4.11
+ Antlr: 4.11.1
+ maven-clean-plugin: 3.1.0
+ maven-resources-plugin: 3.0.2
+ maven-compiler-plugin: 3.8.0
+ maven-surefire-plugin: 2.22.1
+ maven-jar-plugin: 3.0.2
+ maven-install-plugin: 2.5.2
+ maven-deploy-plugin: 2.8.2
+ maven-site-plugin: 3.7.1
+ maven-project-info-reports-plugin: 3.0.0

## 1° Opção de execução ##
Para buildar o projeto é necessário usar um comando na raiz do diretório

    mvn clean package

Após executar o comando anterior, será criado um arquivo .jar. Para utilizar o corretor e testar o programa é necessário usar um comando com alguns parâmetros. A entrada é um arquivo .json e a saída é salva em um arquivo .txt

    java -jar <caminho do arquivo json-validator-1.0-SNAPSHOT-jar-with-dependencies> <caminho para o json de entrada> <caminho para o txt de saída>

## 2° Opção de execução ##
Os integrantes do grupo criaram um script com o nome run_testes.sh que facilita o build e a execução do programa. Para rodar é necessário executar o script na raiz do diretório

    ./run_testes.sh
    
Caso haja erro de permissão, é necessário outro comando para conceder permissão ao script

    chmod +x run_testes.sh

## Resultado ##
O resultado final será exibido no arquivo de saída (o mesmo que foi passado como parâmetro para a execução)