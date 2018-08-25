/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.tests;

import com.gti.datamerge.DatabaseConnection.Mysql;
import com.gti.datamerge.DatabaseConnectionI;
import com.gti.datamerge.database.Table;
import com.gti.datamerge.mocks.DatabaseConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Assert;
import org.junit.*;

/**
 *
 * @author Xachman
 */
public class DatabaseConnectionMysqlTest {
	private final String connectionUrl = "jdbc:mysql://192.168.99.100:4000?user=root&password=root";

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

    @Test
    public void testTableOutput() {
        String connectionUrl = "jdbc:mysql://192.168.99.100:4000/database1?user=root&password=root";
        DatabaseConnectionI dbc = new Mysql(connectionUrl);

        List<Table> tables  = dbc.getAllTables();
        Table table = null;
        for(Table item: tables) {
            System.out.println(item.getName());
            if(item.getName().equals("users_meta")) table = item;
        }
        
        Assert.assertEquals("id", table.getPrimaryKey());
        Assert.assertEquals("user_id", table.getRelationship().getColumn());
        Assert.assertEquals("id", table.getRelationship().getParentColumn());
        Assert.assertEquals(5, table.getIncrement());

    }
}
