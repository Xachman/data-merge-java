/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.tests;

import com.gti.datamerge.Action;
import com.gti.datamerge.Database;
import com.gti.datamerge.database.Row;
import com.gti.datamerge.mocks.DatabaseConnection;
import com.gti.datamerge.mocks.DatabaseConnection2;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Xachman
 */
public class DatabaseTest {

	@Test
	public void testMerge() {
		DatabaseConnection dbc = new DatabaseConnection();
		DatabaseConnection2 dbc2 = new DatabaseConnection2();
		
		Database db1 = new Database(dbc);
		Database db2 = new Database(dbc2);

		List<Action> actions = db2.mergeTable("users",db1);


		assertEquals(1, actions.size());
		
	}
	
}
