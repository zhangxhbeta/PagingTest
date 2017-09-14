package com.xhsoft.lab.pageingtest;

import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

/**
 * Oracle 测试
 * <p>
 * Created by zhangxh on 2017/9/14.
 */
public class OraclePagingTest extends PagingTest {

  public OraclePagingTest() {
    super();
    databaseSource.setType(DatabaseSource.TYPE_ORACLE);
    databaseSource.setIp("192.168.50.186");
    databaseSource.setName("");
    databaseSource.setUsername("KSSUPER");
    databaseSource.setPassword(""); // 改为你自己的
    databaseSource.setServiceId("ORCL");
    databaseSource.setPort(1521);
  }


  public void queryByPagingSql(int limit, int offset) throws SQLException {

    // 打开连接
    try (Connection con = getConnection()) {
      PreparedStatement statement = con.prepareStatement("SELECT * FROM (" +
          "  SELECT * " +
          "  FROM " +
          "    (SELECT /*+ INDEX_DESC(ut SYS_C0010814) */ ut.*, ROWNUM RN FROM USERTABLE ut order by YCSB_KEY desc) " +
          "     WHERE ROWNUM <= ?) " +
          "WHERE RN > ?");

      statement.setInt(1, limit);
      statement.setInt(2, offset);

      statement.setFetchSize(limit - offset);
      statement.setMaxRows(limit - offset);

      try (ResultSet rs = statement.executeQuery()) {
        // 循环遍历
        Vector<HashMap<Integer, String>> result = new Vector<>();
        while (rs.next()) {
          result.add(readFromResultSet(rs));
        }
      }
    }

  }

  @SuppressWarnings("Duplicates")
  public void queryByPagingJdbc(int limit, int offset) throws SQLException {
    try (Connection con = getConnection()) {
      PreparedStatement statement = con.prepareStatement("SELECT ut.*, ROWNUM RN FROM USERTABLE ut", ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);

      statement.setMaxRows(limit);
      statement.setFetchSize(limit);

      try (ResultSet rs = statement.executeQuery()) {

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
