package com.sprint.mission.discodeit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

public class PostgreSQLConnectionTest {

  private String URL = "jdbc:postgresql://localhost:5432/discodeit";
  private String USER = "discodeit_user";
  private String PASSWORD = "discodeit1234";

  @Test
  public void ConnectionTest() throws Exception {
    Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
    System.out.println(con);
    Statement pre = con.createStatement();
    ResultSet rs = pre.executeQuery("select * from users");

    if (rs.next()) {
      System.out.println(rs);
      System.out.println(rs.getString("id"));
    }
  }

}
