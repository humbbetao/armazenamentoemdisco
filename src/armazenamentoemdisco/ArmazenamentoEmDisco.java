/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armazenamentoemdisco;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        System.out.println("\n Digite o nome do novo Arquivo: ");
        String nomeDoArquivo = entrada.next();
        File arquivoSql = new File(nomeDoArquivo);//cria o novo arquivo.txt
        File arquivoBinario = new File(nomeDoArquivo.substring(0, nomeDoArquivo.length() - 4) + ".txt");
//        files.add(arquivo);
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
            System.out.println("Deu certo");
//            outputBuffer.close();
//            outputStream.close();

            //escreve o lixo no arquivo;
        } catch (IOException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }

        createEstruturaNoArquivo(bufer, outputBuffer);

    }

    public static void insertRegister() {
        System.out.println("\n Digite o nome do novo registro: ");
    }

    public static void listRegister() {
        System.out.println("\n Listando... ");
    }

    public static void removeRegister() {
        System.out.println("\n Digite o nome do registro a ser removido: ");
    }

    private static void createEstruturaNoArquivo(BufferedReader inputBuffer, BufferedOutputStream outputBuffer) {
        String linha;
        String estrutura;
        String nomeDaTabela;
        ArrayList<String> campos = new ArrayList<>();
        try {
            while ((linha = inputBuffer.readLine()) != null) {
                System.out.println(linha);
                if (linha.contains("CREATE TABLE")) {
                    linha.

                }
                if(linha.contains(linha))
//                if()
//                estrutura +=linha;

            }
        } catch (IOException ex) {
            Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
