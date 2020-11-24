package DATATYPES;

public class Tok {
    private String lexema;
    public enum s{
        programa,
        inicio,
        fim,
        procedimento,
        funcao,
        se,
        entao,
        senao,
        enquanto,
        faca,
        atribuicao,
        escreva,
        leia,
        var,
        inteiro,
        booleano,
        identificador,
        numero,
        ponto,
        ponto_virgula,
        virgula,
        abre_parenteses,
        fecha_parenteses,
        maior,
        maiorig,
        ig,
        menor,
        menorig,
        dif,
        mais,
        menos,
        mult,
        div,
        e,
        ou,
        nao,
        doispontos,
        verdadeiro,
        falso,
        //
        positivo,
        negativo,
    };
    private s simbolo;
    private int line;

    public Tok (String lex, int ln){
        lexema=lex;
        line=ln;
    }

    public Tok (String lex, int ln, s tipo){
        lexema=lex;
        line=ln;
        simbolo=tipo;
    }

    public String getLexema() {return lexema;}
    public s getSimbolo(){ return simbolo;}
    public int getLine(){ return line;}

    public void setLexema(String lex) {lexema=lex;}
    public void setSimbolo(s tipo) {simbolo=tipo;}
    public void setLine(int ln) {line=ln;}

}
