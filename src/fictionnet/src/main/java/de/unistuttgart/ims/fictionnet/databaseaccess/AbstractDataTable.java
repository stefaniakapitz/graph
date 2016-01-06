package de.unistuttgart.ims.fictionnet.databaseaccess;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
/**
 * @author Lukas Rieger
 * @version 17-10-2015
 * 
 * The class DataTable is the superclass of all datatables used in the database.
 * It offers methods to manipulate the data in the database on the table 
 * that is specified by the generic parameters.
 * T is the java class that is persisted
 * in the table. K is the class of the key or id value used for the class.
 * This class is abstract since it must be extended by a class that represents
 * a table from the database so that it can provide the generic parameters.
 * 
 * @param <T> class of objects to persist in the table
 * @param <K> class of key values
 */
public abstract class AbstractDataTable<T, K> {
	/**
	 *  Database connection: can handle several threads.
	 */
	protected ConnectionSource connectionSource;
	/**
	 *  AOLite's data access object
	 */
	protected transient Dao<T, K> dao;
	/**
	 *  Type of class that will be stored in the concrete implementation.
	 */
	protected transient final Class<T> type;
	/**
	 * Builds delete queries on the table.
	 */
	protected transient DeleteBuilder<T, K> deleteBuilder;
	/**
	 * Builds data queries on the table.
	 */
	protected transient QueryBuilder<T, K> queryBuilder;
	
	/**
	 * protected method.
	 * @param type
	 */
	protected AbstractDataTable(final Class<T> type){
		this.type = type;
	}
	
	/**
	 * This method sets the connection source so that
	 * the class knows in which database the table is stored.
	 * 
	 * @param connection string
	 * @throws SQLException 
	 */
	protected void setConnectionSource(final ConnectionSource connection) throws SQLException {
		this.connectionSource = connection;
		this.dao = DaoManager.createDao(connectionSource, type);
		this.deleteBuilder = dao.deleteBuilder();
		this.queryBuilder = dao.queryBuilder();
	}
	
	/**
	 * Getter for connectionSource attribute.
	 * 
	 * @return connectionSource
	 */
	protected ConnectionSource getConnectionSource() {
		return this.connectionSource;
	}
	
	/**
	 * Returns the dao. A dao is an object provided by ORMLite that
	 * allows to manipulate data in the table.
	 * 
	 * @return dao
	 */
	protected Dao<T, K> getDao() {
		return this.dao;
	}
	
	/**
	 * Performs a query for id on the datatable.
	 * 
	 * @param id
	 * @return T 
	 * @throws SQLException
	 */
	public T queryForId(final K id) throws SQLException {
		T result = null;
		if(dao != null) {
			result =  dao.queryForId(id);
		}
		return result;
	}
	
	/**
	 * Creates or updates the table data for the given object.
	 * 
	 * @param data
	 * @return boolean for success
	 * @throws SQLException
	 */
	public boolean createOrUpdate(final T data) throws SQLException {
		boolean result = false;
		CreateOrUpdateStatus status;
		if(dao != null) {
			status = dao.createOrUpdate(data);
			result = status.getNumLinesChanged() == 1;
		}
		return result;
	}
	
	/**
	 * Creates a new entry for the given data
	 * 
	 * @param data
	 * @return
	 * @throws SQLException
	 */
	public boolean create(final T data) throws SQLException {
		boolean result = false;
		int status;
		if(dao != null) {
			status = dao.create(data);
			result = status == 1;
		}
		return result;
	}
	
	/**
	 * Updates the table data for the given object.
	 * 
	 * @param data
	 * @return
	 * @throws SQLException
	 */
	public boolean update(final T data) throws SQLException {
		boolean result = false;
		if(dao != null) {
			final int changedRowsCount = dao.update(data);
			result = changedRowsCount == 1;
		}
		return result;
	}
	
	/**
	 * Deletes the row of the data indicated by the id.
	 * 
	 * @param objectId
	 * @return boolean for success
	 * @throws SQLException
	 */
	public boolean deleteById(final K objectId) throws SQLException {
		boolean result = false;
		if(dao != null) {
			final int deletedRowsCount = dao.deleteById(objectId);
			result = deletedRowsCount == 1;
		} 
		return result;
	}
	
	/**
	 * Deletes the row of the data in the datatable for
	 * the given object.
	 * @param data
	 * @throws SQLException
	 * @throws Exception 
	 */
	public boolean delete(final T data) throws SQLException {
		boolean result = false;
		if(dao != null) {
			final int deletedRowsCount = dao.delete(data);
			result = deletedRowsCount == 1;
		}
		return result;
	}
	
	/**
	 * Returns the beginning of a delete query.
	 * Can be used to build queries to perform more complicated delete
	 * queries on the table.
	 * Example: DataTableSomethings.deleteFromThisAllWhere().eq(columnName, value);
	 * 
	 * @return Where<T, K>
	 */
	public Where<T, K> deleteFromThisAllWhere() {
		if(deleteBuilder != null) {
			return deleteBuilder.where();
		}
		return null;
	}
	
	/**
	 * Executes the delete query from the stored in the deletebuilder.
	 * 
	 * @return boolean success
	 * @throws SQLException 
	 */
	public boolean executeDelete() throws SQLException {
		int deletedRowsCount = 0;
		boolean result;
		deletedRowsCount = deleteBuilder.delete();
		result = deletedRowsCount > 0;
		return result;
	}
	
	/**
	 * Returns the beginning of a query.
	 * Can be used to build queries to perform more complicated queries on the table.
	 * Example: DataTableSomethings.selectFromThisAllWhere().eq(columnName, value).
	 * 
	 * @return Where<T, K>
	 */
	public Where<T, K> selectFromThisAllWhere() {
		if(queryBuilder != null) {
			return queryBuilder.where();
		}
		return null;
	}
}
