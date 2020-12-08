package COMP;
import DATATYPES.*;

import java.util.ArrayList;

public final class Sintatico {
    public static final Sintatico INSTANCE = new Sintatico();
    private Tok T;
    private int i;
    public static Sintatico getInstance() {
        return INSTANCE;
    }
    //semantico
    private Tok Tant;
    private int vars_count = 0;
    private boolean dentro_funcao = false;
    private ArrayList<Tok> expressao;
    //geracao codigo
    private int rotulo;
    private int endereco;

    public Sintatico() {
    }

    void lexico_Token(){
        T = Lexico.getInstance().getToken(i);
        i++;
    }

    Tok.s token_simbolo(){
        return T.getSimbolo();
    }

    Erro load(){
        i=0;
        dentro_funcao = false;
        rotulo=1;
        endereco=0;
        Semantico.getInstance().inicializa();
        lexico_Token();
        if(T==null)
            return new Erro(1,Erro.e.programa_vazio);
        if(token_simbolo() == Tok.s.programa){
            lexico_Token();
            if(token_simbolo() == Tok.s.identificador){
                //semantico insere_tabela
                Semantico.getInstance().insere_tabela(T.getLexema(), Semantico.tipo.programa, rotulo);
                GeradorCodigo.getInstance().gera("START");
                lexico_Token();
                if(token_simbolo() == Tok.s.ponto_virgula){
                    //analisa bloco
                    Erro E = analisa_bloco();
                    if(E.get_errno() != 0)
                        return E;
                    if(T!= null && token_simbolo() == Tok.s.ponto){
                        if(i >= Lexico.getInstance().getLength()){
                            GeradorCodigo.getInstance().gera("HLT");
                            GeradorCodigo.getInstance().close();
                            //return new Erro(0, Erro.e.sucesso);
                        }else{
                            //programa nao acabou
                            return new Erro(T, Erro.e.simbolo_nao_esperado);
                        }
                    }
                    else{
                        //missing ponto fim do bloco
                        T = Lexico.getInstance().getToken(i-2);
                        return new Erro(T, Erro.e.ponto_esperado);
                    }
                }
                else{
                    // missing virugla
                    return new Erro(T, Erro.e.virgula_esperada);
                }
            }
            else{
                //missing identificador after programa
                return new Erro(T, Erro.e.identificador_esperado);
            }
        }
        else{
            //missing programa
            return new Erro(T, Erro.e.programa_esperado);
        }
        //preciso desempilhar aqui???
        return new Erro(0, Erro.e.sucesso);
    }


    Erro analisa_bloco(){
        Erro E;
        lexico_Token();
        //analisa_et_variaveis
        E = analisa_et_variaveis();
        if(E.get_errno() != 0)
            return E;
        //analisa_subrotina
        E = analisa_subrotina();
        if(E.get_errno() != 0)
            return E;
        //analisa_comandos
        E = analisa_comandos();
        if(E.get_errno() != 0)
            return E;
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_et_variaveis(){
        Erro E;
        if(token_simbolo() == Tok.s.var){
            vars_count = 0;
            lexico_Token();
            if(token_simbolo() == Tok.s.identificador){
                while(token_simbolo() == Tok.s.identificador){
                    //analisa variaveis
                    E = analisa_variaveis();
                    if(E.get_errno() != 0)
                        return E;
                    if(token_simbolo() == Tok.s.ponto_virgula){
                        vars_count = 0;
                        lexico_Token();
                    }
                    else{
                        //qualquer outro simbolo diferente de ; nao era esperado aqui..
                        return new Erro(T, Erro.e.simbolo_nao_esperado);
                    }
                }
            }
            else{
                return new Erro(T, Erro.e.identificador_esperado);
            }
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_variaveis(){
        Erro E;
        do{
            if(token_simbolo() == Tok.s.identificador){
                //semantico
                if(!Semantico.getInstance().pesquisa_duplicvar_tabela(T.getLexema())){
                    Semantico.getInstance().insere_tabela(T.getLexema(), Semantico.tipo.variavel, endereco+vars_count);
                    vars_count++;
                    lexico_Token();
                    if(token_simbolo() == Tok.s.virgula || token_simbolo() == Tok.s.doispontos){
                        if(token_simbolo() == Tok.s.virgula){
                            lexico_Token();
                            if(token_simbolo() == Tok.s.doispontos){
                                return new Erro(T, Erro.e.simbolo_nao_esperado);
                            }
                        }
                    }
                    else{
                        return new Erro(T, Erro.e.pontovirgulaouvirugla_esperado);
                    }
                }
                else{
                    return new Erro(T, Erro.e.duplicvar);
                }
            }
            else{
                return new Erro(T, Erro.e.identificador_esperado);
            }
        }while(token_simbolo() != Tok.s.doispontos);
        lexico_Token();
        E=analisa_tipo();
        if(E.get_errno() != 0)
            return E;
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_tipo(){
        Erro E;
        if(token_simbolo()!= Tok.s.inteiro && token_simbolo() != Tok.s.booleano){
            return new Erro(T, Erro.e.tipo_esperado);
        }else{
            boolean tp = (token_simbolo() == Tok.s.inteiro);
            Semantico.getInstance().coloca_tipo_tabela(vars_count,tp);
        }
        lexico_Token();
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_subrotina(){
        Erro E;
        int aux=-5, flag=0;
        if(token_simbolo() == Tok.s.procedimento || token_simbolo() == Tok.s.funcao){
            aux=rotulo;
            GeradorCodigo.getInstance().gera("JMP", "L"+rotulo);
            rotulo++;
            flag=1;
        }
        while(token_simbolo() == Tok.s.procedimento || token_simbolo() == Tok.s.funcao){
            if(token_simbolo() == Tok.s.procedimento){
                //analisa_declaracao_procedimento
                E=analisa_declaracao_procedimento();
                if(E.get_errno() != 0)
                    return E;
            }else{
                //analisa_declaracao_funcao
                E=analisar_declaracao_funcao();
                if(E.get_errno() != 0)
                    return E;
            }
            if(token_simbolo() == Tok.s.ponto_virgula){
                lexico_Token();
            }
            else{
                return new Erro(T, Erro.e.pontovirgula_esperado);
            }
        }
        if(flag == 1){
            GeradorCodigo.getInstance().geraLabel(aux);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_declaracao_procedimento(){
        Erro E;
        lexico_Token();
        if(token_simbolo() == Tok.s.identificador){
            //if(!Semantico.getInstance().pesquisa_declproc_tabela(T.getLexema())){
            if(!Semantico.getInstance().pesquisa_duplicidade_tabela(T.getLexema())){
                Semantico.getInstance().insere_tabela(T.getLexema(), Semantico.tipo.procedimento, rotulo); //rotulo!
                //vermelho
                GeradorCodigo.getInstance().geraLabel(rotulo);
                rotulo++;
                lexico_Token();
                if(token_simbolo() == Tok.s.ponto_virgula){
                    E = analisa_bloco();
                    if(E.get_errno() != 0)
                        return E;
                    //possivel !!!!!!! GeraCodigo RETURN
                }
                else{
                    return new Erro(T, Erro.e.simbolo_nao_esperado);
                }
            }
            else{
                //erro
                return new Erro(T, Erro.e.duplicidade);
            }
        }else{
            return new Erro(T, Erro.e.identificador_esperado);
        }
        Semantico.getInstance().desempilha();
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisar_declaracao_funcao(){
        Erro E;
        lexico_Token();
        if(token_simbolo() == Tok.s.identificador){
            //if(!Semantico.getInstance().pesquisa_declfunc_tabela(T.getLexema())){
            if(!Semantico.getInstance().pesquisa_duplicidade_tabela(T.getLexema())){
                String nome_func = T.getLexema();
                Semantico.getInstance().insere_tabela(T.getLexema(), Semantico.tipo.funcao, rotulo); //rotulo!
                GeradorCodigo.getInstance().geraLabel(rotulo);
                rotulo++;
                lexico_Token();
                if(token_simbolo() == Tok.s.doispontos){
                    lexico_Token();
                    if(token_simbolo() == Tok.s.inteiro || token_simbolo() == Tok.s.booleano){
                        //semantico azul
                        if(token_simbolo() == Tok.s.inteiro){
                            Semantico.getInstance().setUltimoTipo(true);//atribui funcao tipo inteiro
                        }
                        else{
                            Semantico.getInstance().setUltimoTipo(false);//atribui funcao tipo booleano
                        }
                        lexico_Token();
                        if(token_simbolo() == Tok.s.ponto_virgula){
                            dentro_funcao = true ;
                            int pos_inicio_func = i;
                            E=analisa_bloco();
                            if(E.get_errno() !=0)
                                return E;
                            //nao existiu erro sintatico ate o fim da funcao
                            int pos_final_func = i-1;
                            E=Semantico.getInstance().valida_retorno(nome_func,pos_inicio_func,pos_final_func);
                            if(E.get_errno() !=0)
                                return E;
                            //nao existiu erro semantico ate o fim da funcao!

                        }
                        else{
                            return new Erro(T, Erro.e.simbolo_nao_esperado);
                        }
                    }else{
                        return new Erro(T, Erro.e.tipo_esperado);
                    }
                }else{
                    return new Erro(T, Erro.e.doispontos_esperado);
                }
            }
            else{
                return new Erro(T, Erro.e.duplicidade);
            }
        }else{
            return new Erro(T, Erro.e.identificador_esperado);
        }
        Semantico.getInstance().desempilha();
        dentro_funcao = false;
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_comandos(){
        Erro E;
        if(token_simbolo() == Tok.s.inicio){
            lexico_Token();
            E = analisa_comando_simples();
            if(E.get_errno() != 0)
                return E;
            while(token_simbolo() != Tok.s.fim){
                if(token_simbolo() == Tok.s.ponto_virgula){
                    //analisa_comando_simples
                    lexico_Token();
                    if(token_simbolo() != Tok.s.fim){
                        E = analisa_comando_simples();
                        if(E.get_errno() != 0)
                            return E;
                    }
                }else{
                    return new Erro(T, Erro.e.simbolo_nao_esperado);
                }
            }
            lexico_Token();
        }
        else{
            return new Erro(T, Erro.e.simbolo_nao_esperado);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_comando_simples(){
        Erro E;
        if(token_simbolo() == Tok.s.identificador){
            E=analisa_atrib_chprocedimento();
            if(E.get_errno() != 0)
                return E;
        } else if (token_simbolo() == Tok.s.se){
            //analisa_se
            E=analisa_se();
            if(E.get_errno() != 0)
                return E;
        } else if(token_simbolo() == Tok.s.enquanto){
            //analisa_enquanto
            E=analisa_enquanto();
            if(E.get_errno() != 0)
                return E;
        } else if(token_simbolo() == Tok.s.leia){
            //analisa_leia
            E=analisa_leia();
            if(E.get_errno() != 0)
                return E;
        } else if(token_simbolo() == Tok.s.escreva){
            //analisa_escreva
            E=analisa_escreva();
            if(E.get_errno() != 0)
                return E;
        } else{
            E = analisa_comandos();
            if(E.get_errno() != 0)
                return E;
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_atrib_chprocedimento(){
        Erro E;
        Tant = T;
        lexico_Token();
        if(token_simbolo() == Tok.s.atribuicao){
            E = analisa_atribuicao();
            if(E.get_errno() != 0)
                return E;
        }else{
            E = chamada_procedimento();
            if(E.get_errno() != 0)
                return E;
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro chamada_procedimento(){
        //Verificar se identificador é procedimento (sim=OK) e pesquisa_declproc (sim=OK)
        if(!Semantico.getInstance().pesquisa_declproc_tabela(Tant.getLexema())){
            //erro
            return new Erro(Tant, Erro.e.declproc);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro chamada_funcao(){
        lexico_Token();
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_atribuicao() {
        Simbolo s = Semantico.getInstance().pesquisa_tabela(Tant.getLexema());
        boolean tipo_inteiro = false;
        if(s!=null){
            if(s instanceof Variavel){
                tipo_inteiro = ((Variavel)s).getTipo();
            }else {
                if(dentro_funcao == false)
                    return new Erro(Tant, Erro.e.err_retorno);
                tipo_inteiro = ((Funcao)s).getTipo();
            }
            expressao = new ArrayList<Tok>();
            Erro E;
            lexico_Token();
            E = analisa_expressao();
            if(E.get_errno() != 0)
                return E;
            adequaUnarios();
            expressao = Semantico.getInstance().posFixa(expressao);
            int tipo_exp = Semantico.getInstance().analisaExpressao(expressao);
            if(tipo_exp >= 0){
                if(tipo_exp == 1 && tipo_inteiro==false){
                    return new Erro(Tant, Erro.e.atrib_bool2int);
                }
                else if(tipo_exp == 0 && tipo_inteiro==true){
                    return new Erro(Tant, Erro.e.atrib_int2bool);
                }
            }
            else{
                return new Erro(Tant,Erro.e.expressao_incompativel);
            }
            GeradorCodigo.getInstance().geraExpressao(expressao);
        }
        else{
            //erro
            return new Erro(Tant, Erro.e.declvarfunc);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_expressao(){
        Erro E;
        E = analisa_expressao_simples();
        if(E.get_errno() != 0)
            return E;
        if(token_simbolo() == Tok.s.maior || token_simbolo() == Tok.s.maiorig || token_simbolo() == Tok.s.ig
                || token_simbolo() == Tok.s.menor || token_simbolo() == Tok.s.menorig || token_simbolo() == Tok.s.dif){
            expressao.add(T);
            lexico_Token();
            E = analisa_expressao_simples();
            if(E.get_errno() != 0)
                return E;
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_expressao_simples() {
        Erro E;
        if(token_simbolo() == Tok.s.mais || token_simbolo() == Tok.s.menos) {
            expressao.add(T);
            lexico_Token();
        }
        E = analisa_termo();
        if(E.get_errno() != 0)
            return E;
        while((token_simbolo() == Tok.s.mais) || (token_simbolo() == Tok.s.menos) ||
                (token_simbolo() == Tok.s.ou)){
            expressao.add(T);
            lexico_Token();
            E = analisa_termo();
            if(E.get_errno() != 0)
                return E;
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_termo(){
        Erro E;
        E=analisa_fator();
        if(E.get_errno() != 0)
            return E;
        while(token_simbolo() == Tok.s.mult || token_simbolo() == Tok.s.div ||
                token_simbolo() == Tok.s.e){
            expressao.add(T);
            lexico_Token();
            E=analisa_fator();
            if(E.get_errno() != 0)
                return E;
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_fator(){
        Erro E;
        if(token_simbolo() == Tok.s.identificador){
            Simbolo s =Semantico.getInstance().pesquisa_tabela(T.getLexema());
            if(s!=null){
                    //blablabla azul6
                expressao.add(T);
                    E=chamada_funcao();
                    if(E.get_errno() != 0)
                        return E;
                    //blablabla azul
            }
            else{
                return new Erro(T, Erro.e.declvarfunc);
            }
        }else if( token_simbolo() == Tok.s.numero){
            expressao.add(T);
            lexico_Token();
        }else if( token_simbolo() == Tok.s.nao){
            expressao.add(T);
            lexico_Token();
            E=analisa_fator();
            if(E.get_errno() != 0)
                return E;
        }else if( token_simbolo() == Tok.s.abre_parenteses){
            expressao.add(T);
            lexico_Token();
            analisa_expressao();//(token)????!!!!????
            if(token_simbolo() == Tok.s.fecha_parenteses){
                expressao.add(T);
                lexico_Token();
            }else{
                return new Erro(T, Erro.e.simbolo_nao_esperado);
            }
        }else if ( token_simbolo() == Tok.s.verdadeiro || token_simbolo() == Tok.s.falso){
            expressao.add(T);
            lexico_Token();
        }else{
            return new Erro(T, Erro.e.simbolo_nao_esperado);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_leia(){
        Erro E;
        lexico_Token();
        if(token_simbolo() == Tok.s.abre_parenteses){
            lexico_Token();
            if(token_simbolo() == Tok.s.identificador){
                if(Semantico.getInstance().pesquisa_declvar_tabela(T.getLexema())){
                    lexico_Token();
                    if(token_simbolo() == Tok.s.fecha_parenteses){
                        lexico_Token();
                    } else {
                        return new Erro(T, Erro.e.fechaparenteses_esperado);
                    }
                }
                else{
                    //erro
                    return new Erro(T, Erro.e.declvar);
                }
            } else {
                return new Erro(T, Erro.e.identificador_esperado);
            }
        } else {
            return new Erro(T, Erro.e.abreparenteses_esperado);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_escreva(){
        Erro E;
        lexico_Token();
        if(token_simbolo() == Tok.s.abre_parenteses){
            lexico_Token();
            if(token_simbolo() == Tok.s.identificador){
                if(Semantico.getInstance().pesquisa_declvarfunc_tabela(T.getLexema())){
                    lexico_Token();
                    if(token_simbolo() == Tok.s.fecha_parenteses){
                        lexico_Token();
                    } else {
                        return new Erro(T, Erro.e.fechaparenteses_esperado);
                    }
                }
                else{
                    return new Erro(T, Erro.e.declvarfunc);
                }
            } else {
                return new Erro(T, Erro.e.identificador_esperado);
            }
        } else {
            return new Erro(T, Erro.e.abreparenteses_esperado);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_enquanto(){
        expressao = new ArrayList<Tok>();
        Erro E;
        int aux1=rotulo, aux2;
        GeradorCodigo.getInstance().geraLabel(rotulo);
        rotulo++;

        Tant=T;
        lexico_Token();
        E = analisa_expressao();
        if(E.get_errno() != 0)
            return E;
        adequaUnarios();
        expressao = Semantico.getInstance().posFixa(expressao);
        int tipo_exp = Semantico.getInstance().analisaExpressao(expressao);
        if(tipo_exp != 0){
            return new Erro(Tant,Erro.e.exp_booleana_esperada);
        }
        GeradorCodigo.getInstance().geraExpressao(expressao);
        if(token_simbolo() == Tok.s.faca){
            aux2=rotulo;
            GeradorCodigo.getInstance().gera("JMPF","L"+rotulo);
            rotulo++;
            //veremlho
            lexico_Token();
            E =analisa_comando_simples();
            if(E.get_errno() != 0)
                return E;
            GeradorCodigo.getInstance().gera("JMP","L"+aux1);
            GeradorCodigo.getInstance().geraLabel(aux2);
        }else{
            return new Erro(T, Erro.e.faca_esperado);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_se(){
        int aux1,aux2;
        expressao = new ArrayList<Tok>();
        Erro E;
        Tant=T;
        lexico_Token();
        E =analisa_expressao();
        if(E.get_errno() != 0)
            return E;
        adequaUnarios();
        expressao = Semantico.getInstance().posFixa(expressao);
        int tipo_exp = Semantico.getInstance().analisaExpressao(expressao);
        if(tipo_exp != 0){
            return new Erro(Tant,Erro.e.exp_booleana_esperada);
        }
        GeradorCodigo.getInstance().geraExpressao(expressao);
        aux1=rotulo;
        rotulo++;
        if(token_simbolo() == Tok.s.entao){
            GeradorCodigo.getInstance().gera("JMPF", "L"+aux1);
            lexico_Token();
            E =analisa_comando_simples();
            if(E.get_errno() != 0)
                return E;
            if(token_simbolo() == Tok.s.senao){
                aux2=rotulo;
                rotulo++;
                GeradorCodigo.getInstance().gera("JMP", "L"+aux2);
                GeradorCodigo.getInstance().geraLabel(aux1);
                lexico_Token();
                E =analisa_comando_simples();
                if(E.get_errno() != 0)
                    return E;
                GeradorCodigo.getInstance().geraLabel(aux2);
            }
        }else{
            return new Erro(T, Erro.e.entao_esperado);
        }
        return new Erro(0, Erro.e.vazio);
    }

    ///////
    private void adequaUnarios() {
        for(int i=0;i<expressao.size();i++) {
            if(expressao.get(i).getSimbolo()==Tok.s.mais || expressao.get(i).getSimbolo()==Tok.s.menos) {
                if(i==0 || expressao.get(i-1).getSimbolo()==Tok.s.abre_parenteses
                        || Semantico.getInstance().isOperador(expressao.get(i-1).getSimbolo())) {
                    if(expressao.get(i).getSimbolo()==Tok.s.mais)
                        expressao.get(i).setSimbolo(Tok.s.positivo);
                    else
                        expressao.get(i).setSimbolo(Tok.s.negativo);
                }
            }
        }
    }


}