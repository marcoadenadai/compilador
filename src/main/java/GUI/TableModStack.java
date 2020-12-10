package GUI;

import DATATYPES.InstructionData;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;


public class TableModStack extends DefaultTableModel {
    public static final String[] columnNames = {"M[s]", "Value"};
    public TableModStack() {
        super(columnNames, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
            return getValueAt(0, columnIndex).getClass();
        }
        return super.getColumnClass(columnIndex);
    }


    public void addRow(int x, int value) {
        Vector<Object> rowVector = new Vector<>();
        rowVector.add(x);
        rowVector.add(value);
        super.addRow(rowVector);
    }

    public boolean isCellEditable(int row, int col) {
            return false;
    }

}
