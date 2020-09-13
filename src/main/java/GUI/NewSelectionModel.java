package GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

public class NewSelectionModel extends DefaultListSelectionModel {
    private int i=0;
    public NewSelectionModel(){
        super();
        i=0;
        super.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    //tlvz fazer algo assim para tudo que mexe na seleção e fodase
    //ou bloquear tudo que nao for MAQUINA-ROOT HARDCODED
    @Override
    public void setSelectionInterval(int index0, int index1){ }
    @Override
    public void addSelectionInterval(int index0, int index1){ }
    @Override
    public void removeSelectionInterval(int index0, int index1){ }
    @Override
    public void setAnchorSelectionIndex(int x){ }
    @Override
    public void setLeadSelectionIndex(int x){ }
    //@Override
    //public void clearSelection() { }
    @Override
    public void setSelectionMode(int selectionMode) {
        super.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void updateSelection(){
        super.setSelectionInterval(i,i);
    }
    public void inc() { i++; }
    public void set(int x) {i=x;}
    public void dec() { i--; }
}