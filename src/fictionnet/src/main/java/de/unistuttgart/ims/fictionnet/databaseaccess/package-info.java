/**
 * @author Lukas Rieger
 */
package de.unistuttgart.ims.fictionnet.databaseaccess;
/*
 * The package databaseaccess contains all the necessary classes to persist
 * the data of the fictionnet application in a database.
 * The generic class AbstractDataTable provides all the methods necessary to 
 * work on data stored in one datatable.
 * The classes with the prefix DataTable_ extend the AbstractDataTable class
 * and implement more specific methods for the objects stored
 * in this table.
 * The DBAccessManager provides a facade and offers more sophisticated methods
 * such as deleteProject(). These methods use methods of the DataTable_ classes
 * to create more complex operations on various tables.
 * 
 * @author Lukas Rieger
 * @version 1.0
 */