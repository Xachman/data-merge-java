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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	    List<Table> baseTables = getBaseTables();

        for(Table table: tables) {
            mergeTable(table.getName(), database);
        }
	}
    private List<Table> getBaseTables() {
        List<Table> tables = new ArrayList<>();
        List<Table> allTables = dbc.getAllTables();

        for(Table table: allTables){
            if(!table.hasRelationship()) {
                tables.add(table);
            }
        }
        return tables;
    }
    public void mergeTable(String name, Database db) {
        try {
            List<Action> actions = mergeTableActions(name, db);
            dbc.setActions(actions);
            dbc.commit();
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
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
		return new Action(Action.INSERT, row,  table.getName());
	}	
    public List<Action> mergeTableActions(String tableName, Database db) throws Exception {
		List<Row> rows = db.getRows(tableName);
		List<Row> dbRows = getRows(tableName);
        List<Row> addRows = new ArrayList<>();
        System.out.println(rows);
        Table table = getTable(tableName);
        List<Table> relatedTables = dbc.getRelatedTables(table);
        Map<String, String> ids = new HashMap<>();
        int increment = table.getIncrement();


        for(Row row: rows) {
            if(!isRowInRows(row, dbRows)){
                increment++;
                addAction(Action.INSERT, row, table, increment);
                addRows.add(row);
                ids.put(row.getVal(table.getPrimaryKey()), Integer.toString(increment));
            }
        }
        

        if(relatedTables.size() > 0) {
            for(Table rTable: relatedTables) {
                addActionsForTable(rTable, db, addRows, ids);
            }
        }
		return actions;	
    }

    private void addAction(int type, final Row row, Table table, int increment) {
        Row newRow = new Row(row);
        for(String column: row.getColumns()) {
            newRow.put(column, row.getVal(column));
        }
        if(table.hasPrimaryKey()) {
            newRow.put(table.getPrimaryKey(), Integer.toString(increment));
            actions.add(insertAction(newRow, table));
        }else{
            actions.add(insertAction(newRow, table));
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

    private void addActionsForTable(Table table, Database db, List<Row> rows, Map<String,String> ids) {
        int increment = table.getIncrement();
        List<Row> dbRows = db.getRows(table.getName());
        for(Row row: rows) {

            for(Row dbRow: dbRows) {
                if(dbRow.getVal(table.getRelationship().getColumn())
                        .equals(row.getVal(table.getRelationship().getParentColumn()))) {
                    Row newRow = new Row(dbRow);  
                    
                    newRow.put(table.getPrimaryKey(),Integer.toString(increment));
                    newRow.put(table.getRelationship().getColumn(), ids.get(row.getVal(table.getRelationship().getParentColumn())));
                    increment++;
                    addAction(Action.INSERT, newRow, table, increment);
                }
            }
        }
    }
}

