/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gti.datamerge.mocks;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Xachman
 */
public class TableYml {
	private String name;
	private List<String> columns;
	private String primary_key;	
	private Map<String, String> relation;
	public TableYml() {
		
	}
	public TableYml(String name, List<String> columns, String primary_key, Map<String, String> relation) {
		this.name = name;
		this.columns = columns;
		this.primary_key = primary_key;
		this.relation = relation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public String getPrimary_key() {
		return primary_key;
	}

	public void setPrimary_key(String primary_key) {
		this.primary_key = primary_key;
	}

	public Map<String, String> getRelation() {
		return relation;
	}

	public void setRelation(Map<String, String> relation) {
		this.relation = relation;
	}
}
