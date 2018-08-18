/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.database;

/**
 *
 * @author ziron
 */
public class Relationship {
	private String table;
	private String column;

	public Relationship(String table, String column) {
		this.table = table;
		this.column = column;
	}

	public String getTable() {
		return table;
	}

	public String getColumn() {
		return column;
	}

	
}
