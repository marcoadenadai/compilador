package DATATYPES;

public class Variavel extends Simbolo {
    private boolean tipo; //0=booleano, 1=inteiro
    private int endereco;

    public Variavel(String lexema, int escopo){
        super(lexema,escopo);
    }

    public Variavel(String lexema, int escopo, int endereco) {
        super(lexema, escopo);
        this.endereco = endereco;
    }

    public boolean getTipo(){ return tipo; }

    public void setTipo(boolean tipo /*0= booleano, 1=inteiro*/){
        this.tipo = tipo;
    }

    public void setEndereco(int endereco) { this.endereco = endereco;}

    public int getEndereco() { return endereco;}

}
