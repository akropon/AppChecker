package org.akropon.appchecker;

import org.akropon.appchecker.plugin_ui.AppCheckerPanel;
import org.akropon.appchecker.plugin_ui.PluginSettings;
import org.akropon.appchecker.analyzing.PluginBackend;
import org.akropon.appchecker.analyzing.Defect;
import org.akropon.appchecker.defects_output_gui.DefectsTopComponent;
import org.akropon.appchecker.select_project_gui.ChooseProjectDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import static org.akropon.appchecker.plugin_ui.AppCheckerPanel.*;
import org.akropon.appchecker.progress_output_gui.ProgressOutputDialog;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;


/**
 * Class, that handles the clicking of the item in the menu that launches
 * static code analysis with AppChecker
 * 
 * @author akropon
 */
@ActionID(
		category = "Tools",
		id = "org.akropon.appchecker.AppCheckerAction"
)
@ActionRegistration(
		iconBase = "org/akropon/appchecker/res/icon_16x16.png",
		displayName = "#CTL_AppCheckerAction"
)
@ActionReference(path = "Menu/Tools", position = 1600, separatorAfter = 1650)
@Messages("CTL_AppCheckerAction=Analyze with AppChecker")
public final class AppCheckerAction implements ActionListener {
	
	/** This constant is using for sleeping periods when thread is waiting for smth*/
	public static final int WAITING_TICK_PERIOD = 50; // in milliseconds
	
	/**
	 * Launching the working algorithm when the plugin's button was clicked.
	 * 
	 * @param e - event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Launching the control part of the algorithm of work in detached thread
		Thread thread = new Thread(new Runnable() {
			public void run() {
				doIt();
			}
		});
		thread.start();
	}
	
	/**
	 * The control part of the working algorithm.
	 */
	private void doIt() {
		PluginSettings pluginSettings = getPluginSettings();
		
		Project[] projects = OpenProjects.getDefault().getOpenProjects();
		if (projects.length==0) {
			NotifyDescriptor nd = new NotifyDescriptor.Message(
					"There isn't any opened projects. Nothing to analyze.", 
					NotifyDescriptor.INFORMATION_MESSAGE);
			DialogDisplayer.getDefault().notify(nd);
			return;
		}
		
		Project project = chooseProjectForAnalyzing(projects);
		if (project == null )
			return;
		
		ArrayList<String> files = getJavaFilesFromSrc(project);
		
		ProgressOutputDialog progressOutputDialog = new ProgressOutputDialog(
				new JFrame("Analyzing"), true);
		launchProgressOutputWindow(progressOutputDialog);
		PluginBackend.setProgressOutputDialog(progressOutputDialog);
		
		ArrayList<Defect> defects = PluginBackend.analyze(files, pluginSettings);
		waitForClosingProgressOutputWindow(progressOutputDialog);
		if (defects == null)
			return;
		
		showResults(defects);
	}
	
	/**
	 * Read plugin's settings from memory and write them to object of {@link PluginSettings}
	 * 
	 * @return - pluginSettings
	 */
	private PluginSettings getPluginSettings() {
		return new PluginSettings(
				NbPreferences.forModule(AppCheckerPanel.class).getBoolean(
						KEY_BOOLEANVALUE1, DEF_BOOLEANVALUE1),
				NbPreferences.forModule(AppCheckerPanel.class).getBoolean(
						KEY_BOOLEANVALUE2, DEF_BOOLEANVALUE2),
				NbPreferences.forModule(AppCheckerPanel.class).get(
						KEY_STRINGVALUE, DEF_STRINGVALUE));
	}
	
	/**
	 * Returns paths of all java-files from "src"-folder and it's subdirectories 
	 *   in project's folder
	 * 
	 * @param project - project
	 * @return - list of paths of found java-files
	 */
	private ArrayList<String> getJavaFilesFromSrc(Project project) { 
		final String SRC = "src";
		final String JAVA_EXT = "java";
		ArrayList<String> files = new ArrayList<>();
		
		FileObject projectFolder = project.getProjectDirectory();
		FileObject srcFolder = projectFolder.getFileObject(SRC);
		
		Enumeration<? extends FileObject> filesEnum = srcFolder.getData(true);
		while (filesEnum.hasMoreElements()) {
			FileObject file =  filesEnum.nextElement();
			boolean isJava = file.getExt().compareTo(JAVA_EXT) == 0;
			if (isJava) files.add(file.getPath());
		}
		return files;
	}
	
	/**
	 * Shows window for selection project
	 * 
	 * @param projects - all projects
	 * @return - selected project
	 */
	private Project chooseProjectForAnalyzing(Project[] projects) {
		JFrame frame = new JFrame("titleName");
		ChooseProjectDialog dialog = new ChooseProjectDialog(frame, true, projects);
        dialog.setVisible(true);
		
		if (dialog.getResult() == ChooseProjectDialog.RESULT_CANCELED) 
			return null;
		
		return projects[dialog.getSelectedProjectIndex()];
	}
	
	/**
	 * Launches dialog, that shows progress of analysis and details-messages
	 * 
	 * @param dialog - dialog
	 */
	private void launchProgressOutputWindow(final ProgressOutputDialog dialog) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dialog.setVisible(true);
				/* It's OK even for modal dialogs, because (from documentation):
					"It is OK to call this method from the event dispatching thread 
					 because the toolkit ensures that other events are not blocked 
					 while this method is blocked". */
			}
		});
	}
	
	/**
	 * Shows results of analysis at extra panel in NetBeans'es Window System
	 * 
	 * @param defects - list of defects
	 */
	public void showResults(final ArrayList<Defect> defects) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				closeResentWindow();

				DefectsTopComponent defectsWindow = new DefectsTopComponent();
				defectsWindow.initTable(defects);
				Mode mode = WindowManager.getDefault().findMode("output");
				mode.dockInto(defectsWindow);
				defectsWindow.open();
				defectsWindow.requestActive();
			}
		});
	}
	
	/** 
	 * Close all opened TopComponents of class {@link DefectsTopComponents}.
	 */
	private void closeResentWindow() {
		Set<TopComponent> windows = TopComponent.getRegistry().getOpened();
		for (TopComponent window : windows)
			if (window.getClass() == DefectsTopComponent.class)
				window.close();
	}

	/**
	 * Freezes calling thread until 'progressOutputDialog' won't be closed.
	 * 
	 * @param progressOutputDialog - dialog showing progress
	 */
	private void waitForClosingProgressOutputWindow(ProgressOutputDialog progressOutputDialog) {
		while (progressOutputDialog.isVisible()) {
			try {
				Thread.sleep(WAITING_TICK_PERIOD);
			} catch (InterruptedException ex) {
				Exceptions.printStackTrace(ex);
			}
		}
	}
}
