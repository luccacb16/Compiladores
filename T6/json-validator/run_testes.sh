mvn clean package
clear
echo "Rodando teste 1"
java -jar ./target/json-validator-1.0-SNAPSHOT-jar-with-dependencies.jar casos-de-teste/inputs/1_erro_lexico.json casos-de-teste/outputs/1_output.txt 
echo "casos-de-teste/outputs/1_output.txt:" 
cat casos-de-teste/outputs/1_output.txt
echo ""

echo "Rodando teste 2"
echo "casos-de-teste/outputs/2_output.txt:" 
cat casos-de-teste/outputs/2_output.txt
java -jar ./target/json-validator-1.0-SNAPSHOT-jar-with-dependencies.jar casos-de-teste/inputs/2_erro_sintatico.json casos-de-teste/outputs/2_output.txt 
echo ""

echo "Rodando teste 3"
echo "casos-de-teste/outputs/3_output.txt:" 
cat casos-de-teste/outputs/3_output.txt
java -jar ./target/json-validator-1.0-SNAPSHOT-jar-with-dependencies.jar casos-de-teste/inputs/3_erro_semantico_1_chave_vazia.json casos-de-teste/outputs/3_output.txt 
echo ""

echo "Rodando teste 4"
echo "casos-de-teste/outputs/4_output.txt:" 
cat casos-de-teste/outputs/4_output.txt
java -jar ./target/json-validator-1.0-SNAPSHOT-jar-with-dependencies.jar casos-de-teste/inputs/4_erro_semantico_2_chave_duplicada.json casos-de-teste/outputs/4_output.txt 
echo ""

echo "Rodando teste 5"
echo "casos-de-teste/outputs/5_output.txt:" 
cat casos-de-teste/outputs/5_output.txt
java -jar ./target/json-validator-1.0-SNAPSHOT-jar-with-dependencies.jar casos-de-teste/inputs/5_erro_semantico_3_array.json casos-de-teste/outputs/5_output.txt 
echo ""

echo "Rodando teste 6"
echo "casos-de-teste/outputs/6_output.txt:" 
cat casos-de-teste/outputs/6_output.txt
java -jar ./target/json-validator-1.0-SNAPSHOT-jar-with-dependencies.jar casos-de-teste/inputs/6_sem_erros.json casos-de-teste/outputs/6_output.txt