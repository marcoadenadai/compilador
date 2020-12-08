package DATATYPES;

public class Simbolo {
    private String lexema; //nome do identificador
    private int escopo;
    int rotulo;

    public Simbolo(String lexema, int escopo){
        this.lexema = lexema;
        this.escopo = escopo;
    }

    public Simbolo getSimbolo() { return this; }
    public String getLexema() { return this.lexema; }
    public int getEscopo() { return this.escopo; }
}
