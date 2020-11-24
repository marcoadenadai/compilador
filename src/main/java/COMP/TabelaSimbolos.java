package COMP;

import DATATYPES.Simbolo;

import java.util.ArrayList;

public final class TabelaSimbolos {
    private static TabelaSimbolos INSTANCE = new TabelaSimbolos();
    public static TabelaSimbolos getInstance() { return INSTANCE; };
    //-- variables --
    private ArrayList<Simbolo> tbl_simbolos = new ArrayList<Simbolo>();
    //-- functions  --
    //insere na Tabela
    public void insereSimbolo(Simbolo s){
        tbl_simbolos.add(s);
    }

    public Simbolo getSimbolo(int index){
        return tbl_simbolos.get(index);
    }


    public ArrayList<Simbolo> getTabela() { return tbl_simbolos; }
    //getSimbolo()
    //setSimbolo()
    //returnTabela() ArrayList

    public int getLength() { return tbl_simbolos.size();}

    public Simbolo getLastSimbolo() {
        if(tbl_simbolos.size() > 0){
            return tbl_simbolos.get(tbl_simbolos.size() - 1);
        }
        return null;
    }

    public void remove(int index){
        tbl_simbolos.remove(index);
    }

    public void reseta(){
        tbl_simbolos.clear();
    }
}
