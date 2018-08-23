/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.database;

/**
 *
 * @author Xachman
 */
public class Relationship {
	private String table;
	private String column;
    private String parentColumn;

	public Relationship(String table, String column, String parentColumn) {
		this.table = table;
		this.column = column;
        this.parentColumn = parentColumn;
	}

	public String getTable() {
		return table;
	}

	public String getColumn() {
		return column;
	}

    public String getParentColumn() {
        return parentColumn;
    }	
}
