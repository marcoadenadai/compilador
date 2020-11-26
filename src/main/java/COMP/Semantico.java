package COMP;

import DATATYPES.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

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
    private Hashtable<Tok.s,Integer> prioridades=null;

    public Semantico () {
    }
    //f(x)
    public void inicializa(){
        TabelaSimbolos.getInstance().reseta();
        zeraEscopo();
        preparaPrioridades();

    }
    public void insere_tabela(String lexema, tipo t, int rotulo){
        Simbolo s;
        switch(t) {
            case programa:
                s = new ProcedimentoOuPrograma(lexema, escopo, rotulo);
                break;
            case procedimento:
                s = new ProcedimentoOuPrograma(lexema, escopo, rotulo);
                break;
            case funcao:
                s= new Funcao(lexema,escopo, rotulo);
                break;
            case variavel:
                s= new Variavel(lexema, escopo, rotulo);
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

        if(nivel==escopo-1) {//significa que nao houve declaracoes dentro daquela func ou proc
            escopo--;
            return;
        }


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
        //ou se ja existe outro identificador visivel de qualquier tipo em nivel anterior
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(!(s instanceof Variavel) && s.getLexema().equals(lexema)){
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

    //Verifica se há duplicidade na declaração de uma funcao
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
    //Verifica se há duplicidade na declaração de um procedimento
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
    //Verifica se há duplicidade na declaração
    public boolean pesquisa_duplicidade_tabela(String lexema){
        Simbolo s;
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s.getLexema().equals(lexema)){
                return true;
            }
        }
        return false;
    }

    /*public boolean pesquisa_declprocfunc_tabela(String lexema){
        Simbolo s;
        for(int i=0; i<TabelaSimbolos.getInstance().getLength() ;i++) {
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            if(s instanceof ProcedimentoOuPrograma || s instanceof Funcao){
                if(s.getLexema().equals(lexema))
                    return true;
            }
        }
        return false;
    }*/

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

    //------------------------------------------------------------ parte 2
    private void preparaPrioridades(){
        if(prioridades == null){
            prioridades = new Hashtable<Tok.s, Integer>();
            prioridades.put(Tok.s.abre_parenteses,0);
            prioridades.put(Tok.s.ou, 1);
            prioridades.put(Tok.s.e, 2);
            prioridades.put(Tok.s.dif, 3);
            prioridades.put(Tok.s.ig, 3);
            prioridades.put(Tok.s.maior, 4);
            prioridades.put(Tok.s.maiorig, 4);
            prioridades.put(Tok.s.menor, 4);
            prioridades.put(Tok.s.menorig, 4);
            prioridades.put(Tok.s.menos, 5);
            prioridades.put(Tok.s.mais, 5);
            prioridades.put(Tok.s.mult, 6);
            prioridades.put(Tok.s.div, 6);
            prioridades.put(Tok.s.positivo, 7);
            prioridades.put(Tok.s.negativo, 7);
        }
    }

    private boolean isOperando(Tok.s simbolo){
        if(simbolo == Tok.s.identificador || simbolo == Tok.s.numero || simbolo == Tok.s.verdadeiro
                || simbolo == Tok.s.falso)
            return true;
        return false;
    }

    private boolean isOperador(Tok.s simbolo){
        if(simbolo == Tok.s.ou || simbolo == Tok.s.e || simbolo == Tok.s.nao
                || simbolo == Tok.s.maior || simbolo == Tok.s.maiorig || simbolo == Tok.s.menor
        || simbolo == Tok.s.menorig || simbolo == Tok.s.ig || simbolo == Tok.s.dif ||
                simbolo == Tok.s.mais || simbolo == Tok.s.menos || simbolo == Tok.s.mult
                || simbolo == Tok.s.div || simbolo == Tok.s.positivo
        || simbolo == Tok.s.negativo)
            return true;
        return false;
    }

    public ArrayList<Tok> posFixa(int pos){
        ArrayList<Tok> ret = null, tmp = new ArrayList<Tok>();
        Stack<Tok> pilha = new Stack<Tok>();
        Tok T;
        Tok.s simbolo;
        for(int i=pos;i<Lexico.getInstance().getLength();i++){
            T = Lexico.getInstance().getToken(i);
            simbolo = T.getSimbolo();
            if(simbolo != Tok.s.dif && simbolo != Tok.s.ig && simbolo != Tok.s.menor && simbolo != Tok.s.menorig
               && simbolo != Tok.s.maior  && simbolo != Tok.s.maiorig
               && simbolo != Tok.s.mais && simbolo != Tok.s.menos && simbolo != Tok.s.identificador && simbolo != Tok.s.numero
               && simbolo != Tok.s.abre_parenteses  && simbolo != Tok.s.fecha_parenteses  && simbolo != Tok.s.verdadeiro
               && simbolo != Tok.s.falso && simbolo != Tok.s.nao && simbolo != Tok.s.ou && simbolo != Tok.s.mult
               && simbolo != Tok.s.div && simbolo != Tok.s.e){
                break;
            }
            //adequa os unarios
            if(simbolo == Tok.s.mais || simbolo == Tok.s.menos){
                if(i==0 || Lexico.getInstance().getToken(i-1).getSimbolo()==Tok.s.abre_parenteses ||
                        isOperador(Lexico.getInstance().getToken(i-1).getSimbolo()) ){
                    if(simbolo == Tok.s.mais){
                        T.setSimbolo(Tok.s.positivo);
                    }else{
                        T.setSimbolo(Tok.s.negativo);
                    }
                }
            }
            tmp.add(T);
        }
        //comeca a pos fixa de vdd
        if(tmp.size() > 0)
            ret = new ArrayList<Tok>();
        else
            return null;
        //comeca a pos fixa de vdd
        for(int i=0;i<tmp.size();i++) {
            T = tmp.get(i);
            simbolo = T.getSimbolo();
            if (isOperando(simbolo)) {
                ret.add(T);
            } else if (isOperador(simbolo)) {
                while (!pilha.empty() && prioridades.get(pilha.peek().getSimbolo()) >= prioridades.get(simbolo)) {
                    ret.add(pilha.pop());
                }
                pilha.push(T);
            } else if (simbolo == Tok.s.abre_parenteses) {
                pilha.push(T);
            } else if (simbolo == Tok.s.fecha_parenteses) {
                while (pilha.peek().getSimbolo() != Tok.s.abre_parenteses) {
                    ret.add(pilha.pop());
                }
                pilha.pop();
            }
        }
        while(!pilha.empty())
            ret.add(pilha.pop());
        return ret;
    }

    public int analisaExpressao(ArrayList<Tok> exp){
        //-1 = erro, 0= booleano, 1=inteiro
        //obs: a expressao precisa estar na pos-fixa
        ArrayList<Integer> tipos = new ArrayList<Integer>();
        for(Tok T : exp){
            if(isOperador(T.getSimbolo())){
                if(T.getSimbolo() == Tok.s.nao || T.getSimbolo() == Tok.s.positivo || T.getSimbolo() == Tok.s.negativo)
                    continue;
                if(T.getSimbolo() == Tok.s.e || T.getSimbolo() == Tok.s.ou){ //recebe booleanos
                    if(tipos.get(tipos.size()-1)!=0 && tipos.get(tipos.size()-2)!=0)
                        return -1;
                    tipos.remove(tipos.size()-1); // gera booleano
                }
                if(T.getSimbolo() == Tok.s.maior  || T.getSimbolo() == Tok.s.maiorig  || T.getSimbolo() == Tok.s.menor
                        || T.getSimbolo() == Tok.s.menorig || T.getSimbolo() == Tok.s.ig
                        || T.getSimbolo() == Tok.s.dif){
                    //recebe 2 inteiros
                    if(tipos.get(tipos.size()-1)!=1 && tipos.get(tipos.size()-2)!=1)
                        return -2;
                    tipos.remove(tipos.size()-1);
                    tipos.remove(tipos.size()-1);
                    tipos.add(0);// gera booelano
                }
                if(T.getSimbolo() == Tok.s.mais || T.getSimbolo() == Tok.s.menos || T.getSimbolo() == Tok.s.mult ||
                T.getSimbolo() == Tok.s.div){//recebe inteiros
                    if(tipos.get(tipos.size()-1)!=1 && tipos.get(tipos.size()-2)!=1 ){
                        return -2;
                    }
                    tipos.remove(tipos.size()-1); //gera inteiros
                }
            }
            else{
                if( T.getSimbolo() == Tok.s.numero)
                    tipos.add(1); // inteiro
                else if( T.getSimbolo() == Tok.s.identificador){
                    Simbolo s = pesquisa_tabela(T.getLexema());
                    if(s instanceof Variavel && ((Variavel)s).getTipo() == true){//inteiro
                        tipos.add(1);
                    }
                    else if(s instanceof Variavel && ((Variavel)s).getTipo() == false){//booleano
                        tipos.add(0);
                    }
                    else if(s instanceof Funcao && ((Funcao)s).getTipo() == true){//inteiro
                        tipos.add(1);
                    }
                    else if(s instanceof Funcao && ((Funcao)s).getTipo() == false){//booleano
                        tipos.add(0);
                    }
                }
                else if( T.getSimbolo() == Tok.s.verdadeiro || T.getSimbolo() == Tok.s.falso)
                    tipos.add(0);
            }
        }
        return tipos.get(0);
        //return 0;
    }

    public void coloca_tipo_tabela(int count, boolean tipo){
        int tam = TabelaSimbolos.getInstance().getLength();
        Simbolo s;
        for(int i=tam-1; i>=(tam-count);i--){
            s=TabelaSimbolos.getInstance().getSimbolo(i);
            ((Variavel)s).setTipo(tipo);
        }
    }

    public Erro valida_retorno(String nome_funcao, int inicio, int fim){
        boolean cond_flag=false, valido=false;
        int i=inicio;
        for(;i<fim;i++){
            if(Lexico.getInstance().getToken(i).getSimbolo() == Tok.s.inicio)
                break;
        }//começo a trabalhar no bloco (lexema inicio)
        Tok ant=Lexico.getInstance().getToken(i), t, prox;
        Tok.s s1,s2,s3;
        for(i++;i<fim-1;ant=t,i++){
            t=Lexico.getInstance().getToken(i);
            prox=Lexico.getInstance().getToken(i+1);
            s1=ant.getSimbolo(); s2=t.getSimbolo(); s3=prox.getSimbolo(); //foco no s2 que é o atual

            if(valido){ //percorro expressao apos atrib, e verifico se aquele é o ultimo comando do bloco
                if(s1!=Tok.s.ponto_virgula)
                    continue;//ignora tudo dentro da expressao
                if(s2!= Tok.s.fim){
                    return new Erro(t,Erro.e.err_unreachable);
                }
                continue;
            } //--------------

            if(cond_flag){ //verificacao de retorno condicional
                if(s1!=Tok.s.senao && s1!=Tok.s.ponto_virgula)
                    continue;//ignora tudo dentro da expressao
                if(s1 == Tok.s.senao && t.getLexema().equals(nome_funcao) && s2 == Tok.s.identificador && s3 == Tok.s.atribuicao){
                    //apos encontrar o senao verifico se a atribuicao de retorno eh valida
                    valido=true; //percorro expressao apos atrib, e verifico se aquele é o ultimo comando do bloco
                }
                else{//se nao encontrar senao continuo o loop
                    cond_flag=false;
                    i--;
                    continue;
                }
                continue;
            } //------------
            //verifico se ocorre atribuicao de retorno da funcao
            if(t.getLexema().equals(nome_funcao) && s2 == Tok.s.identificador && s3 == Tok.s.atribuicao){
                if(s1 == Tok.s.entao){ // se for dentro de um "se (..exp..) entao (retorno)"
                    //condicional
                    cond_flag=true;
                }
                else{
                    //simples
                    valido=true;
                }
            }

        }
        if(!valido){
            return new Erro(Lexico.getInstance().getToken(fim), Erro.e.err_retorno2);
        }
        return new Erro(0, Erro.e.vazio);
    }

}
