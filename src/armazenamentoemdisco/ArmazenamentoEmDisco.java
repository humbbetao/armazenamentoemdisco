/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armazenamentoemdisco;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author humbe
 */
public class ArmazenamentoEmDisco {

    static ArrayList<File> files;
    static FileInputStream inputStream;
    static FileOutputStream outputStream;
    static BufferedOutputStream outputBuffer;
    static BufferedInputStream inputBuffer;
    static Scanner entrada;

    static String nomeDoArquivo = "createSql.txt";
    static RandomAccessFile raf;//cria o novo arquivo.txt

    public static void main(String[] args) {
        // TODO code application logic here
        entrada = new Scanner(System.in);
        int numero = 0;
        while (true) {
            System.out.println("Digite 1, 2, 3 ou 4 para selecionar uma opcao abaixo: \n"
                    + "1 - Criar Arquivo;\n"
                    + "2 - Inserir Registros;\n"
                    + "3 - Listar Registros;\n"
                    + "4 - Excluir Registros.\n");
            try {
                numero = entrada.nextInt();
            } catch (NumberFormatException ex) {
                System.out.println("Erro de Entrada, Digite novamente\n");
            }
            switch (numero) {
                case 1:
                    createBinaryFile();
                    break;
                case 2:
                    insertRegister();
                    break;
                case 3:
                    listRegister();
                    break;
                case 4:
                    removeRegister();
                    break;
                default:
                    System.out.println("Erro, Entrada Invalida\n");
                    break;
            }
        }
    }

    public static void createBinaryFile() {
//        System.out.println("\n Digite o nome do novo Arquivo: ");
//        String nomeDoArquivo = entrada.next();
        String nomeDoArquivo = "createSql.sql";
        File arquivoSql = new File(nomeDoArquivo);//cria o novo arquivo.txt
        FileReader inputReader = null;
        BufferedReader bufferReader;
        try {
            inputReader = new FileReader(arquivoSql);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
        bufferReader = new BufferedReader(inputReader);
        String linha;
        String estrutura = "";
        ArrayList<String> campos = new ArrayList<>();
        try {
            while ((linha = bufferReader.readLine()) != null) {
                estrutura += linha;
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
        String inputString = estrutura.replaceAll("\t", "");
        inputString = inputString.replaceAll("\n", "");
        String[] createTablesDiferentes = inputString.split("\\;");

        for (String s : createTablesDiferentes) {
//            System.out.println(s + "Nome");
            String[] create = s.split("CREATE TABLE");
            String[] nomeDaTabela = create[1].split("\\(", 2);

            System.out.println(nomeDaTabela[1]);
            nomeDaTabela[1] = nomeDaTabela[1].replaceAll(",", "");
            nomeDaTabela[1] = nomeDaTabela[1].replaceAll("\\)", "");
            String[] itensDaTabela = nomeDaTabela[1].split(" ");
//            System.out.println("itens " + Arrays.toString(itensDaTabela));
            File arquivoMetaDados = new File("Metadados" + nomeDaTabela[0] + ".txt");//cria o novo arquivo.txt
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(arquivoMetaDados);
            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            int j = 0;
            String metadadosDaTabela = "";
            for (String dados : itensDaTabela) {
//                if (j % 2 == 0) {
                metadadosDaTabela += dados;
                metadadosDaTabela += " ";
//                    bufferWriter.
//                }
                j++;

            }
            try {
                bufferWriter.write(metadadosDaTabela);
            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                bufferWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }

            File arquivoBinario = new File(nomeDaTabela[0] + ".txt");
//            System.out.println(nomeDaTabela[0] + "Nome do arquivo");
//         files.add(arquivo);
//nomeDoArquivo.substring(0, nomeDoArquivo.length() - 4) + ".txt"

            FileReader f = null;

            try {
                f = new FileReader(arquivoSql);
                outputStream = new FileOutputStream(arquivoBinario);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader bufer = new BufferedReader(f);
            outputBuffer = new BufferedOutputStream(outputStream);
            byte[] buffer = new byte[2 * 1024]; // cria o arquivo com 2kb
            try {
                outputBuffer.write(buffer);
                outputBuffer.flush();
//                System.out.println("Deu certo");
//            outputBuffer.close();
//            outputStream.close();

                //escreve o lixo no arquivo;
            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }

            int numeroDeItensArmazenados = 0;
            int quantidadeDeItensExcluidos = 0;
            int deslocamentoParaPrimeiroByteLivre = ((2 * 1024) - 6);
//            int delocamentoParaOPrimeiroRegistro = ((2 * 1024));
            System.out.println(deslocamentoParaPrimeiroByteLivre + "esse eh descolamentgo");
            //conta para deslocamento até o ultimo byte;
            try {
                raf = new RandomAccessFile(arquivoBinario, "rw");//cria o novo arquivo.txt

//            byte[] buffer = new byte[2 * 1024]; // cria o arquivo              
                raf.write(new byte[1]);
                raf.write((byte) numeroDeItensArmazenados);
                raf.write(new byte[1]);
                raf.write((byte) quantidadeDeItensExcluidos);
//                raf.write(new byte[1]);
                raf.writeShort(deslocamentoParaPrimeiroByteLivre);
//                raf.write(new byte[1]);
//                raf.writeShort(delocamentoParaOPrimeiroRegistro);
//            raf.flush();
                raf.close();
                //escreve no arquivo o cabecalho
            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void insertRegister() {
        System.out.println("Digite o nome do arquivo de Insert \n");
        // String nomeDoArquivoDeInsert = entrada.next();
        String nomeDoArquivoDeInsert = "insert.sql";

        File arquivoInsert = new File(nomeDoArquivoDeInsert);//cria o novo arquivo.txt
        FileReader inputReader = null;
        try {
            inputReader = new FileReader(arquivoInsert);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader bufferReader = new BufferedReader(inputReader);
        String insert = "";
        try {
            String linha;
            while ((linha = bufferReader.readLine()) != null) {
                insert += linha;
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
        String inputString = insert.replaceAll("\t", "");
        inputString = inputString.replaceAll("\n", "");
        String[] insertInto = inputString.split("\\;");

        for (String s : insertInto) {
            System.out.println("Inserindo " + s);
//            System.out.println(s + "Nome");
            String[] insertStrings = s.split("INSERT INTO");
            String[] campos = insertStrings[1].split("values");
            String valores = campos[1];
//           System.out.println("Campos" + campos[0]);
            String camposNovos = campos[0];
            String[] nomeDaTabela = camposNovos.split("\\(", 2);
//            System.out.println(Arrays.toString(nomeDaTabela));
            String items = valores.replaceAll("\\)", "");
            items = items.replaceAll("\\(", "");
            items = items.trim();
            String[] itens = items.split("\\,");
//            System.out.println("Campos" + Arrays.toString(itens));
            String[] valoresDaTabela = camposNovos.split("\\(", 2);
//            System.out.println(Arrays.toString(nomeDaTabela));
            String itemsDeDados = nomeDaTabela[1].replaceAll("\\)", "");
            itemsDeDados = itemsDeDados.replaceAll("\\(", "");
            itemsDeDados = itemsDeDados.trim();
            String[] itensDeDados = itemsDeDados.split("\\,");
//            System.out.println("Dados " + Arrays.toString(itensDeDados));

            //comecando a inserir no Arquivo;
//            System.out.println("Esse eh o nome " + nomeDaTabela[0]);
            File arquivoDeMetadados = new File("Metadados " + nomeDaTabela[0].trim() + ".txt");//cria o novo arquivo.txt
            FileReader inputReaderMetadados = null;
            try {
                inputReaderMetadados = new FileReader(arquivoDeMetadados);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader bufferReaderMetadados = new BufferedReader(inputReaderMetadados);
            String metadados = "";
            String linha;
            try {
                while ((linha = bufferReaderMetadados.readLine()) != null) {
                    metadados += linha;
                }
//                System.out.println(metadados + "Esse sao os metadados");

            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }

            String[] m = metadados.split(" ");

            String dados = new String();
            int j = 0;
            for (int i = 0; i < m.length; i = i + 2) {
                if (j < itensDeDados.length) {
                    if (m[i] == null ? itensDeDados[j] == null : m[i].equals(itensDeDados[j])) {
                        dados = dados + itens[j] + ",";
                        j++;
                    } else {
                        dados = dados + "0,";
                    }
                } else {
                    dados = dados + "0,";
                }
            }
            int tamanhoDosDados = dados.getBytes().length;
            String[] dadosprontos = dados.split(",");
//             String completa=""
//            for (int i = 0; i < dadosprontos.length; i++) {
//                += dadosprontos;
//            }
//            System.out.println(Arrays.toString(dadosprontos));
            try {

                RandomAccessFile randomAccess = new RandomAccessFile(nomeDaTabela[0] + ".txt", "rw");
                randomAccess.seek(6);
                short tamanhoTotalLivre = 0;
                if (randomAccess.readShort() == 0) {
                    tamanhoTotalLivre = 2048;
                } else {
                    int numeroOriginal = 6;
                    randomAccess.seek(numeroOriginal);
                    while ((tamanhoTotalLivre = randomAccess.readShort()) != 0) {
//                        randomAccess.seek(numeroOriginal + 2);
                        numeroOriginal += 2;
//                        System.out.println("estaaqui");
                    }
                    randomAccess.seek(numeroOriginal -= 2);
                    tamanhoTotalLivre = randomAccess.readShort();
                }
                System.out.println("tamanhododadoslivre" + tamanhoTotalLivre);
                System.out.println("tamanhododados" + tamanhoDosDados);
                System.out.println("Tamanho da Escrita: " + (tamanhoTotalLivre - tamanhoDosDados));
                randomAccess.seek(tamanhoTotalLivre - tamanhoDosDados);
                for (int i = 0; i < dadosprontos.length; i++) {
                    String[] d = dadosprontos[i].split("");
//                    System.out.println(Arrays.toString(d) + "FOi");
                    randomAccess.write((byte) ';');
                    for (int k = 0; k < d.length; k++) {
//                        randomAccess.write(new byte[1]);6
                        randomAccess.write(d[k].getBytes());
                    }

                }
                randomAccess.write((byte) ';');
                String novaDeEscrita = "";
//                System.out.println(" foi");
                randomAccess.seek(0);
                //atualizao o numero de arquivos salvos;
                short numeroDeRegistroSalvos = randomAccess.readShort();
                randomAccess.seek(0);
                randomAccess.writeShort(numeroDeRegistroSalvos++);
                randomAccess.seek(4);
                short deslocamentoParaoPrimeiroByteLivre = randomAccess.readShort();
                randomAccess.seek(4);
                randomAccess.writeShort((tamanhoTotalLivre - tamanhoDosDados));
//                randomAccess.seek(6);
                short numeroOriginal = 6;
                short zero = 0;
                randomAccess.seek(numeroOriginal);
                while (randomAccess.readShort() != zero) {
                    numeroOriginal += 2;
//                    System.out.println("Estaaqui2");
//                    randomAccess.seek(numeroOriginal);
                }
                randomAccess.seek(numeroOriginal -= 2);
//                randomAccess.seek(numeroOriginal);
//                System.out.println(randomAccess.readShort() + "Esse eh o texto");
                short deslocamentoParaoBloco = randomAccess.readShort();
//                randomAccess.seek(numeroOriginal);
                randomAccess.writeShort((tamanhoTotalLivre - tamanhoDosDados));

                randomAccess.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void listRegister() {
        //System.out.println("\n Listando... ");
        entrada = new Scanner(System.in);
        String file = " " + "employee.txt";
        String registros = new String();

        try {
            raf = new RandomAccessFile(file, "r");
            String dado = new String();
            for (int i = 0; i < raf.length(); i++) {
                dado = raf.readLine();
                //   System.out.println(""+dado);
                if (dado != null) {
                    registros = registros + dado;
                }
            }
            //System.out.println("Registros: "+registros);
            String[] reg = registros.split(";");
            File arquivoDeMetadados = new File("Metadados" + file);//cria o novo arquivo.txt
            FileReader inputReaderMetadados = null;
            try {
                inputReaderMetadados = new FileReader(arquivoDeMetadados);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader bufferReaderMetadados = new BufferedReader(inputReaderMetadados);
            String metadados = "";
            String linha;
            try {
                while ((linha = bufferReaderMetadados.readLine()) != null) {
                    metadados += linha;
                }
//                System.out.println(metadados + "Esse sao os metadados");

            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] m = metadados.split(" ");
            int size = m.length / 2;
            String[] meta = new String[size];
            int k = 0;
            for (int i = 0; i < m.length; i = i + 2) {
                meta[k] = m[i];
                k++;
            }

            int printreg = 0;
            for (int i = 1; i < reg.length; i++) {
                if ((printreg % size) == 0) {
                    System.out.printf("\n");
                    System.out.printf("Registro: ");
                }
                System.out.printf("%s: ", meta[printreg % size]);
                System.out.printf("%s ", reg[i]);
                printreg++;
            }
            System.out.printf("\n\n");
        } catch (IOException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeRegister() {
        System.out.println("\n Digite o nome do arquivo com os deletes: ");
        // String nomeDoArquivoDeInsert = entrada.next();
        String nomeDoArquivoDeInsert = "delete.sql";
        File arquivoInsert = new File(nomeDoArquivoDeInsert);//cria o novo arquivo.txt
        FileReader inputReader = null;
        try {
            inputReader = new FileReader(arquivoInsert);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader bufferReader = new BufferedReader(inputReader);
        String insert = "";
        try {
            String linha;
            while ((linha = bufferReader.readLine()) != null) {
                insert += linha;
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }
        String inputString = insert.replaceAll("\t", "");
        inputString = inputString.replaceAll("\n", "");
        String[] insertInto = inputString.split("\\;");

        for (String s : insertInto) {
            String[] insertStrings = s.split("DELETE FROM");
            System.out.println("insert: " + insertStrings[1]);
            String[] campos = insertStrings[1].split("WHERE ");
            String nomeDaTabela = campos[0];

            File arquivoDeMetadados = new File("Metadados " + nomeDaTabela.trim() + ".txt");//cria o novo arquivo.txt
            FileReader inputReaderMetadados = null;
            try {
                inputReaderMetadados = new FileReader(arquivoDeMetadados);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader bufferReaderMetadados = new BufferedReader(inputReaderMetadados);
            String metadados = "";
            String linha;
            try {
                while ((linha = bufferReaderMetadados.readLine()) != null) {
                    metadados += linha;
                }
//                System.out.println(metadados + "Esse sao os metadados");

            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }

            String[] m = metadados.split(" ");
            int size = m.length / 2;
            String[] meta = new String[size];
            int k = 0;
            for (int i = 0; i < m.length; i = i + 2) {
                meta[k] = m[i];
                k++;
            }
            System.out.println("Comparar: " + campos[1]);
            String[] comparar = new String[2];

            if (campos[1].contains("!=")) {
                // System.out.println("Diferente");
                comparar = campos[1].split("!=");
                comparar[0] = comparar[0].trim();
                comparar[1] = comparar[1].trim();
                //System.out.println("Campos: " + comparar[0] + " " + comparar[1]);
                entrada = new Scanner(System.in);
                int c = 0;
                String file = " " + nomeDaTabela.trim() + ".txt";
                for (int i = 0; i < meta.length; i++) {
                    if (comparar[0].contentEquals(meta[i])) {
                        c = i;
                    }
                }
                //System.out.println("C: " + c);
                String registros = new String();
                List<Integer> apagar = new ArrayList<Integer>();
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String dado = new String();
                    for (int i = 0; i < raf.length(); i++) {
                        dado = raf.readLine();
                        //   System.out.println(""+dado);
                        if (dado != null) {
                            registros = registros + dado;
                        }
                    }
                    //System.out.println("Registros: "+registros);
                    String[] reg = registros.split(";");

                    int regtotal = (reg.length - 1) / size;

                    int regAapagar = regtotal - 1;
                    for (int i = 1; i < reg.length; i = i + size) {
                        if (!reg[i + c].contentEquals(comparar[1])) { //if da comparação
                            apagar.add(regAapagar);
                        }
                        regAapagar--;
                    }
                    System.out.println("regs a apagar: " + apagar.toString());
                    int tamReg = size * 2;

                    //DELEÇÃO AQUI \/
                    // apagar > Listas com os registro a serem apagados, 
                    // te fala se é o primeiro, ou segundo etc...
                    //  size > quantidade de campos da tabela
                    listRegister();
                    System.out.println("Registros apagados: " + apagar.size());

                } catch (IOException ex) {
                    Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (campos[1].contains("=")) {
                //System.out.println("Igual");
                comparar = campos[1].split("=");
                comparar[0] = comparar[0].trim();
                comparar[1] = comparar[1].trim();
                // System.out.println("Campos: " + comparar[0] + " " + comparar[1]);
                entrada = new Scanner(System.in);
                int c = 0;
                String file = " " + nomeDaTabela.trim() + ".txt";
                for (int i = 0; i < meta.length; i++) {
                    if (comparar[0].contentEquals(meta[i])) {
                        c = i;
                    }
                }
                //System.out.println("C: " + c);
                String registros = new String();
                List<Integer> apagar = new ArrayList<Integer>();
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String dado = new String();
                    for (int i = 0; i < raf.length(); i++) {
                        dado = raf.readLine();
                        //   System.out.println(""+dado);
                        if (dado != null) {
                            registros = registros + dado;
                        }
                    }
                    //System.out.println("Registros: "+registros);
                    String[] reg = registros.split(";");

                    int regtotal = (reg.length - 1) / size;
                    int regAapagar = regtotal - 1;
                    for (int i = 1; i < reg.length; i = i + size) {
                        if (reg[i + c].contentEquals(comparar[1])) { //if da comparação
                            apagar.add(regAapagar);
                        }
                        regAapagar--;
                    }
                    System.out.println("regs a apagar: " + apagar.toString());

                    int tamReg = size * 2;

                    //DELEÇÃO AQUI \/                    
                    // apagar > Listas com os registro a serem apagados                    
                    //  size, quantidade de campos da tabela                    
                    raf.seek(6);
                    short pos = raf.readShort();
                    for (int i = 0; i < apagar.size(); i++) {
                        for (int j = 0; j < size; j++) {
                            raf.write(new byte[1]);
                        }
                    }
                    listRegister();
                    System.out.println("Registros apagados: " + apagar.size());
                } catch (IOException ex) {
                    Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (campos[1].contains(">")) {
                //System.out.println("Maior");
                comparar = campos[1].split(">");
                comparar[0] = comparar[0].trim();
                comparar[1] = comparar[1].trim();
                //System.out.println("Campos: " + comparar[0] + " " + comparar[1]);
                entrada = new Scanner(System.in);
                int c = 0;
                String file = " " + nomeDaTabela.trim() + ".txt";
                for (int i = 0; i < meta.length; i++) {
                    if (comparar[0].contentEquals(meta[i])) {
                        c = i;
                    }
                }
                //System.out.println("C: " + c);
                String registros = new String();
                List<Integer> apagar = new ArrayList<Integer>();
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String dado = new String();
                    for (int i = 0; i < raf.length(); i++) {
                        dado = raf.readLine();
                        //   System.out.println(""+dado);
                        if (dado != null) {
                            registros = registros + dado;
                        }
                    }
                    //System.out.println("Registros: "+registros);
                    String[] reg = registros.split(";");

                    int regtotal = (reg.length - 1) / size;
                    int regAapagar = regtotal - 1;

                    for (int i = 1; i < reg.length; i = i + size) {
                        Integer lado1 = Integer.parseInt(reg[i + c]);
                        Integer lado2 = Integer.parseInt(comparar[1]);
                        if (lado1 > lado2) { //if da comparação
                            apagar.add(regAapagar);
                        }
                        regAapagar--;
                    }
                    System.out.println("regs a apagar: " + apagar.toString());
                    int tamReg = size * 2;

                    //DELEÇÃO AQUI \/
                    // apagar > Listas com os registro a serem apagados
                    //  size, quantidade de campos da tabela
                    listRegister();
                    System.out.println("Registros apagados: " + apagar.size());
                } catch (IOException ex) {
                    Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (campos[1].contains("<")) {
                //System.out.println("Menor");
                comparar = campos[1].split("<");
                comparar[0] = comparar[0].trim();
                comparar[1] = comparar[1].trim();
                //System.out.println("Campos: " + comparar[0] + " " + comparar[1]);
                entrada = new Scanner(System.in);
                int c = 0;
                String file = " " + nomeDaTabela.trim() + ".txt";
                for (int i = 0; i < meta.length; i++) {
                    if (comparar[0].contentEquals(meta[i])) {
                        c = i;
                    }
                }
                //System.out.println("C: " + c);
                String registros = new String();
                List<Integer> apagar = new ArrayList<Integer>();
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String dado = new String();
                    for (int i = 0; i < raf.length(); i++) {
                        dado = raf.readLine();
                        //   System.out.println(""+dado);
                        if (dado != null) {
                            registros = registros + dado;
                        }
                    }
                    //System.out.println("Registros: "+registros);
                    String[] reg = registros.split(";");

                    int regtotal = (reg.length - 1) / size;
                    int regAapagar = regtotal - 1;

                    for (int i = 1; i < reg.length; i = i + size) {
                        Integer lado1 = Integer.parseInt(reg[i + c]);
                        Integer lado2 = Integer.parseInt(comparar[1]);
                        if (lado1 < lado2) { //if da comparação
                            apagar.add(regAapagar);
                        }
                        regAapagar--;
                    }
                    System.out.println("regs a apagar: " + apagar.toString());
                    int tamReg = size * 2;

                    //DELEÇÃO AQUI \/
                    // apagar > Listas com os registro a serem apagados
                    //  size, quantidade de campos da tabela
                    listRegister();
                    System.out.println("Registros apagados: " + apagar.size());

                } catch (IOException ex) {
                    Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }

}
