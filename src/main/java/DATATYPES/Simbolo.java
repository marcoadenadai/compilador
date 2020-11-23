package DATATYPES;

public class Simbolo {
    private String lexema; //nome do identificador
    private int escopo;
    int rotulo;

    public Simbolo(String lexema, int escopo){
        this.lexema = lexema;
        this.escopo = escopo;
    }
    public Simbolo(String lexema, int escopo, int rotulo){
        this.lexema = lexema;
        this.escopo = escopo;
        this.rotulo = rotulo;
    }

    public Simbolo getSimbolo() { return this; }
    public String getLexema() { return this.lexema; }
    public int getEscopo() { return this.escopo; }

    /*public void setRotulo(int rotulo) {
        this.rotulo = rotulo;
    }*/
}
