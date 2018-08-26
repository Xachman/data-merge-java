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
import com.gti.datamerge.DataMerge;
import com.gti.datamerge.Database;
import com.gti.datamerge.DatabaseConnection.Mysql;
import com.gti.datamerge.DatabaseConnectionI;
import com.gti.datamerge.mocks.DataYml;
import com.gti.datamerge.mocks.TableDataYml;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import org.apache.ibatis.jdbc.ScriptRunner;
import static org.junit.Assert.*;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author xach
 */
public class DataMergeTest extends BaseClass {
	private final String connectionUrl = "jdbc:mysql://localhost:4000?user=root&password=root";
	private final String connectionUrlDB1 = "jdbc:mysql://localhost:4000/database1?user=root&password=root";

	public DataMergeTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("setup_test.sql").getFile());
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

			Reader reader = new BufferedReader(new FileReader(file.getPath()));

			sr.runScript(reader);
			
			
		} catch (Exception e) {
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
	public void testDataMergeTable() {
		// TODO review the generated test code and remove the default call to fail.
        DatabaseConnectionI dbc1 = new Mysql("jdbc:mysql://localhost:4000/database1?user=root&password=root");


        DatabaseConnectionI dbc2 = new Mysql("jdbc:mysql://localhost:4000/database2?user=root&password=root");

        Database db1 = new Database(dbc1);
        Database db2 = new Database(dbc2);


        db1.mergeTable("users", db2);
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
                System.out.println("users");
                for(int i = 1; i <= user_md.getColumnCount(); i++) {
                    String columnName = user_md.getColumnName(i);
                    System.out.println(tableData.get(columnName)+"="+rs.getString(columnName));
                    assertEquals(tableData.get(columnName).toString(), rs.getString(columnName));
                }
                count++;
                 
            }

			ResultSet rs_users_meta = stmt.executeQuery("SELECT * FROM users_meta");
            ResultSetMetaData users_meta_md = rs_users_meta.getMetaData();
		    count = 0;	
            while(rs_users_meta.next()) {
                Map<String,Object> tableData = expect.getTables().get(1).getData().get(count);
                System.out.println("users_meta");
                for(int i = 1; i <= users_meta_md.getColumnCount(); i++) {
                    String columnName = users_meta_md.getColumnName(i);
                    System.out.println(columnName);
                    System.out.println(tableData.get(columnName)+"="+rs_users_meta.getString(columnName));
                    assertEquals(tableData.get(columnName).toString(), rs_users_meta.getString(columnName));
                }
                count++;
                 
            }
		} catch (Exception e) {
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
