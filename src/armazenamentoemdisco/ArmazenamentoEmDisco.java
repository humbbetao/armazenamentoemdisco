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
        System.out.println("\n Digite o nome do novo Arquivo: ");
        String nomeDoArquivo = entrada.next();
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
            String[] create = s.split("CREATE TABLE");
            String[] nomeDaTabela = create[1].split("\\(", 2);
            nomeDaTabela[1] = nomeDaTabela[1].replaceAll(",", "");
            nomeDaTabela[1] = nomeDaTabela[1].replaceAll("\\)", "");
            String[] itensDaTabela = nomeDaTabela[1].split(" ");
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
                metadadosDaTabela += dados;
                metadadosDaTabela += " ";
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
            } catch (IOException ex) {
                Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
            }
            int numeroDeItensArmazenados = 0;
            int quantidadeDeItensExcluidos = 0;
            int deslocamentoParaPrimeiroByteLivre = ((2 * 1024) - 6);
            try {
                raf = new RandomAccessFile(arquivoBinario, "rw");//cria o novo arquivo.txt            
                raf.write(new byte[1]);
                raf.write((byte) numeroDeItensArmazenados);
                raf.write(new byte[1]);
                raf.write((byte) quantidadeDeItensExcluidos);
                raf.writeShort(deslocamentoParaPrimeiroByteLivre);
                raf.close();
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
        String[] insertInto = inputString.split("\\;");
        for (String s : insertInto) {
            String[] insertStrings = s.split("INSERT INTO");
            String[] campos = insertStrings[1].split("values");
            String valores = campos[1];
            String camposNovos = campos[0];
            String[] nomeDaTabela = camposNovos.split("\\(", 2);
            String items = valores.replaceAll("\\)", "");
            items = items.replaceAll("\\(", "");
            items = items.trim();
            String[] itens = items.split("\\,");
            String[] valoresDaTabela = camposNovos.split("\\(", 2);
            String itemsDeDados = nomeDaTabela[1].replaceAll("\\)", "");
            itemsDeDados = itemsDeDados.replaceAll("\\(", "");
            itemsDeDados = itemsDeDados.trim();
            String[] itensDeDados = itemsDeDados.split("\\,");
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
                        numeroOriginal += 2;
                    }
                    randomAccess.seek(numeroOriginal -= 2);
                    tamanhoTotalLivre = randomAccess.readShort();
                }
                randomAccess.seek(tamanhoTotalLivre - tamanhoDosDados);
                for (int i = 0; i < dadosprontos.length; i++) {
                    String[] d = dadosprontos[i].split("");
                    randomAccess.write((byte) ';');
                    for (int k = 0; k < d.length; k++) {
                        randomAccess.write(d[k].getBytes());
                    }

                }
                String novaDeEscrita = "";
                randomAccess.seek(0);
                short numeroDeRegistroSalvos = randomAccess.readShort();
                randomAccess.seek(0);
                numeroDeRegistroSalvos=(short) (numeroDeRegistroSalvos+1);
                randomAccess.writeShort(numeroDeRegistroSalvos);
                randomAccess.seek(4);
                short deslocamentoParaoPrimeiroByteLivre = randomAccess.readShort();
                randomAccess.seek(4);
                randomAccess.writeShort((tamanhoTotalLivre - tamanhoDosDados));
                short numeroOriginal = 6;
                short zero = 0;
                randomAccess.seek(numeroOriginal);
                while (randomAccess.readShort() != zero) {
                    numeroOriginal += 2;
                }
                randomAccess.seek(numeroOriginal -= 2);
                short deslocamentoParaoBloco = randomAccess.readShort();
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
        System.out.println("\nDigite o arquivo a ser listado\n");
        entrada = new Scanner(System.in);
        String file = " " + entrada.next();
        String registros = new String();
        try {
            raf = new RandomAccessFile(file, "r");
            String dado = new String();
            for (int i = 0; i < raf.length(); i++) {
                dado = raf.readLine();

                if (dado != null) {
                    registros = registros + dado;
                }
            }
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
        System.out.println("\nDigite o nome do arquivo com os deletes: ");
        String nomeDoArquivoDeInsert = entrada.next();
        int totalapagados = 0;
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
            String[] comparar = new String[2];
            if (campos[1].contains("!=")) {
                comparar = campos[1].split("!=");
                comparar[0] = comparar[0].trim();
                comparar[1] = comparar[1].trim();
                entrada = new Scanner(System.in);
                int c = 0;
                String file = " " + nomeDaTabela.trim() + ".txt";
                for (int i = 0; i < meta.length; i++) {
                    if (comparar[0].contentEquals(meta[i])) {
                        c = i;
                    }
                }
                String registros = new String();
                List<Integer> apagar = new ArrayList<Integer>();
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String dado = new String();
                    for (int i = 0; i < raf.length(); i++) {
                        dado = raf.readLine();
                        if (dado != null) {
                            registros = registros + dado;
                        }
                    }
                    String[] reg = registros.split(";");
                    int regtotal = (reg.length - 1) / size;
                    int regAapagar = regtotal - 1;
                    for (int i = 1; i < reg.length; i = i + size) {
                        if (!reg[i + c].contentEquals(comparar[1])) { //if da comparação
                            apagar.add(regAapagar);
                        }
                        regAapagar--;
                    }
                    for (int i = 0; i < apagar.size(); i++) {
                        raf.seek(6 + (apagar.get(i)) * 2);
                        short ini = raf.readShort();
                        raf.seek(6 + ((apagar.get(i)) * 2) - 2);
                        short fim = raf.readShort();
                        if (ini > fim) {
                            fim = 2048;
                            int x = 0;
                            for (int j = 0; j < regtotal; j++) {
                                raf.seek(6 + (x + 2));
                                short y = raf.readShort();
                                raf.seek(6 + x);
                                raf.writeShort(y);
                                x += 2;
                            }
                            raf.seek(6 + x);
                            raf.write(new byte[4]);
                        }
                        for (int j = ini; j < fim; j++) {
                            raf.seek(j);
                            raf.write(new byte[1]);
                        }

                    }
                    raf.seek(2);
                    short d = raf.readShort();
                    d = (short) (d + apagar.size());
                    raf.seek(2);
                    raf.writeShort(d);
                    totalapagados = totalapagados + apagar.size();
                } catch (IOException ex) {
                    Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (campos[1].contains("=")) {
                comparar = campos[1].split("=");
                comparar[0] = comparar[0].trim();
                comparar[1] = comparar[1].trim();
                entrada = new Scanner(System.in);
                int c = 0;
                String file = " " + nomeDaTabela.trim() + ".txt";
                for (int i = 0; i < meta.length; i++) {
                    if (comparar[0].contentEquals(meta[i])) {
                        c = i;
                    }
                }
                String registros = new String();
                List<Integer> apagar = new ArrayList<Integer>();
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String dado = new String();
                    for (int i = 0; i < raf.length(); i++) {
                        dado = raf.readLine();
                        if (dado != null) {
                            registros = registros + dado;
                        }
                    }
                    String[] reg = registros.split(";");
                    int regtotal = (reg.length - 1) / size;
                    int regAapagar = regtotal - 1;
                    for (int i = 1; i < reg.length; i = i + size) {
                        if (reg[i + c].contentEquals(comparar[1])) { //if da comparação
                            apagar.add(regAapagar);
                        }
                        regAapagar--;
                    }
                    for (int i = 0; i < apagar.size(); i++) {
                        raf.seek(6 + (apagar.get(i)) * 2);
                        short ini = raf.readShort();
                        raf.seek(6 + ((apagar.get(i)) * 2) - 2);
                        short fim = raf.readShort();
                        if (ini > fim) {
                            fim = 2048;
                            int x = 0;
                            for (int j = 0; j < regtotal; j++) {
                                raf.seek(6 + (x + 2));
                                short y = raf.readShort();
                                raf.seek(6 + x);
                                raf.writeShort(y);
                                x += 2;
                            }
                            raf.seek(6 + x);
                            raf.write(new byte[4]);
                        }
                        for (int j = ini; j < fim; j++) {
                            raf.seek(j);
                            raf.write(new byte[1]);
                        }
                    }
                    raf.seek(2);
                    short d = raf.readShort();
                    d = (short) (d + apagar.size());
                    raf.seek(2);
                    raf.writeShort(d);
                    totalapagados = totalapagados + apagar.size();
                } catch (IOException ex) {
                    Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (campos[1].contains(">")) {
                comparar = campos[1].split(">");
                comparar[0] = comparar[0].trim();
                comparar[1] = comparar[1].trim();
                entrada = new Scanner(System.in);
                int c = 0;
                String file = " " + nomeDaTabela.trim() + ".txt";
                for (int i = 0; i < meta.length; i++) {
                    if (comparar[0].contentEquals(meta[i])) {
                        c = i;
                    }
                }
                String registros = new String();
                List<Integer> apagar = new ArrayList<Integer>();
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String dado = new String();
                    for (int i = 0; i < raf.length(); i++) {
                        dado = raf.readLine();
                        if (dado != null) {
                            registros = registros + dado;
                        }
                    }
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
                    for (int i = 0; i < apagar.size(); i++) {
                        raf.seek(6 + (apagar.get(i)) * 2);
                        short ini = raf.readShort();
                        raf.seek(6 + ((apagar.get(i)) * 2) - 2);
                        short fim = raf.readShort();
                        if (ini > fim) {
                            fim = 2048;
                            int x = 0;
                            for (int j = 0; j < regtotal; j++) {
                                raf.seek(6 + (x + 2));
                                short y = raf.readShort();
                                raf.seek(6 + x);
                                raf.writeShort(y);
                                x += 2;
                            }
                            raf.seek(6 + x);
                            raf.write(new byte[4]);
                        }
                        for (int j = ini; j < fim; j++) {
                            raf.seek(j);
                            raf.write(new byte[1]);
                        }
                    }
                    raf.seek(2);
                    short d = raf.readShort();
                    d = (short) (d + apagar.size());
                    raf.seek(2);
                    raf.writeShort(d);
                    totalapagados = totalapagados + apagar.size();
                    totalapagados = totalapagados + apagar.size();
                } catch (IOException ex) {
                    Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (campos[1].contains("<")) {
                comparar = campos[1].split("<");
                comparar[0] = comparar[0].trim();
                comparar[1] = comparar[1].trim();
                entrada = new Scanner(System.in);
                int c = 0;
                String file = " " + nomeDaTabela.trim() + ".txt";
                for (int i = 0; i < meta.length; i++) {
                    if (comparar[0].contentEquals(meta[i])) {
                        c = i;
                    }
                }
                String registros = new String();
                List<Integer> apagar = new ArrayList<Integer>();
                try {
                    raf = new RandomAccessFile(file, "rw");
                    String dado = new String();
                    for (int i = 0; i < raf.length(); i++) {
                        dado = raf.readLine();
                        if (dado != null) {
                            registros = registros + dado;
                        }
                    }
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
                    for (int i = 0; i < apagar.size(); i++) {
                        raf.seek(6 + (apagar.get(i)) * 2);
                        short ini = raf.readShort();
                        raf.seek(6 + ((apagar.get(i)) * 2) - 2);
                        short fim = raf.readShort();
                        if (ini > fim) {
                            fim = 2048;
                            int x = 0;
                            for (int j = 0; j < regtotal; j++) {
                                raf.seek(6 + (x + 2));
                                short y = raf.readShort();
                                raf.seek(6 + x);
                                raf.writeShort(y);
                                x += 2;
                            }
                            raf.seek(6 + x);
                            raf.write(new byte[4]);
                        }
                        for (int j = ini; j < fim; j++) {
                            raf.seek(j);
                            raf.write(new byte[1]);
                        }
                    }
                    raf.seek(2);
                    short d = raf.readShort();
                    d = (short) (d + apagar.size());
                    raf.seek(2);
                    raf.writeShort(d);
                    totalapagados = totalapagados + apagar.size();
                } catch (IOException ex) {
                    Logger.getLogger(ArmazenamentoEmDisco.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Total de registros apagados: " + totalapagados + "\n");
    }

}
