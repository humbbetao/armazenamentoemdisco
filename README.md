# armazenamentoemdisco

Esse projeto tem como proposta a realização de armazenamento em disco para banco de dados 2

Felipe Minorelli
Guilherme Alberton
Humberto Moreira Gonçalves 

Codigo feito em java com auxilio do Netbeans

Ao executar o projeto o Menu Apresentará 4 opções:

Digite 1, 2, 3 ou 4 para selecionar uma opcao abaixo: 
1 - Criar Arquivo;
2 - Inserir Registros;
3 - Listar Registros;
4 - Excluir Registros;
0 - Sair.

Ao selecionar o primeiro, haverá o parser dos dados das tabelas desses arquivos,
criará um novo arquivo para salvar os dados e um metadados para auxiliar na criação, 
mesmo que já haja um arquivo para isso, será substituido;

Ao Selecionar o segundo, pedirá um arquivo.sql e haverá uma consulta no metadados para a correta inserção, 
como não podemos colocar esse dados no arquivo de dados mesmo, jogamos essa opcao de consulta
nos metadados, pegando da string e separando o que realmente vai usar. Usando a classe RandomAccessFile
para manipular o arquivo binário;

Ao Selecionar a terceira, pedirá um arquivo.sql para fazer uma consulta no arquivo criado e saber quais 
dados devem ser retornados do select, sendo essa consulta sendo comparada os metadados para percorrer
o arquivo. Usando a classe RandomAccessFile para manipular o arquivo binário;

Ao Selecionar a quarta, pedirá um arquivo.sql com o delete e usa a mesma idéia do listar para
recuperar os registros, depois de feito isso usa a mesma ideia feita no inserir, que é pegar da string
e separar o que realmente vai usar, usando um primeiro for pra achar e armazenar o registro que deve 
ser apagado e um segundo for pra apagar. Usando a classe RandomAcessFile para manipular o arquivo binário;

E para sair do programa basta digitar 0, que é a quinta e ultima opção.;
