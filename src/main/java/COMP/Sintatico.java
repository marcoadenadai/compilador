package COMP;
import DATATYPES.Erro;
import DATATYPES.Tok;

public final class Sintatico {
    public static final Sintatico INSTANCE = new Sintatico();
    private Tok T;
    private int i;
    public static Sintatico getInstance() {
        return INSTANCE;
    }

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
        lexico_Token();
        if(T==null)
            return new Erro(1,Erro.e.programa_vazio);
        if(token_simbolo() == Tok.s.programa){
            lexico_Token();
            if(token_simbolo() == Tok.s.identificador){
                //semantico insere_tabela
                lexico_Token();
                if(token_simbolo() == Tok.s.ponto_virgula){
                    //analisa bloco
                    Erro E = analisa_bloco();
                    if(E.get_errno() != 0)
                        return E;
                    if(T!= null && token_simbolo() == Tok.s.ponto){
                        if(i >= Lexico.getInstance().getLength()){
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
            lexico_Token();
            if(token_simbolo() == Tok.s.identificador){
                while(token_simbolo() == Tok.s.identificador){
                    //analisa variaveis
                    E = analisa_variaveis();
                    if(E.get_errno() != 0)
                        return E;
                    if(token_simbolo() == Tok.s.ponto_virgula){
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
            //semantico
        }
        lexico_Token();
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_subrotina(){
        Erro E;
        int flag=0;
        if(token_simbolo() == Tok.s.procedimento || token_simbolo() == Tok.s.funcao){
            //auxrot := rotulo .....
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
                return new Erro(T, Erro.e.simbolo_nao_esperado);
            }
        }
        if(flag == 1){
            //gera blablabla vermelho ....
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_declaracao_procedimento(){
        Erro E;
        lexico_Token();
        if(token_simbolo() == Tok.s.identificador){
            //azul
            //vermelho
            lexico_Token();
            if(token_simbolo() == Tok.s.ponto_virgula){
                E = analisa_bloco();
                if(E.get_errno() != 0)
                    return E;
            }
            else{
                return new Erro(T, Erro.e.simbolo_nao_esperado);
            }
        }else{
            return new Erro(T, Erro.e.identificador_esperado);
        }
        //DESEMPILHA EM AZUL
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisar_declaracao_funcao(){
        Erro E;
        lexico_Token();
        if(token_simbolo() == Tok.s.identificador){
            lexico_Token();
            if(token_simbolo() == Tok.s.doispontos){
                lexico_Token();
                if(token_simbolo() == Tok.s.inteiro || token_simbolo() == Tok.s.booleano){
                    lexico_Token();
                    if(token_simbolo() == Tok.s.ponto_virgula){
                        E=analisa_bloco();
                        if(E.get_errno() !=0)
                            return E;
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
        }else{
            return new Erro(T, Erro.e.identificador_esperado);
        }
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
        return new Erro(0, Erro.e.vazio);
    }

    Erro chamada_funcao(){
        lexico_Token();
        //!!!ATENCAO AQUI, TALVEZ PRECISE MEXER
        //POIS analisa_fator chama esta função considerando uma possivel variavel assim como uma possivel funcao
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_atribuicao() {
        Erro E;
        lexico_Token();
        E = analisa_expressao();
        if(E.get_errno() != 0)
            return E;
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_expressao(){
        Erro E;
        E = analisa_expressao_simples();
        if(E.get_errno() != 0)
            return E;
        if(token_simbolo() == Tok.s.maior || token_simbolo() == Tok.s.maiorig || token_simbolo() == Tok.s.ig
                || token_simbolo() == Tok.s.menor || token_simbolo() == Tok.s.menorig || token_simbolo() == Tok.s.dif){
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
            lexico_Token();
        }
        E = analisa_termo();
        if(E.get_errno() != 0)
            return E;
        while((token_simbolo() == Tok.s.mais) || (token_simbolo() == Tok.s.menos) ||
                (token_simbolo() == Tok.s.ou)){
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
            //blablabla azul
            E=chamada_funcao();
            if(E.get_errno() != 0)
                return E;
            //blablabla azul
        }else if( token_simbolo() == Tok.s.numero){
            lexico_Token();
        }else if( token_simbolo() == Tok.s.nao){
            lexico_Token();
            E=analisa_fator();
            if(E.get_errno() != 0)
                return E;
        }else if( token_simbolo() == Tok.s.abre_parenteses){
            lexico_Token();
            analisa_expressao();//(token)????!!!!????
            if(token_simbolo() == Tok.s.fecha_parenteses){
                lexico_Token();
            }else{
                return new Erro(T, Erro.e.simbolo_nao_esperado);
            }
        }else if ( token_simbolo() == Tok.s.verdadeiro || token_simbolo() == Tok.s.falso){
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
                //azul
                //azul
                lexico_Token();
                if(token_simbolo() == Tok.s.fecha_parenteses){
                    lexico_Token();
                } else {
                    return new Erro(T, Erro.e.fechaparenteses_esperado);
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
                //azul
                //azul
                lexico_Token();
                if(token_simbolo() == Tok.s.fecha_parenteses){
                    lexico_Token();
                } else {
                    return new Erro(T, Erro.e.fechaparenteses_esperado);
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
        Erro E;
        //int auxrot1, auxrot2;
        //vermelho
        lexico_Token();
        E = analisa_expressao();
        if(E.get_errno() != 0)
            return E;
        if(token_simbolo() == Tok.s.faca){
            //veremlho
            lexico_Token();
            E =analisa_comando_simples();
            if(E.get_errno() != 0)
                return E;
        }else{
            return new Erro(T, Erro.e.faca_esperado);
        }
        return new Erro(0, Erro.e.vazio);
    }

    Erro analisa_se(){
        Erro E;
        lexico_Token();
        E =analisa_expressao();
        if(E.get_errno() != 0)
            return E;
        if(token_simbolo() == Tok.s.entao){
            lexico_Token();
            E =analisa_comando_simples();
            if(E.get_errno() != 0)
                return E;
            if(token_simbolo() == Tok.s.senao){
                lexico_Token();
                E =analisa_comando_simples();
                if(E.get_errno() != 0)
                    return E;
            }
        }else{
            return new Erro(T, Erro.e.entao_esperado);
        }
        return new Erro(0, Erro.e.vazio);
    }


}