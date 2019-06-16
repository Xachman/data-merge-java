/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gti.datamerge.Database;
import com.gti.datamerge.DatabaseConnection.Mysql;
import com.gti.datamerge.DatabaseConnectionI;
import com.gti.datamerge.mocks.DataYml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import static org.junit.Assert.*;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author xach
 */
public class DataMergeTest extends BaseClass {
	private String connectionUrl = "";
	private String connectionUrlDB1 = "";
	private String dbcHost;
	public DataMergeTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() throws FileNotFoundException, IOException {
		InputStream actionsInput = new FileInputStream(new File(getResource("config.yml")));
		Config config = new Yaml().loadAs(actionsInput, Config.class);

		dbcHost = config.getHost();
		connectionUrl = "jdbc:mysql://"+dbcHost+":4000?user=root&password=root";
		connectionUrlDB1 = "jdbc:mysql://"+dbcHost+":4000/database1?user=root&password=root";
		ClassLoader classLoader = getClass().getClassLoader();
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

			Reader reader = Resources.getResourceAsReader("setup_test.sql");

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
		
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testDataMergeTable() throws FileNotFoundException {
		// TODO review the generated test code and remove the default call to fail.
        DatabaseConnectionI dbc1 = new Mysql("jdbc:mysql://"+dbcHost+":4000/database1?user=root&password=root");


        DatabaseConnectionI dbc2 = new Mysql("jdbc:mysql://"+dbcHost+":4000/database2?user=root&password=root");

        Database db1 = new Database(dbc1);
        Database db2 = new Database(dbc2);


        db1.merge(db2);
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(connectionUrlDB1);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            ResultSetMetaData user_md = rs.getMetaData();


            InputStream actionsInput = new FileInputStream(new File(getResource("expect_users.yml")));
            DataYml expect = new Yaml().loadAs(actionsInput, DataYml.class);
            int count = 0;


            while(rs.next()) {
                Map<String,Object> tableData = expect.getTables().get(0).getData().get(count);
                for(int i = 1; i <= user_md.getColumnCount(); i++) {
					String columnName = user_md.getColumnName(i);
					String expectStr =  tableData.get(columnName).toString();
					String assertStr = rs.getString(columnName);
                    assertEquals(expectStr, assertStr);
                }
                count++;
                 
            }

			ResultSet rs_users_meta = stmt.executeQuery("SELECT * FROM users_meta");
            ResultSetMetaData users_meta_md = rs_users_meta.getMetaData();
		    count = 0;	
            while(rs_users_meta.next()) {
                System.out.println(count);
                for(int i = 1; i <= users_meta_md.getColumnCount(); i++) {
                    String columnName = users_meta_md.getColumnName(i);
                    String result = rs_users_meta.getString(columnName);
                    Map<String,Object> tableData = expect.getTables().get(1).getData().get(count);
                    assertEquals(tableData.get(columnName).toString(), result);
                }
                count++;
                 
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
