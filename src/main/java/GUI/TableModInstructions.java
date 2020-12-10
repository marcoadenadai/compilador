package GUI;
import DATATYPES.InstructionData;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;


public class TableModInstructions extends DefaultTableModel {
    public static final String[] columnNames = {"P[i]", "Instruction", "Att. 1", "Att. 2", "Comment"};
    private int count=0;

    public TableModInstructions() {
        super(columnNames, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
            return getValueAt(0, columnIndex).getClass();
        }
        return super.getColumnClass(columnIndex);
    }


    public void clear(){
        count=0;
    }


    public void addRow(InstructionData instructionData) {
        if (instructionData == null) {
            throw new IllegalArgumentException("rowData cannot be null");
        }
        Vector<Object> rowVector = new Vector<>();
        rowVector.add(count);
        rowVector.add(instructionData.get_mnemonic());
        if(instructionData.get_att1()==null)
            rowVector.add("");
        else
            rowVector.add(instructionData.get_att1());
        if(instructionData.get_att2()==null)
            rowVector.add("");
        else
            rowVector.add(instructionData.get_att2());
        rowVector.add(instructionData.get_comment());
        super.addRow(rowVector);
        count++;
    }

    public boolean isCellEditable(int row, int col) {
        if (col > 3)
            return true;
        else
            return false;
    }

}
