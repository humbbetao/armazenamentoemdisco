/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armazenamentoemdisco;

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
            int deslocamentoParaPrimeiroByteLivre = (byte) (((2 * 1024) / 8) - 6);
            int delocamentoParaOPrimeiroRegistro = (byte) (((2 * 1024) / 8) - 8);
            //conta para deslocamento até o ultimo byte;
            try {
                raf = new RandomAccessFile(arquivoBinario, "rw");//cria o novo arquivo.txt

//            byte[] buffer = new byte[2 * 1024]; // cria o arquivo 
                raf.write(new byte[1]);
                raf.write((byte) numeroDeItensArmazenados);
                raf.write(new byte[1]);
                raf.write((byte) quantidadeDeItensExcluidos);
                raf.write(new byte[1]);
                raf.write((byte) deslocamentoParaPrimeiroByteLivre);
                raf.write(new byte[1]);
                raf.write((byte) delocamentoParaOPrimeiroRegistro);
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
        String nomeDoArquivoDeInsert = entrada.next();

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
        String[] createTablesDiferentes = inputString.split("\\;");

        for (String s : createTablesDiferentes) {
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
            System.out.println(Arrays.toString(itens));
            String[] valoresDaTabela = camposNovos.split("\\(", 2);
//            System.out.println(Arrays.toString(nomeDaTabela));
            String itemsDeDados = nomeDaTabela[1].replaceAll("\\)", "");
            itemsDeDados = itemsDeDados.replaceAll("\\(", "");
            itemsDeDados = itemsDeDados.trim();
            String[] itensDeDados = itemsDeDados.split("\\,");
            System.out.println(Arrays.toString(itensDeDados));

            //comecando a inserir no Arquivo;
            System.out.println("Esse eh o nome " + nomeDaTabela[0]);
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
                System.out.println(metadados + "Esse sao os metadados");

            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                RandomAccessFile randomAccess = new RandomAccessFile(nomeDaTabela[0] + ".txt", "rw");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] m = metadados.split(" ");
//            for (String v : valoresDaTabela) {
//                if(v.equals())
//            }

//            byte[] DadosCompletosParaEscrever = ;
        }

    }

    public static void listRegister() {
        System.out.println("\n Listando... ");
    }

    public static void removeRegister() {
        System.out.println("\n Digite o nome do registro a ser removido: ");
    }

}
