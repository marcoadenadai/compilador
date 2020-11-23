package COMP;

import DATATYPES.*;

import java.util.ArrayList;
//TIPOS 0=booleano, 1=inteiro
public final class Semantico {
    public enum tipo {
        programa,
        procedimento,
        funcao,
        variavel,
        vazio
    }
    private static Semantico INSTANCE = new Semantico();
    public static Semantico getInstance() { return INSTANCE; };
    private int escopo=0;

    public Semantico () {

    }
    //f(x)
    public void insere_tabela(String lexema, tipo t, int rotulo){
        Simbolo s;
        switch(t) {
            case programa:
                s = new ProcedimentoOuPrograma(lexema, escopo);
                break;
            case procedimento:
                s = new ProcedimentoOuPrograma(lexema, escopo);
                break;
            case funcao:
                s= new Funcao(lexema,escopo);
                break;
            case variavel:
                s= new Variavel(lexema, escopo);
                break;
            default:
                throw new IllegalStateException("Unexpected value");
        }
        TabelaSimbolos.getInstance().insereSimbolo(s);
        if(t == tipo.procedimento || t == tipo.funcao)
            escopo++;
    }
    public void insere_tabela(String lexema, tipo t){
        Simbolo s;
        switch(t) {
            case programa:
                s = new ProcedimentoOuPrograma(lexema, escopo);
                break;
            case procedimento:
                s = new ProcedimentoOuPrograma(lexema, escopo);
                break;
            case funcao:
                s= new Funcao(lexema,escopo);
                break;
            case variavel:
                s= new Variavel(lexema, escopo);
                break;
            default:
                throw new IllegalStateException("Unexpected value");
        }
        TabelaSimbolos.getInstance().insereSimbolo(s);
        if(t == tipo.procedimento || t == tipo.funcao)
            escopo++;
    }
    public void desempilha(){
        Simbolo s = TabelaSimbolos.getInstance().getLastSimbolo();
        if (s == null){
            return;
        }

        int tam = TabelaSimbolos.getInstance().getLength();
        int nivel=s.getEscopo();
        int i;
        for(i=tam-1;s!=null && s.getEscopo() == nivel ; i--){
            TabelaSimbolos.getInstance().remove(i);
            s = TabelaSimbolos.getInstance().getLastSimbolo();
        }
        escopo--;
    }

    public boolean pesquisa_duplicvar_tabela(String lexema){
        int tam = TabelaSimbolos.getInstance().getLength();
        Simbolo s = TabelaSimbolos.getInstance().getLastSimbolo();
        int escopo=s.getEscopo();

        for(int i=tam-1; i>=0 && s.getEscopo() == escopo ;i--){
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s instanceof Variavel && s.getLexema().equals(lexema)){
                return true;
            }
        }
        return false;
    }

    public boolean pesquisa_declvar_tabela(String lexema){
        Simbolo s;
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s instanceof Variavel && s.getLexema().equals(lexema)){
                return true;
            }
        }
        return false;
    }

    public boolean pesquisa_declvarfunc_tabela(String lexema){
        Simbolo s;
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s instanceof Variavel || s instanceof Funcao){
                if(s.getLexema().equals(lexema))
                    return true;
            }
        }
        return false;
    }

    public boolean pesquisa_declfunc_tabela(String lexema){
        Simbolo s;
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s instanceof Funcao && s.getLexema().equals(lexema)){
                    return true;
            }
        }
        return false;
    }

    public boolean pesquisa_declproc_tabela(String lexema){
        Simbolo s;
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s instanceof ProcedimentoOuPrograma && s.getLexema().equals(lexema)){
                return true;
            }
        }
        return false;
    }

    public boolean pesquisa_declprocfunc_tabela(String lexema){
        Simbolo s;
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s instanceof ProcedimentoOuPrograma || s instanceof Funcao){
                if(s.getLexema().equals(lexema))
                    return true;
            }
        }
        return false;
    }

    public void zeraEscopo(){
        escopo = 0;
    }

    public Simbolo pesquisa_tabela(String lexema){
        Simbolo s;
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s instanceof Variavel || s instanceof Funcao){
                if(s.getLexema().equals(lexema))
                    return s;
            }
        }
        return null;
    }

    public ArrayList<Tok> posFixa(ArrayList<Tok> expressao){
        ArrayList<Tok> ret = new ArrayList<Tok>();

        return ret;
    }

    //todo:

    public int analisaExpressao(){
        //cada numero é um tipo da expressao
        return 0;
    }

    public void coloca_tipo_tabela(int count, String lexema){

    }

    public void setUltimoTipo(boolean tipo/*0=booleano, 1=inteiro*/){
        //int index = TabelaSimbolos.getInstance().getLength()-1;
        Simbolo s = TabelaSimbolos.getInstance().getLastSimbolo();
        if( s instanceof Variavel){
            ((Variavel)s).setTipo(tipo);
        }
        else if (s instanceof Funcao){
            ((Funcao)s).setTipo(tipo);
        }
    }


}
