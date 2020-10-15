package COMP;

import DATATYPES.Erro;

import java.io.File;

public final class Compilador {
    public static final Compilador INSTANCE = new Compilador();
    public static Compilador getInstance() {
        return INSTANCE;
    }

    public void executa(File arquivo){
        Erro L = Lexico.getInstance().load(arquivo);
        System.out.println("Lexico returned " + + L.get_errno() + " , desc: " + L.get_description());
        Erro S = Sintatico.getInstance().load();
        System.out.println("Sintatico returned " + S.get_errno() + " , desc: " + S.get_description());

    }

}
