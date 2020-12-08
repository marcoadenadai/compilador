package COMP;

import DATATYPES.Erro;
import GUI.Interface;
import java.io.File;

public final class Compilador {
    public static final Compilador INSTANCE = new Compilador();
    public static Compilador getInstance() {
        return INSTANCE;
    }

    public void executa(File arquivo){
        Interface.getInstance().printConsole("Iniciando processo de compilação (\'" + arquivo.toString() + "\')");
        GeradorCodigo.getInstance().inicializa(arquivo.toString());

        Erro L = Lexico.getInstance().load(arquivo);
        if(L.get_errno() != 0){
            Interface.getInstance().printConsole(L.get_description());
        }
        else{
            Erro S = Sintatico.getInstance().load();
            Interface.getInstance().printConsole(S.get_description());
        }
        Interface.getInstance().printConsole("");
    }

}
