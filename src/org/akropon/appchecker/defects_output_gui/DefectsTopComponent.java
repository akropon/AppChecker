/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.akropon.appchecker.defects_output_gui;

import java.io.File;
import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
//import org.akropon.appchecker.Bundle;
import org.akropon.appchecker.analyzing.Defect;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Line;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.nodes.Node;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
		dtd = "-//org.akropon.appchecker//Defects//EN",
		autostore = false
)
@TopComponent.Description(
		preferredID = "DefectsTopComponent",
		iconBase = "org/akropon/appchecker/icon_16x16.png",
		persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.akropon.appchecker.DefectsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
		displayName = "#CTL_DefectsAction",
		preferredID = "DefectsTopComponent"
)
@Messages({
	"CTL_DefectsAction=Defects",
	"CTL_DefectsTopComponent=[AppChecker] Defects",  // title of new Window
	"HINT_DefectsTopComponent=This is a Defects window"
})
public final class DefectsTopComponent extends TopComponent {

	String[] columnNames = {"File",
							"Line",
							"Defect",
							"Description"};
	
	public DefectsTopComponent() {
		initComponents();
		setName(Bundle.CTL_DefectsTopComponent());
		setToolTipText(Bundle.HINT_DefectsTopComponent());
		
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				
				if (evt.getClickCount() != 2) return;
				
				int rowIndex = table.rowAtPoint(evt.getPoint());
				int columnIndex = table.columnAtPoint(evt.getPoint());
				if (rowIndex >= 0 && columnIndex >= 0) {
					Defect defect = ((TableModelForDefects)table.getModel()).getDefectAt(rowIndex);
					
					int lineNumber=defect.getLine();
					int colNumber=0;
					
					File file = new File(defect.getFile());
					FileObject fileObject = FileUtil.toFileObject(file);
					DataObject dataObject = null;
					try {
						dataObject = DataObject.find(fileObject);
					} catch (DataObjectNotFoundException ex) {
						ex.printStackTrace();
					}
					if (dataObject != null)
					{
						LineCookie lc = dataObject.getLookup().lookup(LineCookie.class);
						if (lc == null) {/* cannot do it */ return;}
						Line line = lc.getLineSet().getOriginal(lineNumber);
						//line.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FOCUS);
						line.show(Line.ShowOpenType.OPEN, 
								Line.ShowVisibilityType.FOCUS, colNumber);
					}
				}
			}
		});
	}
	
	public void initTable(ArrayList<Defect> defects) {
		table.setModel(new TableModelForDefects(defects));
	}


	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
	@Override
	public void componentOpened() {
		super.componentOpened();
	}

	@Override
	public void componentClosed() {
		super.componentClosed();
	}

	void writeProperties(java.util.Properties p) {
		// better to version settings since initial version as advocated at
		// http://wiki.apidesign.org/wiki/PropertyFiles
		p.setProperty("version", "1.0");
		// TODO store your settings
	}

	void readProperties(java.util.Properties p) {
		String version = p.getProperty("version");
		// TODO read your settings according to their version
	}

	@Override
	public int getPersistenceType() {
		return PERSISTENCE_NEVER;
	}
	
	
}
