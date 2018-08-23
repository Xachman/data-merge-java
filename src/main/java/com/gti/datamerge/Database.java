/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import com.gti.datamerge.database.Relationship;
import com.gti.datamerge.database.Row;
import com.gti.datamerge.database.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Xachman
 */
public class Database {
	private List<Table> tables;
	private DatabaseConnectionI dbc;
    private List<Action> actions = new ArrayList<>();

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
    private void actionsByRelation(Table table, String id, Database db, int relationId) {
		List<Row> dbRows = db.getRows(table.getName());
        Relationship relationship = table.getRelationship();
        int increment = table.getIncrement();
        for(Row row: dbRows) {

            if(row.getVal(relationship.getColumn()).equals(id)) {
                increment++;
                row.put(relationship.getColumn(), Integer.toString(relationId));
                addAction(Action.INSERT, row, table, increment);
            }
        }
        
    }
    public List<Action> mergeTableActions(String tableName, Database db) throws Exception {
		List<Row> rows = db.getRows(tableName);
		List<Row> dbRows = getRows(tableName);
        Table table = getTable(tableName);
        List<Table> relatedTables = dbc.getRelatedTables(table);
        int increment = table.getIncrement();

        System.out.println(relatedTables);

		for(Row row: rows) {
			if(!isRowInRows(row, dbRows)){
                String id = row.getVal(table.getPrimaryKey());
                increment++;
                addAction(Action.INSERT, row, table, increment);
                for(Table rTable: relatedTables) {
                    actionsByRelation(rTable, id, db, increment);
                }
			}
		}
	
		return actions;	
    }

    private void addAction(int type, Row row, Table table, int increment) {
        if(table.hasPrimaryKey()) {
            row.put(table.getPrimaryKey(), Integer.toString(increment));
            actions.add(insertAction(row, table));
        }else{
            actions.add(insertAction(row, table));
        }
    }

    private boolean isRowInRows(Row row, List<Row> rows) {
        for(Row dbRow: rows) {
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
                return true;
            }
        }
        return false;
    } 
}

