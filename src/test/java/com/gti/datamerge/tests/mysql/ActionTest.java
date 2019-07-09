package com.gti.datamerge.tests.mysql;

import com.gti.datamerge.Action;
import com.gti.datamerge.DatabaseConnection.Mysql;
import com.gti.datamerge.database.Row;
import static org.junit.Assert.*;

import com.gti.datamerge.tests.BaseClass;
import com.gti.datamerge.tests.Config;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActionTest  extends BaseClass {
    private String dbcHost;
    private String connectionUrl;
    private Mysql mysql;
    @Before
    public void setUp() throws FileNotFoundException, IOException {
        InputStream actionsInput = new FileInputStream(new File(getResource("config.yml")));
        Config config = new Yaml().loadAs(actionsInput, Config.class);

        dbcHost = config.getHost();
        connectionUrl = "jdbc:mysql://"+dbcHost+":4000?user=root&password=root";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionUrl);

            ScriptRunner sr = new ScriptRunner(conn);
            sr.setLogWriter(null);

            Reader reader = Resources.getResourceAsReader("setup_mysql_actions.sql");


            sr.runScript(reader);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                conn.close();
            }catch(Exception e) {
                e.printStackTrace();
            }

        }
        String connectionUrl = "jdbc:mysql://"+dbcHost+":4000/actions?user=root&password=root";
        mysql = new Mysql(connectionUrl);
    }
    @Test
    public void testInsertActionWithSpecialCharacters() {
        String connectionUrl = "jdbc:mysql://"+dbcHost+":4000/actions?user=root&password=root";

        Row row = new Row();
        row.setVal("id", "1");
        row.setVal("content", "<p> 'test' \"test2\" `test3` </p>");
        row.setVal("created_date", "2019-07-01 10:00:00");

        Action action = new Action(Action.INSERT, row, "posts");

        List<Action> actions = new ArrayList<>();

        actions.add(action);

        mysql.setActions(actions);
        mysql.commit();

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionUrl);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM posts");
            ResultSetMetaData user_md = rs.getMetaData();


            while(rs.next()) {
                assertEquals(1,rs.getInt(1));
                assertEquals("<p> 'test' \"test2\" `test3` </p>",rs.getString(2));
                assertEquals("2019-07-01 10:00:00.0",rs.getString(3));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            fail("Something wrong");
        } finally {
            try {
                conn.close();
            }catch(Exception e) {
                e.printStackTrace();
            }

        }
    }
}
