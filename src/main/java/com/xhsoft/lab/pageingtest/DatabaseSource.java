package com.xhsoft.lab.pageingtest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author zhangxh
 */
@Data
public class DatabaseSource {

  public static final int TYPE_MYSQL = 0;

  public static final int TYPE_SQL_SERVER = 1;

  public static final int TYPE_ORACLE = 2;

  /**
   * 主键
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long sourceId;

  /**
   * 数据源名称
   */
  private String name;

  /**
   * 数据库类型
   */
  private Integer type;

  /**
   * ip 地址
   */
  private String ip;

  /**
   * 端口
   */
  private Integer port;

  /**
   * 服务 id （Oracle SID／Service Name）
   * 同时也是 SqlServer 的实例名
   */
  private String serviceId;

  /**
   * 数据库名 （SqlServer 数据库，MySql 也有这个概念）
   */
  private String databaseName;

  /**
   * 用户名
   */
  private String username;

  /**
   * 密码
   */
  private String password;

  String testConnectionSql() {
    if (type == TYPE_ORACLE) {
      return "select 1 from dual";
    } else if (type == TYPE_SQL_SERVER) {
      return "";
    } else {
      return "select 1";
    }
  }

  /**
   * 获取连接字符串
   * @return
   */
  public String url() {
    if (type == TYPE_ORACLE) {
      // "jdbc:oracle:thin:@localhost:1521:serviceId";
      return String.format("jdbc:oracle:thin:@%s:%s:%s", ip, port, serviceId);

    } else if (type == TYPE_SQL_SERVER) {
      // "jdbc:sqlserver://localhost\\instanceName:3333;databaseName=sampleDb";
      return String.format("jdbc:sqlserver://%s\\%s:%s;databaseName=%s", ip, serviceId, port, databaseName);
    } else {
      // jdbc:mysql://localhost:3306/databaseName
      return String.format("jdbc:mysql://%s:%s/%s?useSSL=false", ip, port, databaseName);
    }
  }

  String driver() {
    if (type == DatabaseSource.TYPE_ORACLE) {
      return "oracle.jdbc.driver.OracleDriver";
    } else if (type == DatabaseSource.TYPE_SQL_SERVER) {
      return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    } else {
      return "com.mysql.jdbc.Driver";
    }
  }
}
