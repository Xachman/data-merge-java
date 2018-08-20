/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import com.gti.datamerge.database.Row;
import com.gti.datamerge.database.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Xachman
 */
public class Database {
	private List<Table> tables;
	private DatabaseConnectionI dbc;

	public Database(DatabaseConnectionI dbc) {
		this.dbc = dbc;
		tables = dbc.getAllTables();
	}

	public void merge(Database database) {
		
	}

	public List<Row> getRows(String tableName) {
		return dbc.getAll(tableName);
	}

	public List<Action> mergeTable(String tableName, Database db) {
		List<Row> result;
		List<Row> rows = getRows(tableName);
		List<Row> dbRows = db.getRows(tableName);
		List<Action> actions = new ArrayList<>();

		for(Row row: rows) {
			for(Row dbRow: dbRows) {
				int countIsE = 0;
				int totalCount = 0;
				Map<String,String> update = new HashMap<>();
				for(String key: (Set<String>) row.getMap().keySet()) {
					String rowVal = (String) row.getMap().get(key);
					String dbVal = (String) dbRow.getMap().get(key);
					totalCount++;
					if(dbVal == null || !dbVal.equals(rowVal)) {
						update.put(key, dbVal);
						continue;
					}
					countIsE++;
				}	
				System.out.println(dbRow.getMap());
				if(countIsE != totalCount) {
					actions.add(new Action("insert", row, tableName));	
				}
			}
		}			
			


		return actions;	
	}

	
}
