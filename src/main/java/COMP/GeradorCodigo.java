package COMP;

import DATATYPES.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public void geraExpressao(ArrayList<Tok> expressao) {
        for(Tok t : expressao){
            if(t.getSimbolo() == Tok.s.identificador){
                Simbolo s = Semantico.getInstance().buscaSimbolo(t.getLexema());
                if(s!=null && s instanceof Variavel){
                    GeradorCodigo.getInstance().gera("LDV", ((Variavel)s).getEndereco());
                }
                if(s!=null && s instanceof Funcao){
                    GeradorCodigo.getInstance().gera("CALL", "L"+((Funcao)s).getRotulo());
                    GeradorCodigo.getInstance().gera("LDV", 0);
                }
            }
            else if(t.getSimbolo()==Tok.s.numero)
                GeradorCodigo.getInstance().gera("LDC", t.getLexema());
            else if(t.getSimbolo()==Tok.s.verdadeiro)
                GeradorCodigo.getInstance().gera("LDC", 1);
            else if(t.getSimbolo()==Tok.s.falso)
                GeradorCodigo.getInstance().gera("LDC", 0);
            else if(t.getSimbolo()==Tok.s.ou)
                GeradorCodigo.getInstance().gera("OR");
            else if(t.getSimbolo()==Tok.s.e)
                GeradorCodigo.getInstance().gera("AND");
            else if(t.getSimbolo()==Tok.s.nao)
                GeradorCodigo.getInstance().gera("NEG");
            else if(t.getSimbolo()==Tok.s.menor)
                GeradorCodigo.getInstance().gera("CME");
            else if(t.getSimbolo()==Tok.s.menorig)
                GeradorCodigo.getInstance().gera("CMEQ");
            else if(t.getSimbolo()==Tok.s.maior)
                GeradorCodigo.getInstance().gera("CMA");
            else if(t.getSimbolo()==Tok.s.maiorig)
                GeradorCodigo.getInstance().gera("CMAQ");
            else if(t.getSimbolo()==Tok.s.ig)
                GeradorCodigo.getInstance().gera("CEQ");
            else if(t.getSimbolo()==Tok.s.dif)
                GeradorCodigo.getInstance().gera("CDIF");
            else if (t.getSimbolo()==Tok.s.negativo)
                GeradorCodigo.getInstance().gera("INV");
            else if(t.getSimbolo()==Tok.s.mais)
                GeradorCodigo.getInstance().gera("ADD");
            else if(t.getSimbolo()==Tok.s.menos)
                GeradorCodigo.getInstance().gera("SUB");
            else if(t.getSimbolo()==Tok.s.mult)
                GeradorCodigo.getInstance().gera("MULT");
            else if(t.getSimbolo()==Tok.s.div)
                GeradorCodigo.getInstance().gera("DIVI");
        }
    }


}
