package DATATYPES;

public class Variavel extends Simbolo {
    private boolean tipo; //0=booleano, 1=inteiro
    private Valor valor;

    public Variavel(String lexema, int escopo){
        super(lexema,escopo);
    }

    public Variavel(String lexema, int escopo, boolean valor) {
        super(lexema, escopo);
        this.valor = new Valor(valor);
        this.tipo = false;
    }
    public Variavel(String lexema, int escopo, int valor) {
        super(lexema, escopo);
        this.valor = new Valor(valor);
        this.tipo = true;
    }

    public boolean getTipo(){ return tipo; }

    public void setTipo(boolean tipo /*0= booleano, 1=inteiro*/){
        this.tipo = tipo;
    }
    public void setValor(boolean valor) {
        this.valor.setValor(valor);
    }
    public void setValor(int valor) {
        this.valor.setValor(valor);
    }

}
