/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.mocks;

import com.gti.datamerge.DatabaseConnectionI;
import com.gti.datamerge.database.Column;
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
public class DatabaseConnection implements DatabaseConnectionI {
	private Map<String,List<Row>> tableRows = new HashMap<>();
	private List<Table> tables = new ArrayList<>();
	public DatabaseConnection() {
	}

	public DatabaseConnection(TablesYml tables, DataYml data) {
		setupTables(tables.getTables());	
		setData(data.getTables());
	}

	public void setupTables(List<TableYml> tableMap) {
		for(TableYml table: tableMap)	{
			List<String> columns = (List<String>) table.getColumns();
			List<Column> cols = new ArrayList<>();
			for(String columnName: columns) {
				cols.add(new Column(columnName));
			}
			tables.add(new Table(table.getName(), cols));
		}
	}
	public void setData(List<TableDataYml> data) {
		for(TableDataYml table: data ) {
			List<Row> rows =  new ArrayList<>();
			for(Map<String, String> map: table.getData()) {
				Row row = new Row();
				for(String key: map.keySet()) {
					Object val = map.get(key);
					if(val instanceof Integer) {	
						row.add(key, Integer.toString((int)val));
						continue;
					}
					row.add(key, val.toString());
				}
				rows.add(row);
			}
			tableRows.put(table.getName(), rows);
		}
	}	
	
	private void addTableData(String tableName, Map<String, String> data) {
		Row row = new Row();
		for(String key: data.keySet()) {
			row.add(key, data.get(key));
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
	
}
