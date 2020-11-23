package DATATYPES;

public class Valor{
    private boolean tipo; //0=booleano, 1=inteiro
    private int valor_inteiro;
    private boolean valor_booleano;
    public Valor(int val){
        this.valor_inteiro = val;
        this.tipo = true;
    }
    public Valor(boolean val){
        this.valor_booleano = val;
        this.tipo = false;
    }

    public <T> T getValor() {
        if (tipo == false) {
            return (T) Boolean.valueOf(valor_booleano);
        } else
            return (T) Integer.valueOf(valor_inteiro);
    }

    public void setValor(int val){
        this.valor_inteiro = val;
        this.tipo = true;
    }

    public void setValor(boolean val){
        this.valor_booleano = val;
        this.tipo = false;
    }

    //ver tipo instanceOf?
}
