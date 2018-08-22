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


	private Action insertAction(Row row, String table) {
		return new Action(Action.INSERT, row, table);
	}	

    public List<Action> mergeTableActions(String tableName, Database db) {
		List<Row> rows = db.getRows(tableName);
		List<Row> dbRows = getRows(tableName);
		List<Action> actions = new ArrayList<>();

		for(Row row: rows) {
			boolean isIn = false;
			for(Row dbRow: dbRows) {
				int countIsE = 0;
				int totalCount = 0;
				for(String key: (Set<String>) row.getMap().keySet()) {
					String rowVal = (String) row.getMap().get(key);
					String dbVal = (String) dbRow.getMap().get(key);
					totalCount++;
					if(dbVal != null && dbVal.equals(rowVal)) {
						countIsE++;
					}
				}	
				if(countIsE == totalCount) {
					isIn = true;
				}
			}
			if(!isIn){
				actions.add(insertAction(row, tableName));
			}
		}
	
		return actions;	
    }
}
