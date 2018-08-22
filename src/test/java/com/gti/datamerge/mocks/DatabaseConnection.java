/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.mocks;

import com.gti.datamerge.AbstractDatabaseConnection;
import com.gti.datamerge.database.Column;
import com.gti.datamerge.database.Relationship;
import com.gti.datamerge.database.Row;
import com.gti.datamerge.database.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Xachman
 */
public class DatabaseConnection extends AbstractDatabaseConnection {
	private Map<String,List<Row>> tableRows = new HashMap<>();
	private List<Table> tables = new ArrayList<>();
	public DatabaseConnection() {
	}

	public DatabaseConnection(TablesYml tables, DataYml data) {
		setupTables(tables.getTables(), data.getTables());	
		setData(data.getTables());
	}

	public void setupTables(List<TableYml> tableMap, List<TableDataYml> data) {
		for(TableYml table: tableMap)	{
			List<String> columns = (List<String>) table.getColumns();
			List<Column> cols = new ArrayList<>();
			for(String columnName: columns) {
				cols.add(new Column(columnName));
			}
            int increment = 0;
            int newInc = 0;
            if(table.getPrimary_key() != null) {
                for(TableDataYml tData: data) {
                    if(tData.getName().equals(table.getName())) {
                        for(Map<String, Object> map: tData.getData()) {
                            newInc = (int) map.get(table.getPrimary_key());
                            if( newInc > increment) {
                                increment = newInc;
                            }
                        }
                    }
                }
            }

            Relationship relationship = null;
            if(table.getRelation() != null && table.getRelation().size() > 0) {
                relationship = new Relationship(table.getRelation().get("table"), table.getRelation().get("column"));
            }
			tables.add(new Table(table.getName(), cols, table.getPrimary_key(), newInc, relationship));
		}
	}
	public void setData(List<TableDataYml> data) {
		for(TableDataYml table: data ) {
			List<Row> rows =  new ArrayList<>();
			for(Map<String, Object> map: table.getData()) {
				Row row = new Row();
				for(String key: map.keySet()) {
					Object val = map.get(key);
					if(val instanceof Integer) {	
						row.put(key, Integer.toString((int)val));
						continue;
					}
					row.put(key, val.toString());
				}
				rows.add(row);
			}
			tableRows.put(table.getName(), rows);
		}
	}	
	
	private void addTableData(String tableName, Map<String, String> data) {
		Row row = new Row();
		for(String key: data.keySet()) {
			row.put(key, data.get(key));
		}
	}

	@Override
	public Row get(Table table, int id) {
		return tableRows.get(table.getName()).get(id);
	}

	@Override
	public List<Row> getAll(String tableName) {
		
		return tableRows.get(tableName);
	}

	@Override
	public List<Row> getAll(Table table) {
		return tableRows.get(table.getName());
	}

	@Override
	public List<Table> getAllTables() {
		return tables;
	}

    @Override
    public int getNextIncrement(String tableName) {
        return tableRows.get(tableName).size();
    }
	
}
