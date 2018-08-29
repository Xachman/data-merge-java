/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.database;

import java.util.ArrayList;
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
    private List<Relationship> relationships = new ArrayList<>();
	
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
	public Table(String name, List<Column> cols, String pk, int increment, List<Relationship> relationships) {
		columns = cols;	
		this.name = name;
        this.primaryKey = pk;
        this.increment = increment;
        this.relationships = relationships;
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
        if(relationships.size() > 0) {
            return true;
        }
        return false;
    }
    public boolean hasRelationship(Table table) {
        if(relationships == null || relationships.size() == 0) return false;
        if(getRelationship() != null && getRelationship().getTable().equals(table.getName())) return true;
        return false;
    }
    public Relationship getRelationship() {
        return relationships.get(0);
    }

    public List<Column> getColumns() {
        return columns;
    }
}
