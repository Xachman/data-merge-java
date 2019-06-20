/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.database;

import com.gti.datamerge.Database;

/**
 *
 * @author Xachman
 */
public class Column {
	private String name;	
    private int type;
    public final static int NUMBER = 1;
    public final static int STRING = 2;
    public final static int DATETIME = 3;
	private Relationship relationship;

	public Column(String name) {
		this.name = name;
	}
	
	public Column(String name, int t) {
		type = t;
		this.name = name;
	}
	public Column(String name, Relationship relationship) {
		this.name = name;
		this.relationship = relationship;
	}
	public String getName() {
		return name;
	}

	
	public boolean hasRelationship() {
		if(relationship != null){
			return true;
		}
		return false;
	}


    public int getType() {
        return type; 
    }
}
