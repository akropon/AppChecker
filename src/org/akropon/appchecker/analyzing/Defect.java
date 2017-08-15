package org.akropon.appchecker.analyzing;

/**
 * Class describing code defect.
 *
 * @author akropon
 */
public class Defect {
	/** file path */
	protected String file;
	/** number of line in corresponding file */
	protected int line;
	/** name of the defect */
	protected String name;
	/** description of the defect */
	protected String description;

	/**
	 * Creates defect with preset parameters.
	 * 
	 * @param file - file path
	 * @param line - number of line in corresponding file
	 * @param name - name of the defect
	 * @param description - description of the defect
	 */
	public Defect(String file, int line, String name, String description) {
		this.file = file;
		this.line = line;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return file path
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @return number of line in corresponding file
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return name of the defect
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return description of the defect
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Return parameter with corresponding number:
	 *  0 - file
	 *  1 - line
	 *  2 - name
	 *  3 - description
	 * 
	 * @param parameterIndex - number
	 * @return parameter as Object if success, null - if number is less then 0 or bigger than 3
	 */
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
