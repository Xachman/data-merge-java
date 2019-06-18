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
import org.apache.commons.lang.ArrayUtils;

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
        for(Table table: tables) {
        //    if(!table.hasRelationship())
            mergeTable(table.getName(), database);
        }
	}
    
    public void mergeTable(String name, Database db) {
        try {
            List<Action> actions = mergeTableActions(name, db);
            dbc.setActions(actions);
            dbc.commit();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	public List<Row> getRows(String tableName) {
		return dbc.getAll(tableName);
	}

    private Table getTable(String tableName) throws IllegalArgumentException {
        for(Table table: tables) {
            if(table.getName().equals(tableName))return table;
        } 
        throw new IllegalArgumentException("Table not found");
    }
            
    public List<Action> mergeTableActions(String tableName, Database db) throws IllegalArgumentException {
        Table table = getTable(tableName);

        List<Action> actions = new ArrayList<>();
        actions.addAll(getActions(table, db));

		return actions;
    }

    private Action addAction(int type, final Row row, Table table, int increment) {
        Row newRow = new Row(row);
        if(table.hasPrimaryKey() && increment > 0) {
            newRow.put(table.getPrimaryKey(), Integer.toString(increment));
        }
        return new Action(type, newRow,  table.getName());
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


    public List<Action> mergeTablesActions(Database db2) {
        List<Table> bTables = getBaseTables();
        List<Action> actions = new ArrayList<>();
        for(Table table: bTables) {
//            actions.addAll(getRelationshipActions(table, db2, getActions(table, db2)));
        }
        return actions;
    }
    private List<Action> getActions(Table table, Database db) {
	    return getActions(table, db, new HashMap<>());
    }
    private List<Action> getActions(Table table, Database db, Map<String,String> pIds) {
		List<Row> mergeRows = db.getRows(table.getName());
		List<Row> rows = getRows(table.getName());
        Map<String, String> ids = new HashMap<>();
        List<Action> actions = new ArrayList<>();
        int increment = table.getIncrement();


        for(Row row: mergeRows) {
            if(!isRowInRows(row, rows)){
                if(needsUpdate(table, row, mergeRows, pIds)) {
                    actions.add(addAction(Action.UPDATE, row, table, 0));
                    continue;
                }
                increment++;
                ids.put(row.getVal(table.getPrimaryKey()), Integer.toString(increment));
                if(table.hasRelationship()) {
                    String nId = pIds.get(row.getVal(table.getRelationship().getColumn()));
                    if(nId != null) {
                        row.setVal(table.getRelationship().getColumn(), nId);
                    }
                }
                actions.add(addAction(Action.INSERT, row, table, increment));
            }
        }
        List<Table> relatedTables = dbc.getRelatedTables(table);
        for(Table rTable : relatedTables) {
            actions.addAll(getActions(rTable, db, ids));
        }
        return actions;

    }
    private boolean needsUpdate(Table table, Row row, List<Row> rows, Map<String,String> pIds) {
        if(table.hasRelationship()) {
            String pc = table.getRelationship().getColumn();
            String pk = table.getPrimaryKey();
            String pcv = row.getVal(pc);
            if(pIds.get(pcv) != null) {
                return false;
            }
            for(Row cRow: rows) {
               if(pk != null && pc != null && row.getVal(pc).equals(cRow.getVal(pc)) && row.getVal(pk).equals(cRow.getVal(pk))) {
                   return true;
               }
           }
        }
	    return false;
    }

    private List<Table> getBaseTables() {
        List<Table> bTables = new ArrayList<>();
        List<Table> tables = dbc.getAllTables();

        for(Table table: tables) {
            if(!table.hasRelationship()) {
                bTables.add(table);
            }
        }
        
        return tables;
    }
}

