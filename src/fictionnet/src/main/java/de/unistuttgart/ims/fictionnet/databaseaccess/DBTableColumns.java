package de.unistuttgart.ims.fictionnet.databaseaccess;

/**
 * @author Lukas Rieger, Domas Mikalkinas
 * @version 27-10-2015
 * 
 * Enum with all the column names. If a column name of a databasetable changes
 * change it here globally.
 *
 */
public enum DBTableColumns {
	USEREMAIL("userEmail"),
	OWNEREMAIL("ownerEmail"),
	PROJECTID("projectId"),
	CORPUSID("corpusId"),
	PROJECTNAME("projectName"),
	TEXTID("textId"),
	CONTAINERID("containerId"),
	LAYERCONTENTID("parentId");
	
	/**
	 * Database column names.
	 * See class annotations for tablenames.
	 */
	private final String columnName;

	/**
	 * Constructor.
	 * @param columnName
	 */
	private DBTableColumns(final String columnName) {
		this.columnName = columnName;
	}

	/**
	 * Returns the name of the column as a string.
	 */
	@Override
	public String toString() {
		return columnName;
	}
}
