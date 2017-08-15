package org.akropon.appchecker.plugin_ui;

/**
 * Represents container for settings for plugin.
 * 
 * @author akropon
 */
public class PluginSettings {
	/** example value */
	protected boolean booleanValue1;
	/** example value */
	protected boolean booleanValue2;
	/** example value */
	protected String stringValue;

	/** Constructor. */
	public PluginSettings(boolean booleanValue1, boolean booleanValue2, String stringValue) {
		this.booleanValue1 = booleanValue1;
		this.booleanValue2 = booleanValue2;
		this.stringValue = stringValue;
	}

	/** getter. */
	public boolean getBooleanValue1() {
		return booleanValue1;
	}

	/** getter. */
	public boolean getBooleanValue2() {
		return booleanValue2;
	}

	/** getter. */
	public String getStringValue() {
		return stringValue;
	}
}
