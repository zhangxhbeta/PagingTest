package com.xhsoft.lab.pageingtest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 * MySql 翻页测试
 * Created by zhangxh on 2017/9/14.
 */
public class MysqlPagingTest extends PagingTest {

  public MysqlPagingTest() {
    super();
    databaseSource.setType(DatabaseSource.TYPE_MYSQL);
    databaseSource.setIp("192.168.50.171");
    databaseSource.setName("");
    databaseSource.setDatabaseName("ycsb_test");
    databaseSource.setUsername("hadoop");
    databaseSource.setPassword(""); // 改为自己的
    databaseSource.setPort(3306);
  }

  @Override
  public void queryByPagingSql(int limit, int offset) throws SQLException {
    String sql = "SELECT ut.* FROM USERTABLE ut order by ycsb_key limit ?, ?";

    try (Connection con = getConnection()) {
      PreparedStatement statement = con.prepareStatement(sql);

      statement.setInt(1, offset / (limit - offset));
      statement.setInt(2, limit - offset);

      statement.setFetchSize(limit - offset);
      statement.setMaxRows(limit - offset);

      try (ResultSet rs = statement.executeQuery()) {
        // 循环遍历
        Vector<HashMap<Integer, String>> result = new Vector<>();
        while (rs.next()) {
          result.add(readFromResultSet(rs));
          // printResultSet(rs);
        }
      }
    }
  }

  @SuppressWarnings("Duplicates")
  @Override
  public void queryByPagingJdbc(int limit, int offset) throws SQLException {
    String sql = "SELECT ut.* FROM USERTABLE ut";

    try (Connection con = getConnection()) {
      PreparedStatement statement = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);

      statement.setMaxRows(limit);
      statement.setFetchSize(limit);

      try (ResultSet rs = statement.executeQuery()) {

        // mysql
        int i = offset + 1;
        Vector<HashMap<Integer, String>> result = new Vector<>();

        if (rs.absolute(i)) {
          do {
            result.add(readFromResultSet(rs));
            // printResultSet(rs);
          } while (rs.next() && i++ < limit);
        }
      }
    }
  }
}
