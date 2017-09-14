package com.xhsoft.lab.pageingtest;

import java.sql.SQLException;

/**
 * 测试不同的数据库对 Jdbc 形式翻页的支持是否良好
 * <p>
 * Created by zhangxh on 2017/9/14.
 */
public class App {

  public static void main(String[] args) {

    int testcount = 100;
    int pageSize = 10;
    long start;
    long firstTimeElapsed = 0;

    try {
      PagingTest pagingTest = new MysqlPagingTest();

      long elapsedTime = 0;
      for (int i = 1; i <= testcount; i++) {
        start = System.nanoTime();

        pagingTest.queryByPagingSql(pageSize * i, pageSize * (i - 1));
        // pagingTest.queryByPagingJdbc(pageSize * i, pageSize * (i - 1));

        if (i == 1) {
          firstTimeElapsed = (System.nanoTime() - start);
          elapsedTime += firstTimeElapsed;
        } else {
          elapsedTime += (System.nanoTime() - start);
        }

        System.out.print(".");
      }
      System.out.println("");

      System.out.println("首次次操作花费时间：" + firstTimeElapsed / 1000 / 1000);
      System.out.println(testcount + "次操作平均花费时间：" + elapsedTime / testcount / 1000 / 1000);


    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
