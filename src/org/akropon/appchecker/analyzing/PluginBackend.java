/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.akropon.appchecker.analyzing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javax.swing.SwingUtilities;
import org.akropon.appchecker.plugin_ui.PluginSettings;
import org.akropon.appchecker.progress_output_gui.ProgressOutputDialog;

/**
 *
 * @author akropon
 */
public class PluginBackend {
	private static PluginSettings settings;
	private static ArrayList<String> files;
	private static ArrayList<Defect> defects;
	
	private static boolean isPerforming = false;
	private static boolean isCancelled = false;
	
	private static ProgressOutputDialog progressOutputDialog = null;
	
	public static ArrayList<Defect> analyze(
			ArrayList<String> files, 
			PluginSettings settings) {
		// Preparing
		isPerforming = true;
		isCancelled = false;
		PluginBackend.settings = settings;
		PluginBackend.files = files;
		defects = new ArrayList<Defect>();
		
		// Interaction with the dialog of class ProgressOutputDialog
		setProgress(0);
		sendMessage("settings.booleanValue1"+settings.getBooleanValue1());
		sendMessage("settings.booleanValue2"+settings.getBooleanValue2());
		sendMessage("settings.stringValue"+settings.getStringValue());
		sendMessage("Analysis began.");
		
		// Checking for being canceled
		if (isCancelled) { isPerforming=false; return null; }
			
		// Analysis
		if (customAnalyze() == false) {
			isPerforming=false; 
			return null;
		}
		
		// Completion
		if (isCancelled) { isPerforming=false; return null; }
		sendMessage("Analysis finished.");
		setProgress(100);
		isPerforming = false;
		return defects;
	}

	/**
	 * Custom analysis.
	 * 
	 * Method that performs directly analysis.
	 * Change this code to organize your analysis.
	 * Add all found defects to variable "defects"
	 * 
	 * @return - false, if was interrupted by {@link isCancelled} or smth else;
	 *           true, if finished successful
	 */
	public static boolean customAnalyze() {
		// TODO - write your code here
		// Delete the following code before writing the useful code
		
		// Emitaion of analysis BEGIN
		int errors_per_file = 3;
		int full_time = 2000; // ms
		int tick_duration = full_time / files.size() / errors_per_file;
		double progress_increment = 100 / files.size() / errors_per_file;
		double current_progress = 0;
		
		
		for (String file : files) {
			sendMessage("Analysing of file "+file);
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				int numOfLines = 0;
				while (reader.readLine() != null)
					numOfLines++;
				
				for (int i=0; i<errors_per_file; i++) {
					defects.add(new Defect(
							file, 
							i*numOfLines/errors_per_file, 
							"Error #"+i, 
							"Description #"+i));
					Thread.sleep(tick_duration);
					current_progress += progress_increment;
					setProgress((int)current_progress);
					sendMessage("found Error #"+i);
				}
				
				reader.close();
				if (isCancelled) { return false; }
			} catch (Exception exc) {
				return false;
			}
		}
		// Emitaion of analysis END
		return true;
	}
	
	
	public static void setProgressOutputDialog(ProgressOutputDialog progressOutputDialog) {
		PluginBackend.progressOutputDialog = progressOutputDialog;
	}
	
	public static boolean cancel() {
		if (isPerforming) {
			isCancelled = true;
			return true;
		}
		return false;
	}

	public synchronized static boolean isPerforming() {
		return isPerforming;
	}

	public synchronized static boolean isCancelled() {
		return isCancelled;
	}
	
	/**
	 * Allows to control object of class {@link ProgressOutputDialog}
	 * 
	 * @param progressValue - value in [0,100]
	 */
	private static void setProgress(int progressValue) {
		if (progressOutputDialog != null) {
			progressOutputDialog.setProgress(progressValue);
		}
	}
	
	/**
	 * Send message to show it in textbox in object of class {@link ProgressOutputDialog}
	 * 
	 * @param msg - message
	 */
	private static void sendMessage(String msg) {
		if (progressOutputDialog != null) {
			progressOutputDialog.writeMessage(msg);
		}
	}
	
	
	
}
