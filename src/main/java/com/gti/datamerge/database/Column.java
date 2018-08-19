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
public class Column {
	private String name;	
	private String type;
	private Relationship relationship;

	public Column(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public Column(String name, String type, Relationship relationship) {
		this.name = name;
		this.type = type;
		this.relationship = relationship;
	}
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	public boolean hasRelationship() {
		if(relationship != null){
			return true;
		}
		return false;
	}
}
