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
    private Relationship relationship;
	
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
	public Table(String name, List<Column> cols, String pk, int increment, Relationship relationship) {
		columns = cols;	
		this.name = name;
        this.primaryKey = pk;
        this.increment = increment;
        this.relationship = relationship;
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
    
    public boolean hasRelationship() {
        if(relationship != null) {
            return true;
        }
        return false;
    }
    public boolean hasRelationship(Table table) {
        if(relationship == null) return false;
        if(getRelationship().getTable().equals(table.getName())) return true;
        return false;
    }
    public Relationship getRelationship() {
        return relationship;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
