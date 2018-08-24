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
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.ibatis.jdbc.ScriptRunner;
import static org.junit.Assert.*;

/**
 *
 * @author xach
 */
public class DataMergeTest {
	private final String connectionUrl = "jdbc:mysql://192.168.99.100:4000?user=root&password=root";
	private final String connectionUrlDB2 = "jdbc:mysql://192.168.99.100:4000/database2?user=root&password=root";

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
	public void testDataMerge() {
		// TODO review the generated test code and remove the default call to fail.
        DatabaseConnectionI dbc1 = new Mysql("jdbc:mysql://192.168.99.100:4000?user=root&password=root&database=database1");


        DatabaseConnectionI dbc2 = new Mysql("jdbc:mysql://192.168.99.100:4000?user=root&password=root&database=database2");

        Database db1 = new Database(dbc1);
        Database db2 = new Database(dbc2);

        db1.mergeTable("users", db2);
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(connectionUrlDB2);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");

			rs.last();
			int row = rs.getRow();

			assertEquals(4, row);

			
			
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

}
