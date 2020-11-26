package COMP;

import DATATYPES.Erro;
import DATATYPES.Tok;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class Lexico {
    private ArrayList<Tok> T = new ArrayList<Tok>();
    public static final Lexico INSTANCE = new Lexico();
    public static Lexico getInstance() {
        return INSTANCE;
    }
    public Lexico(){

    }

    String TokListToString(){
        String ret = "Lista de Tokens: (Linha/Lexema/Tipo):\n";
        for(int i=0;i<T.size();i++){
            ret += "{" + T.get(i).getLine() + "," + T.get(i).getLexema() + "," + T.get(i).getSimbolo() + "}\n";
        }
        ret+="\n-----------------------";
        return ret;
    }

    public void erro(int line){
        JTextArea txtArea = new JTextArea(TokListToString());
        txtArea.setWrapStyleWord(true);
        JScrollPane scrollP = new JScrollPane(txtArea);
        scrollP.setPreferredSize(new Dimension(500,500));
        JOptionPane.showMessageDialog(null, scrollP,
                "ERRO Lexico na linha " + line,JOptionPane.ERROR_MESSAGE);
    }

    Tok PegaToken (AtomicInteger ln, int c_inicial, BufferedReader br) throws IOException {
        Tok ret = null;
        int c = c_inicial;
        String str;
        if( Character.isDigit(c) ){
            //trata digito
            str = "";
            while(Character.isDigit(c)){
                str+=(char)c;
                br.mark(1);
                c = br.read();
                br.reset();
                if(Character.isDigit(c))
                    c = br.read();
            }
            //if((char)c == '\n') {ln.set(ln.get()+1);}
            ret = new Tok(str,ln.get(),Tok.s.numero);
        }
        else if (Character.isLetter(c)){
            //trata identificador e palavra reservada
            str = "";
            while(Character.isLetter(c) || Character.isDigit(c) || (char)c == '_'){
                str+=(char)c;
                br.mark(1);
                c = br.read();
                br.reset();
                if(Character.isLetter(c) || Character.isDigit(c) || (char)c == '_')
                    c = br.read();
            }
            Tok.s tipo;
            switch (str){
                case "programa":
                    tipo = Tok.s.programa;
                    break;
                case "se":
                    tipo = Tok.s.se;
                    break;
                case "entao":
                    tipo = Tok.s.entao;
                    break;
                case "senao":
                    tipo = Tok.s.senao;
                    break;
                case "enquanto":
                    tipo = Tok.s.enquanto;
                    break;
                case "faca":
                    tipo = Tok.s.faca;
                    break;
                case "inicio":
                    tipo = Tok.s.inicio;
                    break;
                case "fim":
                    tipo = Tok.s.fim;
                    break;
                case "escreva":
                    tipo = Tok.s.escreva;
                    break;
                case "leia":
                    tipo = Tok.s.leia;
                    break;
                case "var":
                    tipo = Tok.s.var;
                    break;
                case "inteiro":
                    tipo = Tok.s.inteiro;
                    break;
                case "booleano":
                    tipo = Tok.s.booleano;
                    break;
                case "verdadeiro":
                    tipo = Tok.s.verdadeiro;
                    break;
                case "falso":
                    tipo = Tok.s.falso;
                    break;
                case "procedimento":
                    tipo = Tok.s.procedimento;
                    break;
                case "funcao":
                    tipo = Tok.s.funcao;
                    break;
                case "div":
                    tipo = Tok.s.div;
                    break;
                case "e":
                    tipo = Tok.s.e;
                    break;
                case "ou":
                    tipo = Tok.s.ou;
                    break;
                case "nao":
                    tipo = Tok.s.nao;
                    break;
                default:
                    tipo = Tok.s.identificador;
                    break;
            }
            ret = new Tok(str,ln.get(),tipo);
        }
        else if ((char)c == ':'){
            //trata atribuicao
            str = "";
            str+=(char)c;
            br.mark(1);
            c = br.read();
            br.reset();
            if((char)c == '='){
                str+=(char)c;
                ret = new Tok(str,ln.get(),Tok.s.atribuicao);
                br.read();
            }
            else{
                ret = new Tok(str,ln.get(),Tok.s.doispontos);
            }

        }
        else if ((char)c == '+' || (char)c == '-' || (char)c == '*'){
            //trata operador aritmetico
            switch((char)c){
                case '+':
                    ret = new Tok("+",ln.get(),Tok.s.mais);
                    break;
                case '-':
                    ret = new Tok("-",ln.get(),Tok.s.menos);
                    break;
                case '*':
                    ret = new Tok("*",ln.get(),Tok.s.mult);
                    break;
            }
        }
        else if ((char)c == '<' || (char)c == '>' || (char)c == '=' || (char)c == '!' ){
            //trata operador relacional
            if((char)c == '=') {
                ret = new Tok("=", ln.get(), Tok.s.ig);
            }
            else if((char)c == '<'){
                br.mark(1);
                c = br.read();
                br.reset();
                if((char)c == '='){
                    ret = new Tok("<=",ln.get(),Tok.s.menorig);
                    br.read();
                }
                else{
                    ret = new Tok("<",ln.get(),Tok.s.menor);
                }

            }
            else if((char)c == '>'){
                br.mark(1);
                c = br.read();
                br.reset();
                if((char)c == '='){
                    ret = new Tok(">=",ln.get(),Tok.s.maiorig);
                    br.read();
                }
                else {
                    ret = new Tok(">",ln.get(),Tok.s.maior);
                }
            }
            else if((char)c == '!'){
                c = br.read();
                if((char)c == '='){
                    ret = new Tok("!=",ln.get(),Tok.s.dif);
                    br.read();
                }
                /*else{
                    erro(ln.get());
                }*/
            }
        }
        else if ((char)c == ';' || (char)c == ',' || (char)c == '(' || (char)c == ')' || (char)c == '.' ){
            //trata pontuacao
            switch((char)c){
                case ';':
                    ret = new Tok(";",ln.get(),Tok.s.ponto_virgula);
                    break;
                case ',':
                    ret = new Tok(",",ln.get(),Tok.s.virgula);
                    break;
                case '(':
                    ret = new Tok("(",ln.get(),Tok.s.abre_parenteses);
                    break;
                case ')':
                    ret = new Tok(")",ln.get(),Tok.s.fecha_parenteses);
                    break;
                case '.':
                    ret = new Tok(".",ln.get(),Tok.s.ponto);
                    break;
            }
        }
        /*else{
            erro(ln.get());
        }*/
        return ret;
    }
//--------------------
    public Erro load(File prog){
        T.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(prog))) {
            int c, ln=1;
            Tok t;
            AtomicInteger LN = new AtomicInteger(0);
            String tok;
            while ((c = br.read()) != -1){
                if((char)c == '\n') {ln++;}
                while(c!=-1 && (char)c == '{' || (char)c == '/' || (char)c == ' '
                      || (char)c == '\t' || (char)c == '\r' || (char)c == '\n') {
                    //trata { comentarios }
                    if ((char) c == '{') {
                        /*int comment_count=1;//IMPLEMENTACAO CHAVES COMPOSTAS
                        while (c != -1 && comment_count > 0) {
                            c = br.read();
                            if((char)c == '{') {comment_count++;}
                            else if((char)c == '}') {comment_count--;}
                            if((char)c == '\n') {ln++;}
                        }*/
                        while (c != -1 && (char) c != '}') {
                            c = br.read();
                            if((char)c == '\n') {ln++;}
                        }
                        c = br.read();
                        if((char)c == '\n') {ln++;}
                    }
                    //trata /* comentarios */
                    if ((char)c == '/'){
                        c = br.read(); if((char)c == '\n') {ln++;}
                        if((char)c == '*'){
                            do{
                                while (c != -1 && (char) c != '*') {
                                    c = br.read();//c = br.read();
                                    if((char)c == '\n') {ln++;}
                                }//consumir qualquer coisa que nao seja um *
                                c = br.read();
                                if((char)c == '\n') {ln++;}
                            }while((char)c != '/');
                            c = br.read();//!!!
                        }
                        else{
                            /*erro(ln);*/
                            return new Erro(ln,Erro.e.simbolo_invalido);
                        }
                    }
                    //trata espaçamentos
                    if((char)c == ' ' || (char)c == '\t' || (char)c == '\r' || (char)c == '\n'){
                        while (c != -1 && (char)c == ' ' || (char)c == '\t' || (char)c == '\r' || (char)c == '\n'){
                            c = br.read();
                            if((char)c == '\n') {ln++;}
                        }
                    }
                }
                if(c!=-1) {
                    //LN.set(ln);
                    //t = PegaToken(LN, c,br);
                    //if(t==null)
                    //    return false;
                    //T.add(t);
                    //ln=LN.get();
                    LN.set(ln);
                    t = PegaToken(LN, c,br);
                    if(t!=null) {
                        T.add(t);
                        ln = LN.get();
                    }
                    else
                        return new Erro(ln,Erro.e.simbolo_invalido);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(T.size() == 0)
            return new Erro(1,Erro.e.programa_vazio);
        return new Erro(0, Erro.e.sucesso);
    }

    public Tok getToken(int index){
        if (index >= T.size())
            return null;
        return T.get(index);
    }

    public int getLength() { return T.size();}


}

