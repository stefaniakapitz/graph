package de.unistuttgart.ims.fictionnet.databaseaccess;

import com.j256.ormlite.db.MysqlDatabaseType;

/**
 * @author Erol Aktay
 * @version 23-09-2015
 * 
 * This allows ORMLite to use the MariaDB JDBC driver instead of the MySQL one
 * 
 */
public class MariaDBType extends MysqlDatabaseType {

  private final static String DATABASE_URL_PORTION = "mariadb";
  private final static String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
  private final static String DATABASE_NAME = "MariaDB";
  
  @Override
  public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
    return DATABASE_URL_PORTION.equals(dbTypePart);
  }

  /**
   * Checks database type.
   * @param url
   * @return
   */
  public boolean isDatabaseUrlThisType(final String url) {
    return url.startsWith("jdbc:" + DATABASE_URL_PORTION);
  }

  @Override
  protected String getDriverClassName() {
    return DRIVER_CLASS_NAME;
  }

  @Override
  public String getDatabaseName() {
    return DATABASE_NAME;
  }
}