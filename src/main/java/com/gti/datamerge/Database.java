    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import com.gti.datamerge.config.Config;
import com.gti.datamerge.database.Relationship;
import com.gti.datamerge.database.Row;
import com.gti.datamerge.database.Table;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public Database(DatabaseConnectionI dbc, Config config) {
        this.dbc = dbc;
        tables = dbc.getAllTables();
        for(com.gti.datamerge.config.Table table: config.getTables()) {
            Table cTable = getTable(table.getName());
            for(com.gti.datamerge.config.Relationship relationship: table.getRelationships())
            cTable.addRelationship(new Relationship(relationship.getParent().getName(), relationship.getColumn(), relationship.getParent().getColumn()));
        }
    }

	public void merge(Database database) {
        for(Table table: tables) {
            if(!table.hasRelationship())
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
        throw new IllegalArgumentException("Table "+tableName+" not found");
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

    private HashSet<String> createRowHashSet(List<Row> rows) {
	    HashSet<String> result = new HashSet<>();
	    for(Row row: rows) {
            result.add(convertRowToHash(row));
        }
	    return result;
    }
    private String convertRowToHash(Row row) {
        int count = 0;
        int totalCount = row.getMap().size();
        String[] strings = new String[totalCount];
        for(String key: row.getMap().keySet()) {
            String rowVal =  row.getMap().get(key);
            strings[count] = rowVal;
            count++;
        }
        return convertStringToHash(String.join(" ",strings));
    }
    private String convertStringToHash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(String.join(" ", str).getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Action> mergeTablesActions(Database db2) {
	    List<Action> actions = new ArrayList<>();
        for(Table table: tables) {
            if(!table.hasRelationship()){
                actions.addAll(getActions(table, db2));
            }
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

        HashSet<String> rowsHashes = createRowHashSet(rows);


        for(Row row: mergeRows) {
            String rowHash = convertRowToHash(row);
            if(!rowsHashes.contains(rowHash) || table.hasRelationship() && pIds.get(row.getVal(table.getRelationship().getColumn())) != null){
                if(needsUpdate(table, row, rows, pIds)) {
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

        for(Table rTable : getRelatedTables(table)) {
            actions.addAll(getActions(rTable, db, ids));
        }
        return actions;

    }
    public List<Table> getRelatedTables(Table table) {
        List<Table> result = new ArrayList<>();
        for(Table tItem: tables)  {
            if(tItem.hasRelationship(table)) {
                result.add(tItem);
            }
        }

        return result;
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
                   int columnCount = row.getColumns().size();
                   int count = 0;
                   for(String col: row.getColumns()) {
                       if(row.getVal(col) == null && cRow.getVal(col) == null || row.getVal(col) != null && row.getVal(col).equals(cRow.getVal(col))) {
                           count++;
                       }
                   }
                   if(count >= columnCount*0.5) {
                       return true;
                   }
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

