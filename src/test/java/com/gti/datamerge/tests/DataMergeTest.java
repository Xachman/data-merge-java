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
	private final String connectionUrl = "jdbc:mysql://localhost:4000?user=root&password=root";
	private final String connectionUrlDB2 = "jdbc:mysql://localhost:4000/database2?user=root&password=root";

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

		DataMerge dataMerge = new DataMerge();

		dataMerge.mergeData();
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
