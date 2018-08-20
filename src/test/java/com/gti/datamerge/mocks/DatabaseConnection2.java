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
public class DatabaseConnection2 implements DatabaseConnectionI {
	private Map<String,List<Row>> tableRows = new HashMap<>();
	private List<Table> tables = new ArrayList<>();
	public DatabaseConnection2() {
		List<Row> rows1 = new ArrayList<>();
		Row row1 = new Row();
		row1.add("id","1");
		row1.add("email","sandra@gmail.com");
		row1.add("phone","1234567890");

		Row row2 = new Row();
		row2.add("id","2");
		row2.add("email","tim@gmail.com");
		row2.add("phone","0987654321");

		Row row6 = new Row();
		row6.add("id","3");
		row6.add("email","jim@gmail.com");
		row6.add("phone","5555551234");

		rows1.add(row1);
		rows1.add(row2);
		rows1.add(row6);

		tableRows.put("users", rows1);

		List<Row> rows2 = new ArrayList<>();
		Row row3 = new Row();
		row3.add("id","1");
		row3.add("user_id","1");
		row3.add("meta_key","color");
		row3.add("meta_value","red");

		Row row4 = new Row();
		row4.add("id","2");
		row4.add("user_id","2");
		row4.add("meta_key","color");
		row4.add("meta_value","blue");

		Row row5 = new Row();
		row5.add("id","2");
		row5.add("user_id","2");
		row5.add("meta_key","food");
		row5.add("meta_value","hot dog");

		Row row7 = new Row();
		row7.add("id","3");
		row7.add("user_id","2");
		row7.add("meta_key","food");
		row7.add("meta_value","pizza");
		
		rows2.add(row3);
		rows2.add(row4);
		rows2.add(row5);
		rows2.add(row7);


		tableRows.put("users_meta", rows2);

		setupTables();
	}

	private void setupTables() {
		Column primKey = new Column("id", "Number");
		Column email = new Column("email", "String");
		Column phone = new Column("phone", "String");
		List<Column> columns = new ArrayList<>();
		columns.add(primKey);
		columns.add(email);
		columns.add(phone);
		Table usersTable = new Table("users", columns);
		tables.add(usersTable);

		Column user_id = new Column("user_id", "Number");
		Column meta_key = new Column("meta_key", "String");
		Column meta_value = new Column("meta_value", "String");
		List<Column> columns2 = new ArrayList<>();
		columns2.add(primKey);
		columns2.add(user_id);
		columns2.add(meta_key);
		columns2.add(meta_value);
		Table usersMeta = new Table("users_meta", columns2);
		tables.add(usersMeta);
	}	
	
	@Override
	public Row get(Table table, int id) {
		return tableRows.get(table.getName()).get(id);
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
	public List<Row> getAll(String tableName) {
		
		return tableRows.get(tableName);
	}
	
}
