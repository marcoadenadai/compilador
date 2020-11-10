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
        tipo_esperado,
        doispontos_esperado,
        inicio_esperado,
        fechaparenteses_esperado,
        abreparenteses_esperado,
        faca_esperado,
        entao_esperado,

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
                ret = "sucesso, não houve erros na execução";
                break;
            //lexico------------------------------------------------------------------
            case simbolo_invalido:
                ret = "Erro lexical encontrado na linha "+errno+", o símbolo \'"+lexema+"\' não é válido.";
                break;
            case programa_vazio:
                ret = "Erro encontrado na linha "+errno+", programa vazio. " +
                        "Verifique se não existem comentários abertos.";
                break;
            //sintatico---------------------------------------------------------------
            case simbolo_nao_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", equivoco no símbolo \'"+lexema+"\'.";
                break;
            case ponto_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", um \'.\' era esperado.";
                break;
            case virgula_esperada:
                ret = "Erro sintático encontrado na linha "+errno+", uma \',\' era esperada.";
                break;
            case identificador_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", um identificador era esperado.";
                break;
            case programa_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", era esperado o simbolo \'programa\'.";
                break;
            case pontovirgulaouvirugla_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", era esperado o simbolo \',\' ou \';\'.";
                break;
            case tipo_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", um tipo (inteiro ou booleano) era esperado.";
                break;
            case doispontos_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", era esperado o simbolo \':\'.";
                break;
            case inicio_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", o simbolo \'inicio\' era esperado.";
                break;
            case abreparenteses_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", o simbolo \'(\' era esperado.";
                break;
            case fechaparenteses_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", o simbolo \')\' era esperado.";
                break;
            case faca_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", o simbolo \'faca\' era esperado.";
                break;
            case entao_esperado:
                ret = "Erro sintático encontrado na linha "+errno+", o simbolo \'entao\' era esperado.";
                break;
        }
        return ret;
    }
}
