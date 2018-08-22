/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge;

import com.gti.datamerge.database.Row;
import com.gti.datamerge.database.Table;
import java.util.List;

/**
 *
 * @author Xachman
 */
public interface DatabaseConnectionI {
	public Row get(Table table, int id);	
	public List<Row> getAll(Table table);	
	public List<Row> getAll(String tableName);	
	public List<Table> getAllTables();	
    public int getNextIncrement(String tableName);
    public List<Table> getRelatedTables(Table table);
}
