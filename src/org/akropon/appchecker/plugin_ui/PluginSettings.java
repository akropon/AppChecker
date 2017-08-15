/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.akropon.appchecker.plugin_ui;

/**
 *
 * @author akropon
 */
public class PluginSettings {
	protected boolean booleanValue1;
	protected boolean booleanValue2;
	protected String stringValue;

	public PluginSettings(boolean booleanValue1, boolean booleanValue2, String stringValue) {
		this.booleanValue1 = booleanValue1;
		this.booleanValue2 = booleanValue2;
		this.stringValue = stringValue;
	}

	public boolean getBooleanValue1() {
		return booleanValue1;
	}

	public boolean getBooleanValue2() {
		return booleanValue2;
	}

	public String getStringValue() {
		return stringValue;
	}
}
