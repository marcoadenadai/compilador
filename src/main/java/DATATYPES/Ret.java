package DATATYPES;

public class Ret {
    public boolean valido=false;
    public int posicao;
    public Erro e;

    public Ret(boolean valido){
        this.valido=valido;
        this.e = new Erro(0,Erro.e.vazio);
    }
    public Ret(){
        this.e = new Erro(0,Erro.e.vazio);
    }
    public Ret(boolean valido, int pos){
        this.valido=valido;
        this.posicao=pos;
        this.e = new Erro(0,Erro.e.vazio);
    }

}
