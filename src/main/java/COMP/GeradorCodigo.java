package COMP;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;

public final class GeradorCodigo {
    public static final GeradorCodigo INSTANCE = new GeradorCodigo();
    public static GeradorCodigo getInstance() { return INSTANCE; }
    private File arquivo;
    private Formatter out;

    public void inicializa(String file_path){
        arquivo = new File(file_path + ".asm");
        if(arquivo.exists())
                arquivo.delete();
        try {
            arquivo.createNewFile();
            out = new Formatter(arquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void geraLabel(int n){
        out.format("L" + n + "\tNULL%n");
    }

    public void gera(String op) {
        out.format("\t" + op + "%n");
    }

    public void gera(String op, String arg1) {
        out.format("\t" + op + "\t" + arg1 + "%n");
    }

    public void gera(String op, int arg1) {
        out.format("\t" + op + "\t" +  arg1 + "%n");
    }

    public void gera(String op, String arg1, String arg2) {
        out.format("\t" + op + "\t" + arg1 + "," + arg2 + "%n");
    }

    public void gera(String op, int arg1, int arg2) {
        out.format("\t" + op + "\t" + arg1 + "," + arg2 + "%n");
    }

    public void close() { out.close(); }



}
