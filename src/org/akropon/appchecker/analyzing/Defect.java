/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.akropon.appchecker.analyzing;

/**
 *
 * @author akropon
 */
public class Defect {
	protected String file;
	protected int line;
	protected String name;
	protected String description;

	public Defect(String file, int line, String name, String description) {
		this.file = file;
		this.line = line;
		this.name = name;
		this.description = description;
	}

	public String getFile() {
		return file;
	}

	public int getLine() {
		return line;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Object getParameter(int parameterIndex) {
		switch (parameterIndex) {
			case 0: return file;
			case 1: return line;
			case 2: return name;
			case 3: return description;
			default: return null;
		}
	}
}
