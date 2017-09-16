package com.xhsoft.lab.pageingtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by zhangxh on 2017/9/14.
 */
abstract public class PagingTest {

  protected final DatabaseSource databaseSource;

  protected PagingTest() {
    this.databaseSource = new DatabaseSource();
  }

  abstract public void queryByPagingSql(int limit, int offset) throws SQLException;

  abstract public void queryByPagingJdbc(int limit, int offset) throws SQLException;

  abstract public void queryBySeekSql(int limit, int offset) throws SQLException;

  protected Connection getConnection() throws SQLException {
    if (databaseSource == null) {
      throw new RuntimeException("数据源必须指定");
    }
    return DriverManager.getConnection(databaseSource.url(), databaseSource.getUsername(), databaseSource.getPassword());
  }

  protected void printResultSet(ResultSet rs) throws SQLException {
    System.out.println(rs.getString(1));
  }

  protected HashMap<Integer, String> readFromResultSet(ResultSet rs) throws SQLException {
    HashMap<Integer, String> values = new HashMap<>();
    values.put(1, rs.getString(1));
    values.put(2, rs.getString(2));
    values.put(3, rs.getString(3));
    values.put(4, rs.getString(4));
    values.put(5, rs.getString(5));
    values.put(6, rs.getString(6));
    values.put(7, rs.getString(7));
    values.put(8, rs.getString(8));
    values.put(9, rs.getString(9));
    values.put(10, rs.getString(10));
    values.put(11, rs.getString(11));
    return values;
  }
}
