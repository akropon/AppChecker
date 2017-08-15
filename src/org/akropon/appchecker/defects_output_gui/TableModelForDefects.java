package org.akropon.appchecker.defects_output_gui;

import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.akropon.appchecker.analyzing.Defect;

/** 
 * Class, that contains defects data and displays it in any object of {@link javax.swing.JTable}.
 *
 * @author akropon
 */
public class TableModelForDefects implements TableModel {
	
	private final String[] columnNames = {  "File",
											"Line",
											"Defect",
											"Description"};
	private final Class[] columnClasses = {	String.class,
											Integer.class,
											String.class,
											String.class};
	
	private ArrayList<Defect> defects;

	public TableModelForDefects(ArrayList<Defect> defects) {
		this.defects = defects;
	}
	
	public Defect getDefectAt(int rowIndex) {
		if (rowIndex<0 || rowIndex>=getRowCount()) return null;
		return defects.get(rowIndex);
	}
	
	@Override
	public int getRowCount() {
		return defects.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Defect defect = defects.get(rowIndex);
		switch (columnIndex) {
			case 0: return defect.getFile();
			case 1: return defect.getLine()+1;
			case 2: return defect.getName();
			case 3: return defect.getDescription();
			default: return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}
	
}
