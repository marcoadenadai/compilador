package DATATYPES;

public class Erro {
    public enum e{
        sucesso,
        vazio,
        //lexico
        simbolo_invalido,
        programa_vazio,
        //sintatico
        miss_sprograma,
        miss_sidentificador,
        miss_svirgula,
        miss_sponto,
        miss_sponto_x,
        todo,
        miss_spontovirgula,
        miss_sdoispontos_x,
        miss_sdoispontos_ou_virgula,
        miss_stipo,
        miss_sdoispontos,
        miss_sfechaparenteses,
        miss_sabreparenteses,
        miss_sfaca,
        miss_sentao,
        miss_fator,
        miss_sinicio,


        //semantico
    };
    private e type;
    private int errno;

    public Erro(int errno, e type){
        this.errno = errno;
        this.type = type;
    }

    /*public Erro(int errno){
        this.errno = errno;
        this.type = e.vazio;
    }*/

    public int get_errno(){ return errno; }
    //public e get_etype() {return type;}

    //ERROR DESCRIPTION
    public String get_description(){
        String ret=null;
        switch (type){
            case vazio:
                break;
            case sucesso:
                ret = "sucesso, não houve erros na execução";
                break;
            case simbolo_invalido:
                ret = "símbolo inválido, não está contido na lista de palavras conhecidas";
                break;
            case programa_vazio:
                ret = "programa vazio, verifique se não existem comentários abertos";
                break;
            case miss_sprograma:
                ret = "o símbolo \'programa\' era esperado";
                break;
            case miss_sidentificador:
                ret = "um identificador era esperado";
                break;
            case miss_svirgula:
                ret = "o símbolo \',\' era esperado";
                break;
            case miss_sponto:
                ret = "o símbolo \'.\' era esperado";
                break;
            case miss_sponto_x:
                ret = "o símbolo \'.\' não era esperado";
                break;
            case todo:
                ret = "erro todo";
                break;
            case miss_spontovirgula:
                ret = "o símbolo \';\' era esperado";
                break;
            case miss_sdoispontos_ou_virgula:
                ret = "o símbolo \':\' ou o símbolo \',\' era esperado";
                break;
            case miss_sdoispontos_x:
                ret = "o símbolo \':\' não era esperado";
                break;
            case miss_stipo:
                ret = "o símbolo 'inteiro' ou 'booleano' era esperado";
                break;
            case miss_sdoispontos:
                ret = "o símbolo \':\' era esperado";
                break;
            case miss_sfechaparenteses:
                ret = "o símbolo \')\' era esperado";
                break;
            case miss_sabreparenteses:
                ret = "o símbolo \'(\' era esperado";
                break;
            case miss_sfaca:
                ret = "o símbolo \'faca\' era esperado";
                break;
            case miss_sentao:
                ret = "o símbolo \'entao\' era esperado";
                break;
            case miss_sinicio:
                ret = "o símbolo \'inicio\' era esperado";
                break;
            case miss_fator:
                ret = "o símbolo \'identificador\' ou \'numero\' \'nao\' ou \'(\' ou \'verdadeiro\' ou \'falso\' era esperado";
            default:
                break;
        }
        return ret;
    }
}
