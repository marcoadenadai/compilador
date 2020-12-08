package DATATYPES;

public class Erro {
    public enum e{
        sucesso,
        vazio,
        //lexico
        simbolo_invalido,
        programa_vazio,
        simbolo_nao_esperado,
        identificador_esperado,
        ponto_esperado,
        virgula_esperada,
        programa_esperado,
        pontovirgulaouvirugla_esperado,
        pontovirgula_esperado,
        tipo_esperado,
        doispontos_esperado,
        inicio_esperado,
        fechaparenteses_esperado,
        abreparenteses_esperado,
        faca_esperado,
        entao_esperado,
        //semantico
        duplicvar,
        declproc,
        declfunc,
        declvarfunc,
        declvar,
        expressao_incompativel,
        atrib_int2bool,
        atrib_bool2int,
        exp_booleana_esperada,
        duplicidade,
        err_retorno,
        err_retorno2,
        err_unreachable,
    };
    private e type;
    private int errno;
    private String lexema;

    public Erro(Tok T, e type){
        this.errno = T.getLine();
        this.lexema = T.getLexema();
        this.type = type;
    }

    public Erro(int errno, e type){
        this.errno = errno;
        this.lexema = null;
        this.type = type;
    }


    public int get_errno(){ return errno; }
    //public e get_etype() {return type;}

    //ERROR DESCRIPTION
    public String get_description(){
        String ret="";
        switch (type){
            case vazio:
                ret = null;
                break;
            case sucesso:
                ret = "Sucesso, não houve erros na compilação! :-)";
                break;
            //lexico------------------------------------------------------------------
            case simbolo_invalido:
                ret = "Erro lexical na linha "+errno+", existe(m) símbolo(s) inválido.";
                break;
            case programa_vazio:
                ret = "Erro na linha "+errno+", programa vazio. " +
                        "Verifique se não existem comentários abertos.";
                break;
            //sintatico---------------------------------------------------------------
            case simbolo_nao_esperado:
                ret = "Erro sintático na linha "+errno+", equivoco no símbolo \'"+lexema+"\'.";
                break;
            case ponto_esperado:
                ret = "Erro sintático na linha "+errno+", um \'.\' era esperado.";
                break;
            case virgula_esperada:
                ret = "Erro sintático na linha "+errno+", uma \',\' era esperada.";
                break;
            case identificador_esperado:
                ret = "Erro sintático na linha "+errno+", um identificador era esperado.";
                break;
            case programa_esperado:
                ret = "Erro sintático na linha "+errno+", era esperado o simbolo \'programa\'.";
                break;
            case pontovirgula_esperado:
                ret = "Erro sintático na linha "+errno+", o simbolo \';\' era esperado.";
                break;
            case pontovirgulaouvirugla_esperado:
                ret = "Erro sintático na linha "+errno+", era esperado o simbolo \',\' ou \';\'.";
                break;
            case tipo_esperado:
                ret = "Erro sintático na linha "+errno+", um tipo (inteiro ou booleano) era esperado.";
                break;
            case doispontos_esperado:
                ret = "Erro sintático na linha "+errno+", era esperado o simbolo \':\'.";
                break;
            case inicio_esperado:
                ret = "Erro sintático na linha "+errno+", o simbolo \'inicio\' era esperado.";
                break;
            case abreparenteses_esperado:
                ret = "Erro sintático na linha "+errno+", o simbolo \'(\' era esperado.";
                break;
            case fechaparenteses_esperado:
                ret = "Erro sintático na linha "+errno+", o simbolo \')\' era esperado.";
                break;
            case faca_esperado:
                ret = "Erro sintático na linha "+errno+", o simbolo \'faca\' era esperado.";
                break;
            case entao_esperado:
                ret = "Erro sintático na linha "+errno+", o simbolo \'entao\' era esperado.";
                break;
            //sintatico---------------------------------------------------------------
            case duplicvar:
                ret = "Erro semântico na linha "+errno+", o nome da variavel \'"+lexema+"\' já foi utilizado anteriormente";
                break;
            case duplicidade:
                ret = "Erro semântico na linha "+errno+", o nome \'"+lexema+"\' já foi utilizado anteriormente";
                break;
            case declproc:
                ret = "Erro semântico na linha "+errno+", declaração do procedimento \'"+lexema+"\' não encontrada.";
                break;
            case declfunc:
                ret = "Erro semântico na linha "+errno+", declaração da função \'"+lexema+"\' não encontrada.";
                break;
            case declvarfunc:
                ret = "Erro semântico na linha "+errno+", varíavel ou função \'"+lexema+"\' não foi declarada.";
                break;
            case declvar:
                ret = "Erro semântico na linha "+errno+", declaração da varíavel \'"+lexema+"\' não encontrada.";
                break;
            case expressao_incompativel:
                ret = "Erro semântico na linha "+errno+", existe(m) incompatibilidade(s) de tipos na expressão.";
                break;
            case atrib_int2bool:
                ret = "Erro semântico na linha "+errno+", não é possível atribuir uma expressao booleana a \'"+lexema+"\' (inteiro).";
                break;
            case atrib_bool2int:
                ret = "Erro semântico na linha "+errno+", não é possível atribuir uma expressao inteira a \'"+lexema+"\' (booleano).";
                break;
            case exp_booleana_esperada:
                ret = "Erro semântico na linha "+errno+", após \'"+lexema+"\' era esperado uma expressão VÁLIDA de resultado booleano.";
                break;
            case err_retorno:
                ret = "Erro semântico na linha "+errno+", uma função não pode receber valores, exceto internamente em forma de retorno.";
                break;
            case err_retorno2:
                ret = "Erro semântico na linha "+errno+", a função precisa ter um retorno válido antes do fim.";
                break;
            case err_unreachable:
                ret = "Erro semântico na linha "+errno+", código após retorno inacessível.";
                break;
        }
        return ret;
    }
}
