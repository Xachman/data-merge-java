/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.database;

import java.util.List;

/**
 *
 * @author Xachman
 */
public class Table {
	private List<Column> columns;
	private String name;
    private String primaryKey;
    private int increment;
	
	public Table(String name, List<Column> cols) {
		columns = cols;	
		this.name = name;
	}

	public Table(String name, List<Column> cols, String pk, int increment) {
		columns = cols;	
		this.name = name;
        this.primaryKey = pk;
        this.increment = increment;
	}
	public String getName() {
		return name;
	}

    public boolean hasPrimaryKey() {
        if(primaryKey != null) return true;
        return false;
    }
    public String getPrimaryKey() {
        return primaryKey;        
    }

    public int getIncrement() {
        return increment;
    }

}
