mvn clean package
clear
echo "Rodando teste 1"
java -jar ./target/alguma-semantico-1.0-SNAPSHOT-jar-with-dependencies.jar testes/inputs/1_erro_lexico.json testes/outputs/1_output.txt 
echo "testes/outputs/1_output.txt:" 
cat testes/outputs/1_output.txt
echo ""

echo "Rodando teste 2"
echo "testes/outputs/2_output.txt:" 
cat testes/outputs/2_output.txt
java -jar ./target/alguma-semantico-1.0-SNAPSHOT-jar-with-dependencies.jar testes/inputs/2_erro_sintatico.json testes/outputs/2_output.txt 
echo ""

echo "Rodando teste 3"
echo "testes/outputs/3_output.txt:" 
cat testes/outputs/3_output.txt
java -jar ./target/alguma-semantico-1.0-SNAPSHOT-jar-with-dependencies.jar testes/inputs/3_erro_semantico_1_chave_vazia.json testes/outputs/3_output.txt 
echo ""

echo "Rodando teste 4"
echo "testes/outputs/4_output.txt:" 
cat testes/outputs/4_output.txt
java -jar ./target/alguma-semantico-1.0-SNAPSHOT-jar-with-dependencies.jar testes/inputs/4_erro_semantico_2_chave_duplicada.json testes/outputs/4_output.txt 
echo ""

echo "Rodando teste 5"
echo "testes/outputs/5_output.txt:" 
cat testes/outputs/5_output.txt
java -jar ./target/alguma-semantico-1.0-SNAPSHOT-jar-with-dependencies.jar testes/inputs/5_erro_semantico_3_array.json testes/outputs/5_output.txt 
echo ""

echo "Rodando teste 6"
echo "testes/outputs/6_output.txt:" 
cat testes/outputs/6_output.txt
java -jar ./target/alguma-semantico-1.0-SNAPSHOT-jar-with-dependencies.jar testes/inputs/6_sem_erros.json testes/outputs/6_output.txt