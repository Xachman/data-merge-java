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
import org.omg.CosNaming.NamingContextPackage.NotFound;

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

    private Table getTable(String tableName) throws Exception {
        for(Table table: tables) {
            if(table.getName().equals(tableName))return table;
        } 
        throw new Exception("Table not found");
    }
            
	private Action insertAction(Row row, Table table) {
		return new Action(Action.INSERT, row, table.getName());
	}	

    public List<Action> mergeTableActions(String tableName, Database db) throws Exception {
		List<Row> rows = db.getRows(tableName);
		List<Row> dbRows = getRows(tableName);
		List<Action> actions = new ArrayList<>();
        Table table = getTable(tableName);
        boolean hasPrimaryKey =  table.hasPrimaryKey();
        int increment = table.getIncrement();

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
                if(table.hasPrimaryKey()) {
                    increment++;
                    row.put(table.getPrimaryKey(), Integer.toString(increment));
				    actions.add(insertAction(row, table));
                }else{
				    actions.add(insertAction(row, table));
                }
                
			}
		}
	
		return actions;	
    }
}
