package DATATYPES;

public class ProcedimentoOuPrograma extends Simbolo {
    private int rotulo;
    public ProcedimentoOuPrograma(String lexema, int escopo) {
        super(lexema, escopo);
    }

    public ProcedimentoOuPrograma(String lexema, int escopo, int rotulo) {
        super(lexema, escopo);
        this.rotulo = rotulo;
    }

    public int getRotulo() {return rotulo;}
    public int setRotulo() {return rotulo;}
}
