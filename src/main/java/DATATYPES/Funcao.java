package DATATYPES;

public class Funcao extends Simbolo{
    private boolean tipo; //0=booleano, 1=inteiro

    public Funcao(String lexema, int escopo) {
        super(lexema, escopo);
    }
    public Funcao(String lexema, int escopo, int rotulo) {
        super(lexema, escopo, rotulo);
    }
    public void setTipo(boolean tipo) {this.tipo = tipo;}
    public boolean getTipo(){ return tipo; /*0=booleano 1=inteiro*/}
}
